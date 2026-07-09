package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Publicacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.dominio.excepcion.UsuarioYaUnidoAHabitoException;
import com.tallerwebi.dominio.interfaz.ServicioComunidad;
import com.tallerwebi.dominio.servicios.ServicioHabitoCompartido;
import com.tallerwebi.presentacion.DTO.ComentarioDTO;
import com.tallerwebi.presentacion.DTO.PublicacionDTO;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorComunidadTest {

  private ServicioComunidad servicioComunidad;
  private ControladorComunidad controladorComunidad;
  private HttpServletRequest request;
  private HttpSession session;
  private ServicioHabitoCompartido servicioHabitoCompartido;

  @BeforeEach
  public void init() {
    servicioComunidad = mock(ServicioComunidad.class);
    servicioHabitoCompartido = mock(ServicioHabitoCompartido.class);
    controladorComunidad = new ControladorComunidad(servicioComunidad, servicioHabitoCompartido);
    request = mock(HttpServletRequest.class);
    session = mock(HttpSession.class);

    when(request.getSession()).thenReturn(session);
  }

  @Test
  public void irAComunidadSinUsuarioEnSesionDeberiaRedirigirAlLogin() {
    when(session.getAttribute("usuario")).thenReturn(null);

    ModelAndView modelAndView = controladorComunidad.irAComunidad(request);

    assertThat(modelAndView.getViewName(), equalTo("redirect:/login"));
  }

  @Test
  public void irAComunidadConUsuarioEnSesionDeberiaMostrarVistaComunidad() {
    Usuario usuario = new Usuario();
    Publicacion publicacion = new Publicacion();

    when(session.getAttribute("usuario")).thenReturn(usuario);
    when(servicioComunidad.obtenerPublicaciones()).thenReturn(Arrays.asList(publicacion));

    ModelAndView modelAndView = controladorComunidad.irAComunidad(request);

    assertThat(modelAndView.getViewName(), equalTo("comunidad"));
    assertThat(modelAndView.getModel().get("usuario"), equalTo(usuario));
    assertThat(modelAndView.getModel().get("publicaciones"), notNullValue());
  }

  @Test
  public void irACrearPublicacionSinUsuarioDeberiaRedirigirAlLogin() {
    when(session.getAttribute("usuario")).thenReturn(null);

    ModelAndView modelAndView = controladorComunidad.irACrearPublicacion(request);

    assertThat(modelAndView.getViewName(), equalTo("redirect:/login"));
  }

  @Test
  public void irACrearPublicacionConUsuarioDeberiaMostrarFormulario() {
    Usuario usuario = new Usuario();
    when(session.getAttribute("usuario")).thenReturn(usuario);

    ModelAndView modelAndView = controladorComunidad.irACrearPublicacion(request);

    assertThat(modelAndView.getViewName(), equalTo("crear-publicacion"));
    assertThat(modelAndView.getModel().get("publicacionDTO"), notNullValue());
  }

  @Test
  public void crearPublicacionValidaDeberiaGuardarYRedirigirAComunidad() {
    Usuario usuario = new Usuario();
    when(session.getAttribute("usuario")).thenReturn(usuario);

    PublicacionDTO publicacionDTO = new PublicacionDTO();
    publicacionDTO.setTitulo("Titulo");
    publicacionDTO.setContenido("Contenido");

    ModelAndView modelAndView = controladorComunidad.crearPublicacion(publicacionDTO, request);

    verify(servicioComunidad).crearPublicacion(publicacionDTO, usuario);
    assertThat(modelAndView.getViewName(), equalTo("redirect:/comunidad"));
  }

  @Test
  public void crearPublicacionSinTituloDeberiaVolverAlFormularioConError() {
    Usuario usuario = new Usuario();
    when(session.getAttribute("usuario")).thenReturn(usuario);

    PublicacionDTO publicacionDTO = new PublicacionDTO();
    publicacionDTO.setTitulo("");
    publicacionDTO.setContenido("Contenido");

    ModelAndView modelAndView = controladorComunidad.crearPublicacion(publicacionDTO, request);

    assertThat(modelAndView.getViewName(), equalTo("crear-publicacion"));
    assertThat(modelAndView.getModel().get("error"), equalTo("El título no puede estar vacío"));
  }

  @Test
  public void verPublicacionExistenteDeberiaMostrarDetalle() {
    Usuario usuario = new Usuario();
    Publicacion publicacion = new Publicacion();

    when(session.getAttribute("usuario")).thenReturn(usuario);
    when(servicioComunidad.buscarPublicacionPorId(1)).thenReturn(publicacion);

    ModelAndView modelAndView = controladorComunidad.verPublicacion(1, request);

    assertThat(modelAndView.getViewName(), equalTo("detalle-publicacion"));
    assertThat(modelAndView.getModel().get("publicacion"), equalTo(publicacion));
    assertThat(modelAndView.getModel().get("comentarioDTO"), notNullValue());
  }

  @Test
  public void comentarPublicacionValidaDeberiaGuardarComentarioYRedirigirAlDetalle() {
    Usuario usuario = new Usuario();
    when(session.getAttribute("usuario")).thenReturn(usuario);

    ComentarioDTO comentarioDTO = new ComentarioDTO();
    comentarioDTO.setContenido("Comentario");

    ModelAndView modelAndView = controladorComunidad.comentarPublicacion(1, comentarioDTO, request);

    verify(servicioComunidad).comentarPublicacion(1, comentarioDTO, usuario);
    assertThat(modelAndView.getViewName(), equalTo("redirect:/comunidad/1"));
  }

  @Test
  public void unirseAHabitoGrupalSinUsuarioDeberiaRedirigirAlLogin() {
    when(session.getAttribute("usuario")).thenReturn(null);

    ModelAndView modelAndView = controladorComunidad.unirseAHabitoGrupal(1, request);

    assertThat(modelAndView.getViewName(), equalTo("redirect:/login"));
  }

  @Test
  public void unirseAHabitoGrupalConPublicacionInexistenteDeberiaRedirigirAComunidad() {
    Usuario usuario = new Usuario();
    when(session.getAttribute("usuario")).thenReturn(usuario);
    when(servicioComunidad.buscarPublicacionPorId(1)).thenReturn(null);

    ModelAndView modelAndView = controladorComunidad.unirseAHabitoGrupal(1, request);

    assertThat(modelAndView.getViewName(), equalTo("redirect:/comunidad"));
  }

  @Test
  public void unirseAHabitoGrupalConPublicacionSinHabitoAsociadoDeberiaRedirigirAComunidad() {
    Usuario usuario = new Usuario();
    Publicacion publicacion = new Publicacion();

    when(session.getAttribute("usuario")).thenReturn(usuario);
    when(servicioComunidad.buscarPublicacionPorId(1)).thenReturn(publicacion);

    ModelAndView modelAndView = controladorComunidad.unirseAHabitoGrupal(1, request);

    assertThat(modelAndView.getViewName(), equalTo("redirect:/comunidad"));
  }

  @Test
  public void unirseAHabitoGrupalValidoDeberiaVincularAlUsuarioYRedirigirAlDetalle()
    throws LimiteHabitosAlcanzadoException, UsuarioYaUnidoAHabitoException {
    Usuario usuario = new Usuario();
    Habito habito = new Habito();
    habito.setEsGrupal(true);
    Publicacion publicacion = new Publicacion();
    publicacion.setHabitoAsociado(habito);

    when(session.getAttribute("usuario")).thenReturn(usuario);
    when(servicioComunidad.buscarPublicacionPorId(1)).thenReturn(publicacion);

    ModelAndView modelAndView = controladorComunidad.unirseAHabitoGrupal(1, request);

    verify(servicioHabitoCompartido).unirseAHabitoGrupal(habito, usuario);
    assertThat(modelAndView.getViewName(), equalTo("redirect:/comunidad/1"));
  }

  @Test
  public void unirseAHabitoGrupalCuandoYaEstaUnidoDeberiaVolverAlDetalleConError()
    throws LimiteHabitosAlcanzadoException, UsuarioYaUnidoAHabitoException {
    Usuario usuario = new Usuario();
    Habito habito = new Habito();
    habito.setEsGrupal(true);
    Publicacion publicacion = new Publicacion();
    publicacion.setHabitoAsociado(habito);

    when(session.getAttribute("usuario")).thenReturn(usuario);
    when(servicioComunidad.buscarPublicacionPorId(1)).thenReturn(publicacion);

    org.mockito.Mockito
      .doThrow(new UsuarioYaUnidoAHabitoException())
      .when(servicioHabitoCompartido)
      .unirseAHabitoGrupal(habito, usuario);

    ModelAndView modelAndView = controladorComunidad.unirseAHabitoGrupal(1, request);

    assertThat(modelAndView.getViewName(), equalTo("detalle-publicacion"));
    assertThat(modelAndView.getModel().get("publicacion"), equalTo(publicacion));
    assertThat(
      modelAndView.getModel().get("error"),
      equalTo("Ya estás participando en este hábito grupal")
    );
  }
}
