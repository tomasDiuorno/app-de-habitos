package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class ServicioRegistroTest {

  private ServicioRegistro servicioRegistro;
  private RepositorioUsuario repositorioUsuarioMock;

  @BeforeEach
  public void init() {
    this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
    this.servicioRegistro = new ServicioRegistroImp(this.repositorioUsuarioMock);
  }

  @Test
  public void registrarUsuarioSiNoExisteDeberiaGuardarlo() throws UsuarioExistente {
    // preparacion
    Usuario usuario = new Usuario();
    usuario.setEmail("nuevo@test.com");
    String contraseniaPlana = "miClaveSegura123";
    usuario.setPassword(contraseniaPlana);
    when(this.repositorioUsuarioMock.buscarPorEmail(usuario.getEmail())).thenReturn(null);

    // ejecucion
    this.servicioRegistro.registrar(usuario);

    // validacion
    verify(this.repositorioUsuarioMock, times(1)).guardar(usuario);

    //Compueba que la contraseña del objeto ya no es igual al texto plano
    assertNotEquals(contraseniaPlana, usuario.getPassword());

    //Comprueba que lo que tiene el objeto ahora es un hash válido de BCrypt que coincide con la plana
    assertTrue(BCrypt.checkpw(contraseniaPlana, usuario.getPassword()));
  }

  @Test
  public void registrarUsuarioSiExisteDeberiaLanzarExcepcion() {
    // preparacion
    Usuario usuario = new Usuario();
    usuario.setEmail("existe@test.com");
    when(this.repositorioUsuarioMock.buscarPorEmail(usuario.getEmail())).thenReturn(new Usuario());

    // ejecucion y validacion
    assertThrows(UsuarioExistente.class, () -> this.servicioRegistro.registrar(usuario));
    verify(this.repositorioUsuarioMock, times(0)).guardar(usuario);
  }
}
