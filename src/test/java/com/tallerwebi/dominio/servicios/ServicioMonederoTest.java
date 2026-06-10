package com.tallerwebi.dominio.servicios;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.entidades.Monedero;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.RepositorioMonedero;
import com.tallerwebi.dominio.interfaz.ServicioMonedero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class ServicioMonederoTest {

  private ServicioMonedero servicioMonedero;
  private RepositorioMonedero repositorioMonederoMock;

  @BeforeEach
  public void init() {
    repositorioMonederoMock = mock(RepositorioMonedero.class);
    servicioMonedero = new ServicioMonederoImpl(repositorioMonederoMock);
  }

  @Test
  public void deberiaCrearUnMonederoConSaldoCeroAlInicializar() {
    Usuario usuario = dadoQueTengoUnUsuario();
    when(repositorioMonederoMock.buscarPorUsuario(usuario)).thenReturn(null);

    servicioMonedero.inicializarMonedero(usuario);

    ArgumentCaptor<Monedero> captor = ArgumentCaptor.forClass(Monedero.class);
    verify(repositorioMonederoMock, times(1)).guardar(captor.capture());
    assertThat(captor.getValue().getSaldo(), is(equalTo(0)));
    assertThat(captor.getValue().getUsuario(), is(usuario));
  }

  @Test
  public void noDeberiaCrearUnMonederoSiElUsuarioYaTieneUno() {
    Usuario usuario = dadoQueTengoUnUsuario();
    Monedero monederoExistente = new Monedero();
    monederoExistente.setUsuario(usuario);
    when(repositorioMonederoMock.buscarPorUsuario(usuario)).thenReturn(monederoExistente);

    servicioMonedero.inicializarMonedero(usuario);

    verify(repositorioMonederoMock, never()).guardar(any(Monedero.class));
  }

  @Test
  public void deberiaAcreditar20MonedasAlMonederoAlObtenerUnLogro() {
    Usuario usuario = dadoQueTengoUnUsuario();
    Monedero monedero = dadoQueTengoUnMonederoConSaldo(usuario, 0);
    when(repositorioMonederoMock.buscarPorUsuario(usuario)).thenReturn(monedero);

    servicioMonedero.acreditarPorLogro(usuario);

    assertThat(monedero.getSaldo(), is(equalTo(20)));
    verify(repositorioMonederoMock, times(1)).guardar(monedero);
    verify(repositorioMonederoMock, times(1)).guardarTransaccion(any(Transaccion.class));
  }

  @Test
  public void deberiaAcreditar10MonedasAlMonederoAlObtenerUnaRecompensa() {
    Usuario usuario = dadoQueTengoUnUsuario();
    Monedero monedero = dadoQueTengoUnMonederoConSaldo(usuario, 0);
    when(repositorioMonederoMock.buscarPorUsuario(usuario)).thenReturn(monedero);

    servicioMonedero.acreditarPorRecompensa(usuario);

    assertThat(monedero.getSaldo(), is(equalTo(10)));
    verify(repositorioMonederoMock, times(1)).guardar(monedero);
    verify(repositorioMonederoMock, times(1)).guardarTransaccion(any(Transaccion.class));
  }

  @Test
  public void elSaldoDeberiaAcumularseCorrectamenteEnVariasOperaciones() {
    Usuario usuario = dadoQueTengoUnUsuario();
    Monedero monedero = dadoQueTengoUnMonederoConSaldo(usuario, 0);
    when(repositorioMonederoMock.buscarPorUsuario(usuario)).thenReturn(monedero);

    servicioMonedero.acreditarPorLogro(usuario); // +20 → 20
    servicioMonedero.acreditarPorRecompensa(usuario); // +10 → 30
    servicioMonedero.acreditarPorLogro(usuario); // +20 → 50

    assertThat(monedero.getSaldo(), is(equalTo(50)));
  }

  @Test
  public void noDeberiaAcreditarMontosNegativos() {
    Usuario usuario = dadoQueTengoUnUsuario();
    Monedero monedero = dadoQueTengoUnMonederoConSaldo(usuario, 100);
    when(repositorioMonederoMock.buscarPorUsuario(usuario)).thenReturn(monedero);

    servicioMonedero.acreditarMonedas(usuario, -50, "LOGRO", "Monto invalido");

    assertThat(monedero.getSaldo(), is(equalTo(100)));
    verify(repositorioMonederoMock, never()).guardar(any(Monedero.class));
    verify(repositorioMonederoMock, never()).guardarTransaccion(any(Transaccion.class));
  }

  @Test
  public void deberiaObtenerElSaldoActualDelUsuario() {
    Usuario usuario = dadoQueTengoUnUsuario();
    Monedero monedero = dadoQueTengoUnMonederoConSaldo(usuario, 75);
    when(repositorioMonederoMock.buscarPorUsuario(usuario)).thenReturn(monedero);

    Integer saldo = servicioMonedero.obtenerSaldo(usuario);

    assertThat(saldo, is(equalTo(75)));
  }

  private Usuario dadoQueTengoUnUsuario() {
    Usuario usuario = new Usuario();
    usuario.setEmail("test@mail.com");
    return usuario;
  }

  private Monedero dadoQueTengoUnMonederoConSaldo(Usuario usuario, Integer saldo) {
    Monedero monedero = new Monedero();
    monedero.setUsuario(usuario);
    monedero.setSaldo(saldo);
    return monedero;
  }
}
