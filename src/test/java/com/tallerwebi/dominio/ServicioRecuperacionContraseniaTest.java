package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.excepcion.ContraseniasNoCoincidenException;
import com.tallerwebi.dominio.excepcion.EmailInexistenteException;
import com.tallerwebi.presentacion.DatosRecuperacionContrasenia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioRecuperacionContraseniaTest {

  private ServicioRecuperacionContrasenia servicioRecuperacion;
  private RepositorioUsuario repositorioUsuarioMock;

  @BeforeEach
  public void init() {
    this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
    this.servicioRecuperacion = new ServicioRecuperacionContraseniaImp(this.repositorioUsuarioMock);
  }

  @Test
  public void quieroRecuperarLaContraseniaExitosamente()
    throws ContraseniasNoCoincidenException, EmailInexistenteException {
    String emailValido = "test@gmail.com";
    String contraseniaNueva1 = "4321";
    String contraseniaNueva2 = "4321";

    DatosRecuperacionContrasenia datos = new DatosRecuperacionContrasenia(
      emailValido,
      contraseniaNueva1,
      contraseniaNueva2
    );

    Usuario usuarioSimulado = new Usuario();
    usuarioSimulado.setEmail(emailValido);

    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(emailValido))
      .thenReturn(usuarioSimulado);

    this.servicioRecuperacion.recuperarContrasenia(datos);

    String contraseniaCambiada = datos.getContrasenia1();

    assertEquals("4321", contraseniaCambiada);
  }

  @Test
  public void SiElEmailNoExisteDebeLanzarException() {
    String emailInexistente = "noexiste@gmail.com";
    DatosRecuperacionContrasenia datos = new DatosRecuperacionContrasenia(
      emailInexistente,
      "4321",
      "4321"
    );

    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(emailInexistente)).thenReturn(null);

    assertThrows(
      EmailInexistenteException.class,
      () -> this.servicioRecuperacion.recuperarContrasenia(datos)
    );
    // assertThrows(RuntimeException.class, () -> {
    // this.servicioRecuperacion.recuperarContrasenia(datos);
    // }
    // );
  }

  @Test
  public void SiLasContraseniasNoCoincidenDebeLanzarException() {
    String emailValido = "test@gmail.com";

    // Las contraseñas son distintas
    DatosRecuperacionContrasenia datos = new DatosRecuperacionContrasenia(
      emailValido,
      "1234",
      "9999"
    );

    Usuario usuario = new Usuario();
    usuario.setEmail(emailValido);
    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(emailValido)).thenReturn(usuario);

    assertThrows(
      ContraseniasNoCoincidenException.class,
      () -> this.servicioRecuperacion.recuperarContrasenia(datos)
    );
    // assertThrows(RuntimeException.class,
    // () -> {
    // this.servicioRecuperacion.recuperarContrasenia(datos);
    // }
    // );
  }

  @Test
  public void deberiaModificarLosDatosDeRecuperacionConSetters() {
    DatosRecuperacionContrasenia datos = new DatosRecuperacionContrasenia(
      "viejo@mail.com",
      "vieja1",
      "vieja2"
    );

    datos.setEmail("nuevo@mail.com");
    datos.setContrasenia1("Password1!");
    datos.setContrasenia2("Password1!");

    assertThat(datos.getEmail(), is("nuevo@mail.com"));
    assertThat(datos.getContrasenia1(), is("Password1!"));
    assertThat(datos.getContrasenia2(), is("Password1!"));
  }
}
