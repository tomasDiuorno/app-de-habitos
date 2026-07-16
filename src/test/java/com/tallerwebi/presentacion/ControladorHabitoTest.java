package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioHabito;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.dominio.interfaz.ServicioCategoria;
import com.tallerwebi.dominio.interfaz.ServicioEvaluadorHabito;
import com.tallerwebi.dominio.interfaz.ServicioHabito;
import com.tallerwebi.dominio.interfaz.ServicioLogro;
import com.tallerwebi.dominio.interfaz.ServicioUsuarioHabito;
import com.tallerwebi.dominio.servicios.ServicioHabitoCompartido;
import com.tallerwebi.dominio.servicios.ServicioHabitoIA;
import com.tallerwebi.presentacion.DTO.EvidenciaDTO;
import com.tallerwebi.presentacion.DTO.PlanHabitoDTO;
import com.tallerwebi.presentacion.DTO.RegistroHabitoDTO;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ControladorHabitoTest {

  private ServicioHabito servicioHabitosMock;
  private ServicioEvaluadorHabito servicioEvaluadorHabitoMock;
  private ServicioUsuarioHabito servicioUsuarioHabitoMock;
  private ControladorHabitos controladorHabitos;
  private HttpServletRequest requestMock;
  private HttpSession sessionMock;
  private ServicioCategoria servicioCategoriaMock;
  private ServicioLogro servicioLogroMock;
  private ServicioHabitoIA servicioHabitoIAMock;
  private ServicioHabitoCompartido servicioHabitoCompartidoMock;

  @BeforeEach
  public void init() {
    servicioEvaluadorHabitoMock = mock(ServicioEvaluadorHabito.class);
    servicioUsuarioHabitoMock = mock(ServicioUsuarioHabito.class);
    servicioHabitosMock = mock(ServicioHabito.class);
    requestMock = mock(HttpServletRequest.class);
    sessionMock = mock(HttpSession.class);
    servicioCategoriaMock = mock(ServicioCategoria.class);
    servicioLogroMock = mock(ServicioLogro.class);
    servicioHabitoIAMock = mock(ServicioHabitoIA.class);
    servicioHabitoCompartidoMock = mock(ServicioHabitoCompartido.class);
    controladorHabitos =
      new ControladorHabitos(
        servicioHabitosMock,
        servicioCategoriaMock,
        servicioLogroMock,
        servicioEvaluadorHabitoMock,
        servicioUsuarioHabitoMock,
        servicioHabitoIAMock,
        servicioHabitoCompartidoMock
      );
  }

  @Test
  public void completarHabitoDeberiaEvaluarHabitoYRedirigirAHabitos() {
    // preparación
    Integer habitoId = 1;
    Usuario usuarioMock = mock(Usuario.class);
    Habito habitoMock = mock(Habito.class);
    EvidenciaDTO evidencia = mock(EvidenciaDTO.class);
    UsuarioHabito usuarioHabitoMock = mock(UsuarioHabito.class);
    RedirectAttributes redirectAttributesMock = mock(RedirectAttributes.class);
    ResultadoEvaluacionDTO resultado = new ResultadoEvaluacionDTO(
      true,
      "Hábito completado correctamente."
    );
    when(requestMock.getSession()).thenReturn(sessionMock);
    when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
    when(servicioHabitosMock.buscarHabitoPorId(habitoId)).thenReturn(habitoMock);
    when(servicioUsuarioHabitoMock.obtenerPorUsuarioYHabito(usuarioMock, habitoMock))
      .thenReturn(usuarioHabitoMock);
    when(servicioEvaluadorHabitoMock.completarHabito(usuarioHabitoMock, evidencia))
      .thenReturn(resultado);

    // ejecución
    ModelAndView modelAndView = controladorHabitos.completarHabito(
      habitoId,
      evidencia,
      requestMock,
      redirectAttributesMock
    );

    // validación
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/habitos"));
    verify(servicioHabitosMock).buscarHabitoPorId(habitoId);
    verify(servicioUsuarioHabitoMock).obtenerPorUsuarioYHabito(usuarioMock, habitoMock);
    verify(servicioEvaluadorHabitoMock).completarHabito(usuarioHabitoMock, evidencia);
    verify(redirectAttributesMock).addFlashAttribute("resultadoEvaluacion", resultado);
  }

  @Test
  public void irAPlanHabitoDeberiaRedirigirALoginSiUsuarioNoEstaLogueado() {
    when(requestMock.getSession()).thenReturn(sessionMock);
    when(sessionMock.getAttribute("usuario")).thenReturn(null);

    ModelAndView modelAndView = controladorHabitos.irAPlanHabito(1, requestMock);

    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
  }

  @Test
  public void irAPlanHabitoDeberiaRetornarVistaPlanHabitoConPasos() throws Exception {
    Integer habitoId = 1;
    Usuario usuarioMock = mock(Usuario.class);
    Habito habitoMock = mock(Habito.class);
    PlanHabitoDTO planMock = new PlanHabitoDTO();
    planMock.setPasos(Arrays.asList("Paso 1", "Paso 2"));

    when(requestMock.getSession()).thenReturn(sessionMock);
    when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
    when(servicioHabitosMock.buscarHabitoPorId(habitoId)).thenReturn(habitoMock);
    when(habitoMock.getTitulo()).thenReturn("Hacer ejercicio");
    when(servicioHabitoIAMock.sugerirPlan("Hacer ejercicio")).thenReturn(planMock);

    ModelAndView modelAndView = controladorHabitos.irAPlanHabito(habitoId, requestMock);

    assertThat(modelAndView.getViewName(), equalToIgnoringCase("plan-habito"));
    assertThat(modelAndView.getModel().get("habito"), equalTo(habitoMock));
    assertThat(modelAndView.getModel().get("plan"), equalTo(planMock));
  }

  @Test
  public void irAPlanHabitoDeberiaManejarErrorDeIAYMostrarMensaje() throws Exception {
    Integer habitoId = 1;
    Usuario usuarioMock = mock(Usuario.class);
    Habito habitoMock = mock(Habito.class);

    when(requestMock.getSession()).thenReturn(sessionMock);
    when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
    when(servicioHabitosMock.buscarHabitoPorId(habitoId)).thenReturn(habitoMock);
    when(habitoMock.getTitulo()).thenReturn("Hacer ejercicio");
    when(servicioHabitoIAMock.sugerirPlan("Hacer ejercicio"))
      .thenThrow(new RuntimeException("IA Error"));

    ModelAndView modelAndView = controladorHabitos.irAPlanHabito(habitoId, requestMock);

    assertThat(modelAndView.getViewName(), equalToIgnoringCase("plan-habito"));
    assertThat(modelAndView.getModel().get("habito"), equalTo(habitoMock));
    assertThat(
      modelAndView.getModel().get("error"),
      equalTo("Hubo un problema al generar los pasos. Intentá de nuevo más tarde.")
    );
  }

  @Test
  public void irAPlanHabitoDeberiaRetornarErrorSiHabitoNoExiste() throws Exception {
    Integer habitoId = 99;
    Usuario usuarioMock = mock(Usuario.class);

    when(requestMock.getSession()).thenReturn(sessionMock);
    when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
    when(servicioHabitosMock.buscarHabitoPorId(habitoId)).thenReturn(null);

    ModelAndView modelAndView = controladorHabitos.irAPlanHabito(habitoId, requestMock);

    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/habitos"));
  }

  @Test
  public void crearHabitoConCompartirEnForoActivadoDeberiaCrearHabitoGrupalYRedirigirAHabitos()
    throws Exception {
    Usuario usuario = new Usuario();
    usuario.setId(1);
    usuario.setUsuarioHabitos(
      new ArrayList<>(java.util.Collections.singletonList(new UsuarioHabito()))
    );

    RegistroHabitoDTO datos = new RegistroHabitoDTO();
    datos.setTitulo("Meditar en grupo");
    datos.setCompartirEnForo(true);

    Habito habitoGrupal = new Habito();
    habitoGrupal.setTitulo("Meditar en grupo");
    habitoGrupal.setEsGrupal(true);

    when(requestMock.getSession()).thenReturn(sessionMock);
    when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
    when(servicioHabitoCompartidoMock.crearHabitoGrupal(datos, usuario)).thenReturn(habitoGrupal);

    ModelAndView modelAndView = controladorHabitos.crearHabito(datos, requestMock);

    verify(servicioHabitoCompartidoMock).crearHabitoGrupal(datos, usuario);
    verify(servicioHabitosMock, org.mockito.Mockito.never())
      .agregarHabitoParaUsuario(any(Habito.class), any(Usuario.class));
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/habitos"));
  }

  @Test
  public void crearHabitoConCompartirEnForoActivadoYLimiteAlcanzadoDeberiaMostrarError()
    throws Exception {
    Usuario usuario = new Usuario();
    usuario.setId(1);

    RegistroHabitoDTO datos = new RegistroHabitoDTO();
    datos.setTitulo("Meditar en grupo");
    datos.setCompartirEnForo(true);

    when(requestMock.getSession()).thenReturn(sessionMock);
    when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
    when(servicioHabitoCompartidoMock.crearHabitoGrupal(datos, usuario))
      .thenThrow(new LimiteHabitosAlcanzadoException());

    ModelAndView modelAndView = controladorHabitos.crearHabito(datos, requestMock);

    assertThat(modelAndView.getViewName(), equalToIgnoringCase("crear-habito"));
    assertThat(
      modelAndView.getModel().get("error"),
      equalTo("No podés tener más de 4 hábitos activos")
    );
  }

  @Test
  public void crearHabitoSinCompartirEnForoDeberiaUsarElFlujoNormal() throws Exception {
    Usuario usuario = new Usuario();
    usuario.setId(1);
    usuario.setUsuarioHabitos(new ArrayList<>(Collections.singletonList(new UsuarioHabito())));

    RegistroHabitoDTO datos = new RegistroHabitoDTO();
    datos.setTitulo("Dormir temprano");
    datos.setCompartirEnForo(false);

    Habito habito = new Habito();
    habito.setTitulo("Dormir temprano");

    when(requestMock.getSession()).thenReturn(sessionMock);
    when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
    when(servicioHabitosMock.obtenerHabito(datos)).thenReturn(habito);

    ModelAndView modelAndView = controladorHabitos.crearHabito(datos, requestMock);

    verify(servicioHabitosMock).agregarHabitoParaUsuario(habito, usuario);
    verify(servicioHabitoCompartidoMock, org.mockito.Mockito.never())
      .crearHabitoGrupal(any(RegistroHabitoDTO.class), any(Usuario.class));
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/habitos"));
  }
}
