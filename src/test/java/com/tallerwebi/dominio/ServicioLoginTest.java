package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.ServicioLogin;
import com.tallerwebi.dominio.servicios.ServicioLoginImpl;

public class ServicioLoginTest {

  private ServicioLogin servicioLogin;
  private RepositorioUsuario repositorioUsuarioMock;

  @BeforeEach
  public void init() {
    this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
    this.servicioLogin = new ServicioLoginImpl(this.repositorioUsuarioMock);
  }

  @Test
  public void consultarUsuarioDeberiaLlamarAlRepositorioYValidarHash() {
    // preparacion
    String email = "test@test.com";
    String passwordPlana = "password";
    String hashBD = BCrypt.hashpw(passwordPlana, BCrypt.gensalt());

    Usuario usuarioEsperado = new Usuario();
    usuarioEsperado.setEmail(email);
    usuarioEsperado.setPassword(hashBD);

    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(email)).thenReturn(usuarioEsperado);

    // ejecucion
    Usuario usuarioObtenido = this.servicioLogin.consultarUsuario(email, passwordPlana);

    // validacion
    assertThat(usuarioObtenido, equalTo(usuarioEsperado));
    verify(this.repositorioUsuarioMock, times(1)).buscarPorEmailOrUsername(email);
  }

  @Test
  public void quieroQueElUsuarioIngreseConUsernameYPasswordCorrecta() {
    // preparacion
    String username = "test1";
    String passwordPlana = "password";
    String hashBD = BCrypt.hashpw(passwordPlana, BCrypt.gensalt());

    Usuario usuarioEsperado = new Usuario();
    usuarioEsperado.setUsername(username);
    usuarioEsperado.setPassword(hashBD);

    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(username))
      .thenReturn(usuarioEsperado);

    // ejecucion
    Usuario usuarioObtenido = this.servicioLogin.consultarUsuario(username, passwordPlana);

    // validacion
    assertThat(usuarioObtenido, equalTo(usuarioEsperado));
    verify(this.repositorioUsuarioMock, times(1)).buscarPorEmailOrUsername(username);
  }

  @Test
  public void consultarUsuarioConPasswordIncorrectaDeberiaRetornarNull() {
    // preparacion
    String username = "test1";
    String passwordPlana = "password";
    String passwordIncorrecta = "clavemala";
    String hashBD = BCrypt.hashpw(passwordPlana, BCrypt.gensalt());

    Usuario usuarioEsperado = new Usuario();
    usuarioEsperado.setUsername(username);
    usuarioEsperado.setPassword(hashBD);

    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(username))
      .thenReturn(usuarioEsperado);

    // ejecucion
    Usuario usuarioObtenido = this.servicioLogin.consultarUsuario(username, passwordIncorrecta);

    // validacion
    assertNull(usuarioObtenido);
    verify(this.repositorioUsuarioMock, times(1)).buscarPorEmailOrUsername(username);
  }
}
