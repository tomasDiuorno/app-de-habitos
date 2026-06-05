package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.tallerwebi.dominio.entidades.Logro;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioHabito;
import com.tallerwebi.dominio.entidades.UsuarioLogro;
import com.tallerwebi.dominio.interfaz.RepositorioLogro;
import com.tallerwebi.dominio.interfaz.RepositorioUsuarioLogro;
import com.tallerwebi.dominio.interfaz.ServicioLogro;
import com.tallerwebi.dominio.servicios.ServicioLogroImpl;

public class ServicioLogroTest {

  private ServicioLogro servicioLogro;
  private RepositorioLogro repositorioLogroMock;
  private RepositorioUsuarioLogro repositorioUsuarioLogroMock;

  @BeforeEach
  public void init() {
    repositorioLogroMock = mock(RepositorioLogro.class);
    repositorioUsuarioLogroMock = mock(RepositorioUsuarioLogro.class);
    servicioLogro = new ServicioLogroImpl(repositorioLogroMock, repositorioUsuarioLogroMock);
  }

  @Test
  public void deberiaAsignarElLogroPrimerPasoAlUsuarioCuandoTieneUnHabito() {
    Usuario usuario = dadoQueTengoUnUsuarioConHabitos(1);
    Logro logroPrimerPaso = dadoQueTengoUnLogro("Primer Paso", "PRIMER_HABITO");

    when(repositorioLogroMock.buscarPorNombre("Primer Paso")).thenReturn(logroPrimerPaso);
    when(repositorioUsuarioLogroMock.existeLogroParaUsuario(usuario, logroPrimerPaso))
      .thenReturn(false);

    servicioLogro.verificarYAsignarLogros(usuario);

    ArgumentCaptor<UsuarioLogro> captor = ArgumentCaptor.forClass(UsuarioLogro.class);
    verify(repositorioUsuarioLogroMock, times(1)).guardar(captor.capture());

    UsuarioLogro usuarioLogroGuardado = captor.getValue();
    assertThat(usuarioLogroGuardado.getUsuario(), is(usuario));
    assertThat(usuarioLogroGuardado.getLogro().getNombre(), is("Primer Paso"));
  }

  @Test
  public void noDeberiaAsignarElLogroPrimerPasoSiElUsuarioYaLoTiene() {
    Usuario usuario = dadoQueTengoUnUsuarioConHabitos(1);
    Logro logroPrimerPaso = dadoQueTengoUnLogro("Primer Paso", "PRIMER_HABITO");

    when(repositorioLogroMock.buscarPorNombre("Primer Paso")).thenReturn(logroPrimerPaso);
    when(repositorioUsuarioLogroMock.existeLogroParaUsuario(usuario, logroPrimerPaso))
      .thenReturn(true);

    servicioLogro.verificarYAsignarLogros(usuario);

    verify(repositorioUsuarioLogroMock, never()).guardar(any(UsuarioLogro.class));
  }

  @Test
  public void noDeberiaAsignarElLogroPrimerPasoSiElUsuarioNoTieneHabitos() {
    Usuario usuario = dadoQueTengoUnUsuarioConHabitos(0);
    Logro logroPrimerPaso = dadoQueTengoUnLogro("Primer Paso", "PRIMER_HABITO");

    when(repositorioLogroMock.buscarPorNombre("Primer Paso")).thenReturn(logroPrimerPaso);

    servicioLogro.verificarYAsignarLogros(usuario);

    verify(repositorioUsuarioLogroMock, never()).guardar(any(UsuarioLogro.class));
  }

  @Test
  public void deberiaObtenerLosLogrosDeUnUsuario() {
    Usuario usuario = dadoQueTengoUnUsuarioConHabitos(1);
    List<UsuarioLogro> logrosEsperados = new ArrayList<>();
    logrosEsperados.add(new UsuarioLogro());

    when(repositorioUsuarioLogroMock.buscarPorUsuario(usuario)).thenReturn(logrosEsperados);

    List<UsuarioLogro> logrosObtenidos = servicioLogro.obtenerLogrosDeUsuario(usuario);

    assertThat(logrosObtenidos.size(), equalTo(1));
    verify(repositorioUsuarioLogroMock, times(1)).buscarPorUsuario(usuario);
  }

