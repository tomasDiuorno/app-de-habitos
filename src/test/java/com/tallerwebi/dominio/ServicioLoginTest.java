package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioLoginTest {

  private ServicioLogin servicioLogin;
  private RepositorioUsuario repositorioUsuarioMock;

  @BeforeEach
  public void init() {
    this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
    this.servicioLogin = new ServicioLoginImpl(this.repositorioUsuarioMock);
  }

  @Test
  public void consultarUsuarioDeberiaLlamarAlRepositorio() {
    // preparacion
    String email = "test@test.com";
    String password = "password";
    Usuario usuarioEsperado = new Usuario();
    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(email, password))
      .thenReturn(usuarioEsperado);

    // ejecucion
    Usuario usuarioObtenido = this.servicioLogin.consultarUsuario(email, password);

    // validacion
    assertThat(usuarioObtenido, equalTo(usuarioEsperado));
    verify(this.repositorioUsuarioMock, times(1)).buscarPorEmailOrUsername(email, password);
  }

  @Test
  public void quieroQueElUsuarioIngreseConUsernameYPassword() {
    String username = "test1";
    String password = "password";

    Usuario usuarioEsperado = new Usuario();
    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(username, password))
      .thenReturn(usuarioEsperado);

    // ejecucion
    Usuario usuarioObtenido = this.servicioLogin.consultarUsuario(username, password);

    // validacion
    assertThat(usuarioObtenido, equalTo(usuarioEsperado));
    verify(this.repositorioUsuarioMock, times(1)).buscarPorEmailOrUsername(username, password);
  }
}
