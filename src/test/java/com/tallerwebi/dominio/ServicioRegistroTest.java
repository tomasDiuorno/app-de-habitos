package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosRegistro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    DatosRegistro datos = new DatosRegistro();
    datos.setEmail("nuevo@test.com");
    when(this.repositorioUsuarioMock.buscarPorEmail(datos.getEmail())).thenReturn(null);

    // ejecucion
    this.servicioRegistro.registrar(datos);

    // validacion
    verify(this.repositorioUsuarioMock, times(1)).guardar(any(Usuario.class));
  }

  @Test
  public void registrarUsuarioSiExisteDeberiaLanzarExcepcion() {
    // preparacion
    DatosRegistro datos = new DatosRegistro();
    datos.setEmail("existe@test.com");
    when(this.repositorioUsuarioMock.buscarPorEmail(datos.getEmail())).thenReturn(new Usuario());

    // ejecucion y validacion
    assertThrows(UsuarioExistente.class, () -> this.servicioRegistro.registrar(datos));
    verify(this.repositorioUsuarioMock, times(0)).guardar(any(Usuario.class));
  }
}
