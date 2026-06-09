package com.tallerwebi.dominio.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.ContraseniasNoCoincidenException;
import com.tallerwebi.dominio.excepcion.EmailInexistenteException;
import com.tallerwebi.dominio.interfaz.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.ServicioRecuperacionContrasenia;
import com.tallerwebi.presentacion.DTO.RecuperacionContraseniaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioRecuperacionContraseniaTest {

  private ServicioRecuperacionContrasenia servicioRecuperacion;
  private RepositorioUsuario repositorioUsuarioMock;

  @BeforeEach
  public void init() {
    this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
    this.servicioRecuperacion =
      new ServicioRecuperacionContraseniaImpl(this.repositorioUsuarioMock);
  }

  @Test
  public void quieroRecuperarLaContraseniaExitosamente()
    throws ContraseniasNoCoincidenException, EmailInexistenteException {
    String emailValido = "test@gmail.com";
    String contraseniaNueva1 = "4321";
    String contraseniaNueva2 = "4321";

    RecuperacionContraseniaDTO datos = new RecuperacionContraseniaDTO(
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
    RecuperacionContraseniaDTO datos = new RecuperacionContraseniaDTO(
      emailInexistente,
      "4321",
      "4321"
    );

    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(emailInexistente)).thenReturn(null);

    assertThrows(
      EmailInexistenteException.class,
      () -> this.servicioRecuperacion.recuperarContrasenia(datos)
    );
  }

  @Test
  public void SiLasContraseniasNoCoincidenDebeLanzarException() {
    String emailValido = "test@gmail.com";

    // Las contraseñas son distintas
    RecuperacionContraseniaDTO datos = new RecuperacionContraseniaDTO(emailValido, "1234", "9999");

    Usuario usuario = new Usuario();
    usuario.setEmail(emailValido);
    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(emailValido)).thenReturn(usuario);

    assertThrows(
      ContraseniasNoCoincidenException.class,
      () -> this.servicioRecuperacion.recuperarContrasenia(datos)
    );
  }

  @Test
  public void SiElMailNoExisteDebeLanzarException() {
    String email = "test@gmail.com";
    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(email)).thenReturn(null);

    // Las contraseñas son distintas
    RecuperacionContraseniaDTO datos = new RecuperacionContraseniaDTO(email, "1234", "1234");

    assertThrows(
      EmailInexistenteException.class,
      () -> this.servicioRecuperacion.recuperarContrasenia(datos)
    );
  }
}