  @Test
  public void deberiaAsignarElLogroConstanteCuandoUsuarioTieneTresHabitos() {
    Usuario usuario = dadoQueTengoUnUsuarioConHabitos(3);

    Logro logroPrimerPaso = dadoQueTengoUnLogro("Primer Paso", "PRIMER_HABITO");
    Logro logroConstante = dadoQueTengoUnLogro("Constante", "TRES_HABITOS");

    when(repositorioLogroMock.buscarPorNombre("Primer Paso")).thenReturn(logroPrimerPaso);
    when(repositorioLogroMock.buscarPorNombre("Constante")).thenReturn(logroConstante);

    when(repositorioUsuarioLogroMock.existeLogroParaUsuario(usuario, logroPrimerPaso))
      .thenReturn(true);
    when(repositorioUsuarioLogroMock.existeLogroParaUsuario(usuario, logroConstante))
      .thenReturn(false);

    servicioLogro.verificarYAsignarLogros(usuario);

    ArgumentCaptor<UsuarioLogro> captor = ArgumentCaptor.forClass(UsuarioLogro.class);
    verify(repositorioUsuarioLogroMock, times(1)).guardar(captor.capture());

    UsuarioLogro usuarioLogroGuardado = captor.getValue();
    assertThat(usuarioLogroGuardado.getUsuario(), is(usuario));
    assertThat(usuarioLogroGuardado.getLogro().getNombre(), is("Constante"));
  }

  @Test
  public void deberiaAsignarElLogroExpertoCuandoUsuarioTieneCuatroHabitos() {
    Usuario usuario = dadoQueTengoUnUsuarioConHabitos(4);

    Logro logroPrimerPaso = dadoQueTengoUnLogro("Primer Paso", "PRIMER_HABITO");
    Logro logroConstante = dadoQueTengoUnLogro("Constante", "TRES_HABITOS");
    Logro logroExperto = dadoQueTengoUnLogro("Experto", "CUATRO_HABITOS");

    when(repositorioLogroMock.buscarPorNombre("Primer Paso")).thenReturn(logroPrimerPaso);
    when(repositorioLogroMock.buscarPorNombre("Constante")).thenReturn(logroConstante);
    when(repositorioLogroMock.buscarPorNombre("Experto")).thenReturn(logroExperto);

    when(repositorioUsuarioLogroMock.existeLogroParaUsuario(usuario, logroPrimerPaso))
      .thenReturn(true);
    when(repositorioUsuarioLogroMock.existeLogroParaUsuario(usuario, logroConstante))
      .thenReturn(true);
    when(repositorioUsuarioLogroMock.existeLogroParaUsuario(usuario, logroExperto))
      .thenReturn(false);

    servicioLogro.verificarYAsignarLogros(usuario);

    ArgumentCaptor<UsuarioLogro> captor = ArgumentCaptor.forClass(UsuarioLogro.class);
    verify(repositorioUsuarioLogroMock, times(1)).guardar(captor.capture());

    UsuarioLogro usuarioLogroGuardado = captor.getValue();
    assertThat(usuarioLogroGuardado.getUsuario(), is(usuario));
    assertThat(usuarioLogroGuardado.getLogro().getNombre(), is("Experto"));
  }

  private Usuario dadoQueTengoUnUsuarioConHabitos(int cantidadHabitos) {
    Usuario usuario = new Usuario();
    usuario.setEmail("test@mail.com");

    List<UsuarioHabito> habitos = new ArrayList<>();
    for (int i = 0; i < cantidadHabitos; i++) {
      habitos.add(new UsuarioHabito());
    }
    usuario.setUsuarioHabito(habitos);
    return usuario;
  }

  private Logro dadoQueTengoUnLogro(String nombre, String condicion) {
    Logro logro = new Logro();
    logro.setNombre(nombre);
    logro.setCondicion(condicion);
    return logro;
  }
}
