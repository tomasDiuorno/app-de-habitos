package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.excepcion.HabitoNoPerteneceAlUsuarioException;
import com.tallerwebi.dominio.excepcion.HabitoYaCompletadoHoyException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class ServicioHistorialHabitoTest {

  private ServicioHistorialHabito servicioHistorialHabito;
  private RepositorioHistorialHabito repositorioHistorialHabitoMock;

  @BeforeEach
  public void init() {
    this.repositorioHistorialHabitoMock = mock(RepositorioHistorialHabito.class);


    this.servicioHistorialHabito = new ServicioHistorialHabitoImp(
      this.repositorioHistorialHabitoMock
    );
  }

  @Test
  public void alIntentarCompletarUnHabitoNuloOPertenecienteAOtroUsuarioDeberiaLanzarExcepcion() {
    Usuario usuario = new Usuario();
    usuario.setUsuarioHabito(new ArrayList<>()); // Usuario sin hábitos

    assertThrows(
      HabitoNoPerteneceAlUsuarioException.class,
      () -> this.servicioHistorialHabito.marcarHabitoComoCompletado(usuario, 99)
    );

    verify(this.repositorioHistorialHabitoMock, times(0)).guardar(any(HistorialHabito.class));
  }

  @Test
  public void alCompletarUnHabitoQueNoPerteneceAlUsuarioDeberiaLanzarExcepcion() {
    Usuario usuario = new Usuario();
    Habito habitoDelUsuario = new Habito();
    habitoDelUsuario.setId(1);

    UsuarioHabito usuarioHabito = new UsuarioHabito();
    usuarioHabito.setHabito(habitoDelUsuario);
    usuarioHabito.setUsuario(usuario);

    List<UsuarioHabito> habitosActivos = new ArrayList<>();
    habitosActivos.add(usuarioHabito);
    usuario.setUsuarioHabito(habitosActivos);

    assertThrows(
      HabitoNoPerteneceAlUsuarioException.class,
      () -> this.servicioHistorialHabito.marcarHabitoComoCompletado(usuario, 2)
    );

    verify(this.repositorioHistorialHabitoMock, times(0)).guardar(any(HistorialHabito.class));
  }
  @Test
  public void alCompletarUnHabitoYaCompletadoEnElMismoDiaDeberiaLanzarExcepcion() {
    Usuario usuario = new Usuario();
    Habito habito = new Habito();
    habito.setId(2);

    UsuarioHabito usuarioHabito = new UsuarioHabito();
    usuarioHabito.setHabito(habito);
    usuario.setUsuarioHabito(new ArrayList<>());
    usuario.getUsuarioHabito().add(usuarioHabito);

    HistorialHabito historialExistente = new HistorialHabito();
    
    when(this.repositorioHistorialHabitoMock.obtenerPorUsuarioHabitoYFecha(eq(usuario), eq(habito), any(LocalDate.class)))
      .thenReturn(historialExistente);

    assertThrows(
      HabitoYaCompletadoHoyException.class,
      () -> this.servicioHistorialHabito.marcarHabitoComoCompletado(usuario, 2)
    );

    verify(this.repositorioHistorialHabitoMock, times(0)).guardar(any(HistorialHabito.class));
  }

  @Test
  public void alObtenerElHistorialDeUnUsuarioDeberiaDevolverUnaLista() {
    Usuario usuario = new Usuario();
    List<HistorialHabito> listaSimulada = new ArrayList<>();
    listaSimulada.add(new HistorialHabito());
    listaSimulada.add(new HistorialHabito());

    when(this.repositorioHistorialHabitoMock.obtenerPorUsuario(usuario)).thenReturn(listaSimulada);

    List<HistorialHabito> historialObtenido = this.servicioHistorialHabito.obtenerHistorial(usuario);

    assertThat(historialObtenido.size(), equalTo(2));
    verify(this.repositorioHistorialHabitoMock, times(1)).obtenerPorUsuario(usuario);
  }

  @Test
  public void alCompletarUnHabitoDebeGuardarseConLaFechaActualYDatosCorrectos() throws HabitoNoPerteneceAlUsuarioException, HabitoYaCompletadoHoyException {
    Usuario usuario = new Usuario();
    Habito habito = new Habito();
    habito.setId(3);

    UsuarioHabito usuarioHabito = new UsuarioHabito();
    usuarioHabito.setHabito(habito);
    usuario.setUsuarioHabito(new ArrayList<>());
    usuario.getUsuarioHabito().add(usuarioHabito);

    when(this.repositorioHistorialHabitoMock.obtenerPorUsuarioHabitoYFecha(eq(usuario), eq(habito), any(LocalDate.class)))
      .thenReturn(null);

    this.servicioHistorialHabito.marcarHabitoComoCompletado(usuario, 3);

    ArgumentCaptor<HistorialHabito> captor = ArgumentCaptor.forClass(HistorialHabito.class);
    verify(this.repositorioHistorialHabitoMock, times(1)).guardar(captor.capture());

    HistorialHabito historialGuardado = captor.getValue();
    
    assertThat(historialGuardado.getUsuario(), is(usuario));
    assertThat(historialGuardado.getHabito(), is(habito));
    assertThat(historialGuardado.getFechaCompletado(), is(LocalDate.now()));
  }
}
