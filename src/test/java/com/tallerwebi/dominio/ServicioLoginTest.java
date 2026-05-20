package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class ServicioLoginTest {

  private ServicioLogin servicioLogin;
  private RepositorioUsuario repositorioUsuarioMock;

  @BeforeEach
  public void init() {
    this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
    this.servicioLogin = new ServicioLoginImpl(this.repositorioUsuarioMock);
  }

  @Test
  public void consultarUsuarioConCredencialesCorrectasDeberiaDevolverUsuario() {
    // preparacion
    String email = "test@test.com";
    String contraseniaPlana = "123456";

    // Simula el hash que devolvería la base de datos
    String contraseniaHasheadaEnBD = BCrypt.hashpw(contraseniaPlana, BCrypt.gensalt());

    Usuario usuarioEnBD = new Usuario();
    usuarioEnBD.setEmail(email);
    usuarioEnBD.setPassword(contraseniaHasheadaEnBD); // El mock devuelve el usuario con la clave ya hasheada

    when(this.repositorioUsuarioMock.buscarPorEmail(email)).thenReturn(usuarioEnBD);

    // ejecucion
    Usuario usuarioObtenido = this.servicioLogin.consultarUsuario(email, contraseniaPlana);

    // validacion
    assertThat(usuarioObtenido, equalTo(usuarioEnBD));
    verify(this.repositorioUsuarioMock, times(1)).buscarPorEmail(email);
  }

  @Test
  public void consultarUsuarioConContraseniaIncorrectaDeberiaDevolverNull() {
    // preparacion
    String email = "test@test.com";
    String contraseniaPlanaReal = "123456";
    String contraseniaIngresadaMal = "claveEquivocada";

    // Prepara el usuario en la BD con su clave real encriptada
    String contraseniaHasheadaEnBD = BCrypt.hashpw(contraseniaPlanaReal, BCrypt.gensalt());

    Usuario usuarioEnBD = new Usuario();
    usuarioEnBD.setEmail(email);
    usuarioEnBD.setPassword(contraseniaHasheadaEnBD);

    when(this.repositorioUsuarioMock.buscarPorEmail(email)).thenReturn(usuarioEnBD);

    // ejecucion (intentamos loguear con la contraseña errónea)
    Usuario usuarioObtenido = this.servicioLogin.consultarUsuario(email, contraseniaIngresadaMal);

    // validacion
    assertNull(usuarioObtenido); // Como la clave está mal, BCrypt falla y debería devolver null
  }
}
