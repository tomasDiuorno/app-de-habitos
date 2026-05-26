package com.tallerwebi.dominio;

import static org.hamcrest.Matchers.theInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosRegistro;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioRegistroTest {

  private ServicioRegistro servicioRegistro;
  private RepositorioUsuario repositorioUsuarioMock;
  private RepositorioHabito repositorioHabitoMock;
  private RepositorioUsuarioHabito repositorioUsuarioHabitoMock;

  @BeforeEach
  public void init() {
    this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
    this.repositorioHabitoMock = mock(RepositorioHabito.class);
    this.repositorioUsuarioHabitoMock = mock(RepositorioUsuarioHabito.class);
    this.servicioRegistro =
      new ServicioRegistroImp(
        this.repositorioUsuarioMock,
        this.repositorioHabitoMock,
        this.repositorioUsuarioHabitoMock
      );
  }

  @Test
  public void registrarUsuarioSiNoExisteDeberiaGuardarlo() throws UsuarioExistente {
    // preparacion
    DatosRegistro datos = new DatosRegistro();
    datos.setEmail("nuevo@test.com");
    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(datos.getEmail())).thenReturn(null);

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
    when(this.repositorioUsuarioMock.buscarPorEmailOrUsername(datos.getEmail())).thenReturn(new Usuario());

    // ejecucion y validacion
    assertThrows(UsuarioExistente.class, () -> this.servicioRegistro.registrar(datos));
    verify(this.repositorioUsuarioMock, times(0)).guardar(any(Usuario.class));
  }

  @Test
  public void registrarUnUsuarioConHabitosSeleccionadosDeberiaGuardarUsuarioHabito()
    throws UsuarioExistente {
    DatosRegistro datos = new DatosRegistro();
    datos.setEmail("test@email.com");
    datos.setPassword("Password1!");
    datos.setHabitosSeleccionados(Arrays.asList(1, 2));

    Habito habito1 = new Habito();
    Habito habito2 = new Habito();

    when(repositorioUsuarioMock.buscarPorEmailOrUsername(datos.getEmail())).thenReturn(null);
    when(repositorioHabitoMock.buscarPorIds(datos.getHabitosSeleccionados()))
      .thenReturn(Arrays.asList(habito1, habito2));

    servicioRegistro.registrar(datos);

    verify(this.repositorioUsuarioMock, times(1)).guardar(any(Usuario.class));
    verify(this.repositorioHabitoMock, times(1)).buscarPorIds(datos.getHabitosSeleccionados());
    verify(this.repositorioUsuarioHabitoMock, times(2)).guardar(any(UsuarioHabito.class));
  }
}
