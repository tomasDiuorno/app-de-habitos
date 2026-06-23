package com.tallerwebi.dominio.servicios;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.entidades.Bonificacion;
import com.tallerwebi.dominio.entidades.Monedero;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioBonificacion;
import com.tallerwebi.dominio.interfaz.RepositorioBonificacion;
import com.tallerwebi.dominio.interfaz.RepositorioMonedero;
import com.tallerwebi.dominio.interfaz.RepositorioUsuarioBonificacion;
import com.tallerwebi.dominio.interfaz.ServicioTienda;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class ServicioTiendaTest {

  private ServicioTienda servicioTienda;
  private RepositorioBonificacion repositorioBonificacion;
  private RepositorioUsuarioBonificacion repositorioUsuarioBonificacion;
  private RepositorioMonedero repositorioMonedero;

  @BeforeEach
  public void init() {
    repositorioBonificacion = mock(RepositorioBonificacion.class);
    repositorioUsuarioBonificacion = mock(RepositorioUsuarioBonificacion.class);
    repositorioMonedero = mock(RepositorioMonedero.class);

    servicioTienda =
      new ServicioTiendaImp(
        repositorioBonificacion,
        repositorioUsuarioBonificacion,
        repositorioMonedero
      );
  }

  @Test
  public void obtenerBonificacionesDisponiblesDeberiaRetornarLasBonificacionesDelRepositorio() {
    Bonificacion bonificacionUno = new Bonificacion();
    bonificacionUno.setNombre("IMPULSO INICIAL");

    Bonificacion bonificacionDos = new Bonificacion();
    bonificacionDos.setNombre("IMPULSO MEDIO");

    List<Bonificacion> bonificaciones = Arrays.asList(bonificacionUno, bonificacionDos);

    when(repositorioBonificacion.buscarDisponibles()).thenReturn(bonificaciones);

    List<Bonificacion> resultado = servicioTienda.obtenerBonificacionesDisponibles();

    assertThat(resultado.size(), is(2));
    assertThat(resultado.get(0).getNombre(), equalTo("IMPULSO INICIAL"));
    assertThat(resultado.get(1).getNombre(), equalTo("IMPULSO MEDIO"));

    verify(repositorioBonificacion).buscarDisponibles();
  }

  @Test
  public void obtenerBonificacionActivaDeberiaRetornarLaBonificacionActivaDelUsuario() {
    Usuario usuario = new Usuario();
    usuario.setId(1);

    UsuarioBonificacion usuarioBonificacion = new UsuarioBonificacion();
    usuarioBonificacion.setActiva(true);

    when(repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(1))
      .thenReturn(usuarioBonificacion);

    UsuarioBonificacion resultado = servicioTienda.obtenerBonificacionActiva(usuario);

    assertThat(resultado, is(usuarioBonificacion));

    verify(repositorioUsuarioBonificacion).buscarActivaPorUsuarioId(1);
  }

  @Test
  public void obtenerBonificacionActivaCuandoNoTieneDeberiaRetornarNull() {
    Usuario usuario = new Usuario();
    usuario.setId(1);

    when(repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(1)).thenReturn(null);

    UsuarioBonificacion resultado = servicioTienda.obtenerBonificacionActiva(usuario);

    assertNull(resultado);

    verify(repositorioUsuarioBonificacion).buscarActivaPorUsuarioId(1);
  }

  @Test
  public void activarBonificacionDeberiaDescontarMonedasRegistrarTransaccionYGuardarBonificacionActiva() {
    Usuario usuario = new Usuario();
    usuario.setId(1);
    usuario.setEmail("usuario@test.com");

    Bonificacion bonificacion = new Bonificacion();
    bonificacion.setId(10);
    bonificacion.setNombre("IMPULSO INICIAL");
    bonificacion.setPrecioMonedas(50);
    bonificacion.setDuracionEnDias(1);
    bonificacion.setDisponible(true);

    Monedero monedero = new Monedero();
    monedero.setSaldo(100);
    monedero.setUsuario(usuario);

    when(repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(1)).thenReturn(null);

    when(repositorioBonificacion.buscarPorId(10)).thenReturn(bonificacion);

    when(repositorioMonedero.buscarPorUsuario(usuario)).thenReturn(monedero);

    servicioTienda.activarBonificacion(usuario, 10);

    assertThat(monedero.getSaldo(), is(50));

    verify(repositorioMonedero).guardar(monedero);

    ArgumentCaptor<Transaccion> captorTransaccion = ArgumentCaptor.forClass(Transaccion.class);

    verify(repositorioMonedero).guardarTransaccion(captorTransaccion.capture());

    Transaccion transaccion = captorTransaccion.getValue();

    assertThat(transaccion.getMonedero(), is(monedero));
    assertThat(transaccion.getMonto(), is(-50));
    assertThat(transaccion.getTipo(), equalTo("BONIFICACION"));
    assertThat(transaccion.getDescripcion(), equalTo("Canje de bonificación: IMPULSO INICIAL"));

    ArgumentCaptor<UsuarioBonificacion> captorUsuarioBonificacion = ArgumentCaptor.forClass(
      UsuarioBonificacion.class
    );

    verify(repositorioUsuarioBonificacion).guardar(captorUsuarioBonificacion.capture());

    UsuarioBonificacion usuarioBonificacion = captorUsuarioBonificacion.getValue();

    assertThat(usuarioBonificacion.getUsuario(), is(usuario));
    assertThat(usuarioBonificacion.getBonificacion(), is(bonificacion));
    assertThat(usuarioBonificacion.getActiva(), is(true));
  }

  @Test
  public void activarBonificacionCuandoYaTieneUnaActivaDeberiaLanzarExcepcion() {
    Usuario usuario = new Usuario();
    usuario.setId(1);

    UsuarioBonificacion activa = new UsuarioBonificacion();
    activa.setActiva(true);

    when(repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(1)).thenReturn(activa);

    try {
      servicioTienda.activarBonificacion(usuario, 10);
      fail("Se esperaba RuntimeException");
    } catch (RuntimeException e) {
      assertThat(e.getMessage(), equalTo("Ya tenés una bonificación activa"));
    }

    verify(repositorioBonificacion, never()).buscarPorId(10);
  }

  @Test
  public void activarBonificacionCuandoNoExisteDeberiaLanzarExcepcion() {
    Usuario usuario = new Usuario();
    usuario.setId(1);

    when(repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(1)).thenReturn(null);

    when(repositorioBonificacion.buscarPorId(10)).thenReturn(null);

    try {
      servicioTienda.activarBonificacion(usuario, 10);
      fail("Se esperaba RuntimeException");
    } catch (RuntimeException e) {
      assertThat(e.getMessage(), equalTo("La bonificación no existe"));
    }

    verify(repositorioMonedero, never()).buscarPorUsuario(usuario);
  }

  @Test
  public void activarBonificacionCuandoNoEstaDisponibleDeberiaLanzarExcepcion() {
    Usuario usuario = new Usuario();
    usuario.setId(1);

    Bonificacion bonificacion = new Bonificacion();
    bonificacion.setId(10);
    bonificacion.setDisponible(false);

    when(repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(1)).thenReturn(null);

    when(repositorioBonificacion.buscarPorId(10)).thenReturn(bonificacion);

    try {
      servicioTienda.activarBonificacion(usuario, 10);
      fail("Se esperaba RuntimeException");
    } catch (RuntimeException e) {
      assertThat(e.getMessage(), equalTo("La bonificación no está disponible"));
    }

    verify(repositorioMonedero, never()).buscarPorUsuario(usuario);
  }

  @Test
  public void activarBonificacionCuandoNoTieneMonederoDeberiaLanzarExcepcion() {
    Usuario usuario = new Usuario();
    usuario.setId(1);

    Bonificacion bonificacion = new Bonificacion();
    bonificacion.setId(10);
    bonificacion.setDisponible(true);

    when(repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(1)).thenReturn(null);

    when(repositorioBonificacion.buscarPorId(10)).thenReturn(bonificacion);

    when(repositorioMonedero.buscarPorUsuario(usuario)).thenReturn(null);

    try {
      servicioTienda.activarBonificacion(usuario, 10);
      fail("Se esperaba RuntimeException");
    } catch (RuntimeException e) {
      assertThat(e.getMessage(), equalTo("El usuario no tiene monedero"));
    }

    verify(repositorioMonedero, never()).guardarTransaccion(any(Transaccion.class));
  }

  @Test
  public void activarBonificacionCuandoNoTieneSaldoSuficienteDeberiaLanzarExcepcion() {
    Usuario usuario = new Usuario();
    usuario.setId(1);

    Bonificacion bonificacion = new Bonificacion();
    bonificacion.setId(10);
    bonificacion.setDisponible(true);
    bonificacion.setPrecioMonedas(130);

    Monedero monedero = new Monedero();
    monedero.setSaldo(50);

    when(repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(1)).thenReturn(null);

    when(repositorioBonificacion.buscarPorId(10)).thenReturn(bonificacion);

    when(repositorioMonedero.buscarPorUsuario(usuario)).thenReturn(monedero);

    try {
      servicioTienda.activarBonificacion(usuario, 10);
      fail("Se esperaba RuntimeException");
    } catch (RuntimeException e) {
      assertThat(e.getMessage(), equalTo("No tenés monedas suficientes"));
    }

    assertThat(monedero.getSaldo(), is(50));

    verify(repositorioMonedero, never()).guardar(monedero);
  }
}
