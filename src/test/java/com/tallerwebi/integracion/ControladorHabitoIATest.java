package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioHabito;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.dominio.interfaz.ServicioCategoria;
import com.tallerwebi.dominio.interfaz.ServicioHabito;
import com.tallerwebi.dominio.interfaz.ServicioLogro;
import com.tallerwebi.dominio.servicios.ServicioHabitoIA;
import com.tallerwebi.presentacion.ControladorHabitoIA;
import com.tallerwebi.presentacion.DTO.HabitoObjetivoDTO;
import com.tallerwebi.presentacion.DTO.HabitoSugeridoDTO;
import com.tallerwebi.presentacion.DTO.RegistroHabitoDTO;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControladorHabitoIATest {

  private ServicioHabitoIA servicioHabitoIAMock;
  private ServicioHabito servicioHabitoMock;
  private ServicioCategoria servicioCategoriaMock;
  private ServicioLogro servicioLogroMock;
  private ControladorHabitoIA controladorHabitoIA;
  private HttpServletRequest requestMock;
  private HttpSession sessionMock;

  private static final String OBJETIVO = "Quiero mejorar mi alimentación";

  @BeforeEach
  public void init() {
    servicioHabitoIAMock = mock(ServicioHabitoIA.class);
    servicioHabitoMock = mock(ServicioHabito.class);
    servicioCategoriaMock = mock(ServicioCategoria.class);
    servicioLogroMock = mock(ServicioLogro.class);
    controladorHabitoIA =
      new ControladorHabitoIA(
        servicioHabitoIAMock,
        servicioHabitoMock,
        servicioCategoriaMock,
        servicioLogroMock
      );

    requestMock = mock(HttpServletRequest.class);
    sessionMock = mock(HttpSession.class);
    when(requestMock.getSession()).thenReturn(sessionMock);
  }

  // ---------- Tests existentes de /recomendar ----------

  @Test
  public void recomendarDeberiaRetornarRespuestaExitosa() throws JsonProcessingException {
    HabitoObjetivoDTO dto = dadoQueTengoUnObjetivo(OBJETIVO);
    HabitoSugeridoDTO sugerencia = new HabitoSugeridoDTO();

    sugerencia.setNombre("Comer saludable");
    sugerencia.setDescripcion("Preparar comidas saludables diariamente");
    sugerencia.setFrecuencia("Diario");
    sugerencia.setCategoria("Salud");

    dadoQueElServicioResponde(sugerencia);

    ResponseEntity<?> response = cuandoRecomiendoUnHabito(dto);
    entoncesLaRespuestaEsExitosa(response);
  }

  @Test
  public void recomendarDeberiaRetornarError500SiOcurreUnaExcepcion()
    throws JsonProcessingException {
    HabitoObjetivoDTO dto = dadoQueTengoUnObjetivo(OBJETIVO);
    dadoQueElServicioLanzaUnaExcepcion("Error comunicando con Gemini");

    ResponseEntity<?> response = cuandoRecomiendoUnHabito(dto);
    entoncesObtengoError500(response);
  }

  // ---------- Tests nuevos de /crear-desde-sugerencia ----------

  @Test
  public void crearDesdeSugerenciaDeberiaRetornar401SiNoHayUsuarioLogueado() {
    dadoQueNoHayUsuarioLogueado();
    HabitoSugeridoDTO sugerido = dadoQueTengoUnaSugerencia();

    ResponseEntity<?> response = cuandoCreoElHabitoDesdeLaSugerencia(sugerido);

    entoncesLaRespuestaTieneStatusCode(response, HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void crearDesdeSugerenciaDeberiaCrearElHabitoSiLaCategoriaCoincide()
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException {
    Usuario usuario = dadoQueHayUnUsuarioLogueado();
    dadoQueExisteLaCategoria("Salud", 3);
    Habito habitoCreado = mock(Habito.class);
    when(servicioHabitoMock.obtenerHabito(any(RegistroHabitoDTO.class))).thenReturn(habitoCreado);

    HabitoSugeridoDTO sugerido = dadoQueTengoUnaSugerencia();

    ResponseEntity<?> response = cuandoCreoElHabitoDesdeLaSugerencia(sugerido);

    entoncesLaRespuestaTieneStatusCode(response, HttpStatus.OK);
    verify(servicioHabitoMock, times(1)).agregarHabitoParaUsuario(habitoCreado, usuario);
  }

  @Test
  public void crearDesdeSugerenciaDeberiaUsarLaPrimeraCategoriaComoFallbackSiNoHayCoincidencia()
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException {
    dadoQueHayUnUsuarioLogueado();
    dadoQueExisteLaCategoria("Productividad", 1);
    Habito habitoCreado = mock(Habito.class);
    when(servicioHabitoMock.obtenerHabito(any(RegistroHabitoDTO.class))).thenReturn(habitoCreado);

    HabitoSugeridoDTO sugerido = dadoQueTengoUnaSugerencia(); // categoria = "Salud", no existe

    ResponseEntity<?> response = cuandoCreoElHabitoDesdeLaSugerencia(sugerido);

    entoncesLaRespuestaTieneStatusCode(response, HttpStatus.OK);
  }

  @Test
  public void crearDesdeSugerenciaDeberiaRetornar409SiYaExisteUnHabitoConEseNombre()
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException {
    dadoQueHayUnUsuarioLogueado();
    when(servicioCategoriaMock.obtenerCategorias()).thenReturn(Collections.emptyList());
    when(servicioHabitoMock.obtenerHabito(any(RegistroHabitoDTO.class)))
      .thenReturn(mock(Habito.class));
    doThrow(new HabitoExistenteExeption())
      .when(servicioHabitoMock)
      .agregarHabitoParaUsuario(any(Habito.class), any(Usuario.class));

    HabitoSugeridoDTO sugerido = dadoQueTengoUnaSugerencia();

    ResponseEntity<?> response = cuandoCreoElHabitoDesdeLaSugerencia(sugerido);

    entoncesLaRespuestaTieneStatusCode(response, HttpStatus.CONFLICT);
  }

  @Test
  public void crearDesdeSugerenciaDeberiaRetornar400SiSeAlcanzoElLimiteDeHabitos()
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException {
    dadoQueHayUnUsuarioLogueado();
    when(servicioCategoriaMock.obtenerCategorias()).thenReturn(Collections.emptyList());
    when(servicioHabitoMock.obtenerHabito(any(RegistroHabitoDTO.class)))
      .thenReturn(mock(Habito.class));
    doThrow(new LimiteHabitosAlcanzadoException())
      .when(servicioHabitoMock)
      .agregarHabitoParaUsuario(any(Habito.class), any(Usuario.class));

    HabitoSugeridoDTO sugerido = dadoQueTengoUnaSugerencia();

    ResponseEntity<?> response = cuandoCreoElHabitoDesdeLaSugerencia(sugerido);

    entoncesLaRespuestaTieneStatusCode(response, HttpStatus.BAD_REQUEST);
  }

  @Test
  public void crearDesdeSugerenciaDeberiaIndicarLogroDesbloqueadoCuandoCorresponde()
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException {
    Usuario usuario = dadoQueHayUnUsuarioLogueadoConHabitos(2); // pasa de 2 a 3 -> logro "Constante"
    dadoQueExisteLaCategoria("Salud", 3);
    Habito habitoCreado = mock(Habito.class);
    when(servicioHabitoMock.obtenerHabito(any(RegistroHabitoDTO.class))).thenReturn(habitoCreado);

    HabitoSugeridoDTO sugerido = dadoQueTengoUnaSugerencia();

    ResponseEntity<?> response = cuandoCreoElHabitoDesdeLaSugerencia(sugerido);

    entoncesLaRespuestaTieneStatusCode(response, HttpStatus.OK);
    verify(servicioLogroMock, times(1)).verificarYAsignarLogros(usuario, 3);

    Map<?, ?> cuerpo = (Map<?, ?>) response.getBody();
    assertThat(cuerpo.get("mostrarLogro"), equalTo(true));
    assertThat(cuerpo.get("tituloLogro"), equalTo("Constante"));
  }

  @Test
  public void crearDesdeSugerenciaNoDeberiaIndicarLogroSiNoCorresponde()
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException {
    dadoQueHayUnUsuarioLogueadoConHabitos(5); // pasa de 5 a 6 -> no hay logro definido
    dadoQueExisteLaCategoria("Salud", 3);
    Habito habitoCreado = mock(Habito.class);
    when(servicioHabitoMock.obtenerHabito(any(RegistroHabitoDTO.class))).thenReturn(habitoCreado);

    HabitoSugeridoDTO sugerido = dadoQueTengoUnaSugerencia();

    ResponseEntity<?> response = cuandoCreoElHabitoDesdeLaSugerencia(sugerido);

    entoncesLaRespuestaTieneStatusCode(response, HttpStatus.OK);

    Map<?, ?> cuerpo = (Map<?, ?>) response.getBody();
    assertThat(cuerpo.get("mostrarLogro"), equalTo(false));
  }

  // ---------- Helpers dado ----------

  private HabitoObjetivoDTO dadoQueTengoUnObjetivo(String objetivo) {
    HabitoObjetivoDTO dto = new HabitoObjetivoDTO();
    dto.setObjetivo(objetivo);
    return dto;
  }

  private HabitoSugeridoDTO dadoQueTengoUnaSugerencia() {
    HabitoSugeridoDTO sugerido = new HabitoSugeridoDTO();
    sugerido.setNombre("Meditar");
    sugerido.setDescripcion("Meditar 10 minutos por día");
    sugerido.setFrecuencia("Diario");
    sugerido.setCategoria("Salud");
    return sugerido;
  }

  private void dadoQueElServicioResponde(HabitoSugeridoDTO sugerencia)
    throws JsonProcessingException {
    when(servicioHabitoIAMock.recomendar(eq(OBJETIVO))).thenReturn(sugerencia);
  }

  private void dadoQueElServicioLanzaUnaExcepcion(String mensaje) throws JsonProcessingException {
    when(servicioHabitoIAMock.recomendar(eq(OBJETIVO))).thenThrow(new RuntimeException(mensaje));
  }

  private void dadoQueNoHayUsuarioLogueado() {
    when(sessionMock.getAttribute("usuario")).thenReturn(null);
  }

  private Usuario dadoQueHayUnUsuarioLogueado() {
    Usuario usuario = mock(Usuario.class);
    when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
    when(usuario.getUsuarioHabitos()).thenReturn(Collections.emptyList());
    return usuario;
  }

  private Usuario dadoQueHayUnUsuarioLogueadoConHabitos(int cantidadHabitosActuales) {
    Usuario usuario = mock(Usuario.class);
    when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
    when(usuario.getUsuarioHabitos())
      .thenReturn(Collections.nCopies(cantidadHabitosActuales, mock(UsuarioHabito.class)));
    return usuario;
  }

  private void dadoQueExisteLaCategoria(String nombre, Integer id) {
    Categoria categoria = mock(Categoria.class);
    when(categoria.getNombre()).thenReturn(nombre);
    when(categoria.getId()).thenReturn(id);
    when(servicioCategoriaMock.obtenerCategorias()).thenReturn(List.of(categoria));
  }

  // ---------- Helpers cuando ----------

  private ResponseEntity<?> cuandoRecomiendoUnHabito(HabitoObjetivoDTO dto) {
    return controladorHabitoIA.preguntar(dto);
  }

  private ResponseEntity<?> cuandoCreoElHabitoDesdeLaSugerencia(HabitoSugeridoDTO sugerido) {
    return controladorHabitoIA.crearDesdeSugerencia(sugerido, requestMock);
  }

  // ---------- Helpers entonces ----------

  private void entoncesLaRespuestaEsExitosa(ResponseEntity<?> response) {
    entoncesLaRespuestaTieneStatusCode(response, HttpStatus.OK);
    HabitoSugeridoDTO body = (HabitoSugeridoDTO) response.getBody();
    assertThat(body.getNombre(), equalTo("Comer saludable"));
    assertThat(body.getCategoria(), equalTo("Salud"));
  }

  private void entoncesObtengoError500(ResponseEntity<?> response) {
    entoncesLaRespuestaTieneStatusCode(response, HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody().toString(), equalTo("Error comunicando con Gemini"));
  }

  private void entoncesLaRespuestaTieneStatusCode(ResponseEntity<?> response, HttpStatus status) {
    assertThat(response.getStatusCodeValue(), equalTo(status.value()));
  }
}
