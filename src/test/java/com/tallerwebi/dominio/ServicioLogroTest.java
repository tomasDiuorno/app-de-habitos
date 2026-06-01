package com.tallerwebi.dominio;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
  public void siElUsuarioTieneUnHabitoDeberiaDesbloquearElPrimerLogro() {
    Usuario usuario = new Usuario();

    List<UsuarioHabito> usuarioHabitos = new ArrayList<>();
    usuarioHabitos.add(new UsuarioHabito());

    usuario.setUsuarioHabito(usuarioHabitos);

    Logro logro = new Logro();
    logro.setNombre("Primer hábito creado");

    when(repositorioLogroMock.buscarPorNombre("Primer hábito creado")).thenReturn(logro);

    when(repositorioUsuarioLogroMock.buscarPorUsuarioYLogro(usuario, logro)).thenReturn(null);

    servicioLogro.verificarLogros(usuario);

    verify(repositorioUsuarioLogroMock, times(1)).guardar(any(UsuarioLogro.class));
  }

  @Test
  public void siElUsuarioYaTieneElLogroNoDeberiaGuardarloOtraVez() {
    Usuario usuario = new Usuario();

    List<UsuarioHabito> usuarioHabitos = new ArrayList<>();
    usuarioHabitos.add(new UsuarioHabito());

    usuario.setUsuarioHabito(usuarioHabitos);

    Logro logro = new Logro();
    logro.setNombre("Primer hábito creado");

    UsuarioLogro usuarioLogroExistente = new UsuarioLogro();
    usuarioLogroExistente.setUsuario(usuario);
    usuarioLogroExistente.setLogro(logro);

    when(repositorioLogroMock.buscarPorNombre("Primer hábito creado")).thenReturn(logro);

    when(repositorioUsuarioLogroMock.buscarPorUsuarioYLogro(usuario, logro))
      .thenReturn(usuarioLogroExistente);

    servicioLogro.verificarLogros(usuario);

    verify(repositorioUsuarioLogroMock, times(0)).guardar(any(UsuarioLogro.class));
  }

  @Test
  public void siElUsuarioTieneTresHabitosDeberiaDesbloquearElLogroTresHabitos() {
    Usuario usuario = new Usuario();

    List<UsuarioHabito> usuarioHabitos = new ArrayList<>();
    usuarioHabitos.add(new UsuarioHabito());
    usuarioHabitos.add(new UsuarioHabito());
    usuarioHabitos.add(new UsuarioHabito());

    usuario.setUsuarioHabito(usuarioHabitos);

    Logro logro = new Logro();
    logro.setNombre("3 hábitos creados");

    when(repositorioLogroMock.buscarPorNombre("3 hábitos creados")).thenReturn(logro);

    when(repositorioUsuarioLogroMock.buscarPorUsuarioYLogro(usuario, logro)).thenReturn(null);

    servicioLogro.verificarLogros(usuario);

    verify(repositorioUsuarioLogroMock, times(2)).guardar(any(UsuarioLogro.class));
  }

  @Test
  public void siElUsuarioTieneTresHabitosDeberiaDesbloquearDosLogros() {
    Usuario usuario = new Usuario();

    List<UsuarioHabito> usuarioHabitos = new ArrayList<>();
    usuarioHabitos.add(new UsuarioHabito());
    usuarioHabitos.add(new UsuarioHabito());
    usuarioHabitos.add(new UsuarioHabito());

    usuario.setUsuarioHabito(usuarioHabitos);

    Logro logroUno = new Logro();
    logroUno.setNombre("Primer hábito creado");

    Logro logroTres = new Logro();
    logroTres.setNombre("3 hábitos creados");

    when(repositorioLogroMock.buscarPorNombre("Primer hábito creado")).thenReturn(logroUno);

    when(repositorioLogroMock.buscarPorNombre("3 hábitos creados")).thenReturn(logroTres);

    when(repositorioUsuarioLogroMock.buscarPorUsuarioYLogro(any(), any())).thenReturn(null);

    servicioLogro.verificarLogros(usuario);

    verify(repositorioUsuarioLogroMock, times(2)).guardar(any(UsuarioLogro.class));
  }
}
