package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
  public void quieroRecuperarLaContraseniaExitosamente() {
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

    when(this.repositorioUsuarioMock.buscarPorEmail(emailValido)).thenReturn(usuarioSimulado);

    this.servicioRecuperacion.recuperarContrasenia(datos);

    String contraseniaCambiada = datos.getContrasenia1();

    assertEquals("4321", contraseniaCambiada);
  }
}
