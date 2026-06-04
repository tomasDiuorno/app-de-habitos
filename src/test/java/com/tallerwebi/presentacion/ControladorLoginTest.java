package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.ServicioHabito;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioRecuperacionContrasenia;
import com.tallerwebi.dominio.ServicioRegistro;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

public class ControladorLoginTest {

  private ControladorLogin controladorLogin;
  private ControladorRegistro controladorRegistro;
  private Usuario usuarioMock;
  private DatosLogin datosLoginMock;
  private HttpServletRequest requestMock;
  private HttpSession sessionMock;
  private ServicioLogin servicioLoginMock;
  private ServicioRegistro servicioRegistroMock;
  private ServicioRecuperacionContrasenia servicioRecuperacionContraseniaMock;
  private DatosRegistro datosRegistroMock;
  private ServicioHabito servicioHabitosMock;
  private BindingResult bindingResultMock;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    datosLoginMock = new DatosLogin("juli@unlam.com", "123");
    datosRegistroMock = mock(DatosRegistro.class);
    usuarioMock = mock(Usuario.class);
    when(usuarioMock.getEmail()).thenReturn("juli@unlam.com");
    requestMock = mock(HttpServletRequest.class);
    sessionMock = mock(HttpSession.class);
    servicioLoginMock = mock(ServicioLogin.class);
    servicioRegistroMock = mock(ServicioRegistro.class);
    servicioHabitosMock = mock(ServicioHabito.class);
    servicioRecuperacionContraseniaMock = mock(ServicioRecuperacionContrasenia.class);
    bindingResultMock = mock(BindingResult.class);
    when(bindingResultMock.hasErrors()).thenReturn(false);
    controladorLogin = new ControladorLogin(
        servicioLoginMock,
        servicioRecuperacionContraseniaMock,
        servicioHabitosMock);
    controladorRegistro = new ControladorRegistro(servicioRegistroMock, servicioHabitosMock);
    this.mockMvc = MockMvcBuilders.standaloneSetup(controladorLogin).build();
  }

  @Test
  public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamente() {
    // preparacion
    when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(null);

    // ejecucion
    ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

    // validacion
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
    assertThat(
        modelAndView.getModel().get("error").toString(),
        equalToIgnoringCase("Usuario o clave incorrecta"));
    verify(sessionMock, times(0)).setAttribute("ROL", "ADMIN");
  }

  @Test
  public void loginConUsuarioYPasswordCorrectosDeberiaLLevarAHome() {
    // preparacion
    Usuario usuarioEncontradoMock = mock(Usuario.class);
    when(usuarioEncontradoMock.getRol()).thenReturn("ADMIN");

    when(requestMock.getSession()).thenReturn(sessionMock);
    when(servicioLoginMock.consultarUsuario(anyString(), anyString()))
        .thenReturn(usuarioEncontradoMock);

    // ejecucion
    ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

    // validacion
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
    verify(sessionMock, times(1)).setAttribute("ROL", usuarioEncontradoMock.getRol());
  }

  @Test
  public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin()
      throws UsuarioExistente {
    // ejecucion
    ModelAndView modelAndView = controladorRegistro.registrarme(
        datosRegistroMock,
        bindingResultMock);

    // validacion
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    verify(servicioRegistroMock, times(1)).registrar(datosRegistroMock);
  }

  @Test
  public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError()
      throws UsuarioExistente {
    // preparacion
    doThrow(UsuarioExistente.class).when(servicioRegistroMock).registrar(datosRegistroMock);
    when(servicioHabitosMock.obtenerHabitosIniciales()).thenReturn(new ArrayList<>());

    // ejecucion
    ModelAndView modelAndView = controladorRegistro.registrarme(
        datosRegistroMock,
        bindingResultMock);

    // validacion
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
    assertThat(
        modelAndView.getModel().get("error").toString(),
        equalToIgnoringCase("El usuario ya existe"));
    assertThat(modelAndView.getModel().get("habitos"), instanceOf(List.class));
  }

  @Test
  public void errorEnRegistrarmeDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
    // preparacion
    doThrow(RuntimeException.class).when(servicioRegistroMock).registrar(datosRegistroMock);

    // ejecucion
    ModelAndView modelAndView = controladorRegistro.registrarme(
        datosRegistroMock,
        bindingResultMock);

    // validacion
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
    assertThat(
        modelAndView.getModel().get("error").toString(),
        equalToIgnoringCase("Error al registrar el nuevo usuario"));
  }

  @Test
  public void deberiaDarmeErrorSiElUsuarioDejaCamposVacios() throws Exception {
    DatosRegistro datosRegistro = new DatosRegistro();

    when(bindingResultMock.hasErrors()).thenReturn(true);
    when(servicioHabitosMock.obtenerHabitosIniciales()).thenReturn(new ArrayList<>());

    ModelAndView modelAndView = controladorRegistro.registrarme(datosRegistro, bindingResultMock);

    assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
    assertThat(modelAndView.getModel().containsKey("datosRegistro"), is(true));
    verify(servicioRegistroMock, times(0)).registrar(datosRegistro);
  }

  @Test
  public void irALoginDeberiaRetornarVistaLoginConDatosLogin() {
    // ejecucion
    ModelAndView modelAndView = controladorLogin.irALogin();

    // validacion
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
    assertThat(modelAndView.getModel().get("datosLogin"), instanceOf(DatosLogin.class));
  }

  @Test
  public void nuevoUsuarioDeberiaRetornarVistaNuevoUsuarioConUsuarioVacio() {
    List<Habito> habitos = new ArrayList<>();
    when(servicioHabitosMock.obtenerHabitosIniciales()).thenReturn(habitos);

    // ejecucion
    ModelAndView modelAndView = controladorLogin.nuevoUsuario();

    // validacion
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
    assertThat(modelAndView.getModel().get("datosRegistro"), instanceOf(DatosRegistro.class));
    assertThat(modelAndView.getModel().get("habitos"), instanceOf(List.class));
  }

  @Test
  public void irAHomeDeberiaRetornarVistaHome() {
    // ejecucion
    ModelAndView modelAndView = controladorLogin.irAHome();

    // validacion
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));
  }

  @Test
  public void inicioDeberiaRedirigirALogin() {
    // ejecucion
    ModelAndView modelAndView = controladorLogin.inicio();

    // validacion
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
  }

  @Test
  public void deberiaRedirigirAlLoginCuandoLaContraseniaSeRecuperaCorrectamente() throws Exception {

    doNothing()
        .when(servicioRecuperacionContraseniaMock)
        .recuperarContrasenia(any(DatosRecuperacionContrasenia.class));

    MvcResult result = mockMvc.perform(
        post("/recuperacion-contrasenia")
            .param("email", "test@mail.com")
            .param("contrasenia1", "Password1!")
            .param("contrasenia2", "Password1!"))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    System.out.println(
        result.getModelAndView().getModel().get("error"));

    ModelAndView modelAndView = result.getModelAndView();

    assertThat(
        modelAndView.getViewName(),
        equalToIgnoringCase("redirect:/login"));

    verify(servicioRecuperacionContraseniaMock, times(1))
        .recuperarContrasenia(any(DatosRecuperacionContrasenia.class));
  }
}
