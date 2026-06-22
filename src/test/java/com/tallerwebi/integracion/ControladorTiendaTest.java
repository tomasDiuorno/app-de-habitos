package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.entidades.Bonificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioBonificacion;
import com.tallerwebi.dominio.interfaz.ServicioMonedero;
import com.tallerwebi.dominio.interfaz.ServicioTienda;
import com.tallerwebi.presentacion.ControladorTienda;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorTiendaTest {

  private ServicioTienda servicioTienda;
  private ServicioMonedero servicioMonedero;
  private ControladorTienda controladorTienda;
  private HttpServletRequest request;
  private HttpSession session;

  @BeforeEach
  public void init() {
    servicioTienda = mock(ServicioTienda.class);
    servicioMonedero = mock(ServicioMonedero.class);

    controladorTienda = new ControladorTienda(servicioTienda, servicioMonedero);

    request = mock(HttpServletRequest.class);
    session = mock(HttpSession.class);

    when(request.getSession()).thenReturn(session);
  }

  @Test
  public void irATiendaCuandoNoHayUsuarioEnSesionDeberiaRedirigirALogin() {
    when(session.getAttribute("usuario")).thenReturn(null);

    ModelAndView modelAndView = controladorTienda.irATienda(request);

    assertThat(modelAndView.getViewName(), equalTo("redirect:/login"));
  }

  @Test
  public void irATiendaCuandoHayUsuarioEnSesionDeberiaMostrarLaVistaTienda() {
    Usuario usuario = new Usuario();
    usuario.setId(1);
    usuario.setEmail("usuario@test.com");

    Bonificacion bonificacion = new Bonificacion();
    bonificacion.setNombre("IMPULSO INICIAL");

    List<Bonificacion> bonificaciones = Arrays.asList(bonificacion);

    when(session.getAttribute("usuario")).thenReturn(usuario);
    when(servicioTienda.obtenerBonificacionesDisponibles()).thenReturn(bonificaciones);
    when(servicioTienda.obtenerBonificacionActiva(usuario)).thenReturn(null);
    when(servicioMonedero.obtenerSaldo(usuario)).thenReturn(100);

    ModelAndView modelAndView = controladorTienda.irATienda(request);

    assertThat(modelAndView.getViewName(), equalTo("tienda"));
    assertThat(modelAndView.getModel().get("bonificaciones"), is((Object) bonificaciones));
    assertThat(modelAndView.getModel().get("bonificacionActiva"), is((Object) null));
    assertThat(modelAndView.getModel().get("monedasUsuario"), is((Object) 100));

    verify(servicioTienda).obtenerBonificacionesDisponibles();
    verify(servicioTienda).obtenerBonificacionActiva(usuario);
    verify(servicioMonedero).obtenerSaldo(usuario);
  }

  @Test
  public void irATiendaCuandoUsuarioTieneBonificacionActivaDeberiaAgregarlaAlModelo() {
    Usuario usuario = new Usuario();
    usuario.setId(1);
    usuario.setEmail("usuario@test.com");

    UsuarioBonificacion bonificacionActiva = new UsuarioBonificacion();
    bonificacionActiva.setActiva(true);

    when(session.getAttribute("usuario")).thenReturn(usuario);
    when(servicioTienda.obtenerBonificacionesDisponibles()).thenReturn(Arrays.asList());
    when(servicioTienda.obtenerBonificacionActiva(usuario)).thenReturn(bonificacionActiva);
    when(servicioMonedero.obtenerSaldo(usuario)).thenReturn(80);

    ModelAndView modelAndView = controladorTienda.irATienda(request);

    assertThat(modelAndView.getViewName(), equalTo("tienda"));
    assertThat(modelAndView.getModel().get("bonificacionActiva"), is((Object) bonificacionActiva));
    assertThat(modelAndView.getModel().get("monedasUsuario"), is((Object) 80));

    verify(servicioTienda).obtenerBonificacionesDisponibles();
    verify(servicioTienda).obtenerBonificacionActiva(usuario);
    verify(servicioMonedero).obtenerSaldo(usuario);
  }

  @Test
  public void activarBonificacionCuandoNoHayUsuarioEnSesionDeberiaRedirigirALogin() {
    when(session.getAttribute("usuario")).thenReturn(null);

    ModelAndView modelAndView = controladorTienda.activarBonificacion(1, request);

    assertThat(modelAndView.getViewName(), equalTo("redirect:/login"));
  }

  @Test
  public void activarBonificacionCuandoHayUsuarioDeberiaActivarYRedirigirATienda() {
    Usuario usuario = new Usuario();
    usuario.setId(1);
    usuario.setEmail("usuario@test.com");

    when(session.getAttribute("usuario")).thenReturn(usuario);

    ModelAndView modelAndView = controladorTienda.activarBonificacion(10, request);

    verify(servicioTienda).activarBonificacion(usuario, 10);

    assertThat(modelAndView.getViewName(), equalTo("redirect:/tienda?activada=true"));
  }

  @Test
  public void activarBonificacionCuandoElServicioLanzaErrorDeberiaRedirigirATiendaConError() {
    Usuario usuario = new Usuario();
    usuario.setId(1);
    usuario.setEmail("usuario@test.com");

    when(session.getAttribute("usuario")).thenReturn(usuario);

    doThrow(new RuntimeException("Error")).when(servicioTienda).activarBonificacion(usuario, 10);

    ModelAndView modelAndView = controladorTienda.activarBonificacion(10, request);

    assertThat(modelAndView.getViewName(), equalTo("redirect:/tienda?error=true"));
  }
}
