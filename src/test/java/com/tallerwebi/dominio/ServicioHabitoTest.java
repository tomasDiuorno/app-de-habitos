package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.excepcion.ChecklistInsuficienteExeption;
import com.tallerwebi.dominio.excepcion.DescripcionChecklistInvalidaException;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.HabitoNoEncontradoException;
import com.tallerwebi.dominio.excepcion.ItemChecklistNoEncontradoException;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.presentacion.DatosRegistroHabito;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class ServicioHabitoTest {

  private ServicioHabito servicioHabitos;
  private RepositorioHabito repositorioHabitoMock;
  private RepositorioUsuarioHabito repositorioUsuarioHabitoMock;
  private RepositorioCategoria repositorioCategoriaMock;
  private ServicioLogro servicioLogroMock;

  @BeforeEach
  public void init() {
    this.repositorioHabitoMock = mock(RepositorioHabito.class);
    this.repositorioUsuarioHabitoMock = mock(RepositorioUsuarioHabito.class);
    this.repositorioCategoriaMock = mock(RepositorioCategoria.class);
    this.servicioLogroMock = mock(ServicioLogro.class);

    this.servicioHabitos =
      new ServicioHabitoImp(
        this.repositorioHabitoMock,
        this.repositorioUsuarioHabitoMock,
        this.repositorioCategoriaMock,
        this.servicioLogroMock
      );
  }

  @Test
  public void obtenerHabitosInicialesDeberiaRetornarUnaListaDeHabitos() {
    List<Habito> habitosMock = new ArrayList<>();
    Habito habito1 = new Habito();
    habito1.setTitulo("Meditar");
    Habito habito2 = new Habito();
    habito2.setTitulo("Leer un libro");
    Habito habito3 = new Habito();
    habito3.setTitulo("Hacer ejercicio");

    habitosMock.add(habito1);
    habitosMock.add(habito2);
    habitosMock.add(habito3);

    when(this.repositorioHabitoMock.obtenerHabitosIniciales()).thenReturn(habitosMock);

    List<Habito> habitosObtenidos = this.servicioHabitos.obtenerHabitosIniciales();

    assertThat(habitosObtenidos.size(), equalTo(3));
    assertThat(habitosObtenidos.get(0).getTitulo(), equalTo("Meditar"));
    assertThat(habitosObtenidos.get(1).getTitulo(), equalTo("Leer un libro"));
    assertThat(habitosObtenidos.get(2).getTitulo(), equalTo("Hacer ejercicio"));
  }

  @Test
  public void alCrearUnHabitoDeberiaGuardarloExisosamente() throws HabitoExistenteExeption {
    Habito habitoEsperado = new Habito();
    Categoria cat = new Categoria();
    cat.setNombre("Deportes");
    habitoEsperado.setTitulo("Correr");
    habitoEsperado.setCategoria(cat);

    when(this.repositorioHabitoMock.buscarPorTitulo(habitoEsperado.getTitulo())).thenReturn(null);

    this.servicioHabitos.agregarHabito(habitoEsperado);

    verify(this.repositorioHabitoMock, times(1)).guardar(habitoEsperado);
  }

  @Test
  public void alBuscarUnHabitoExistenteDeberiaRetornarlo() {
    String titulo = "Correr";
    String categoria = "Deporte";
    Habito habitoEsperado = new Habito();
    Categoria cat = new Categoria();
    cat.setNombre(categoria);
    habitoEsperado.setTitulo(titulo);
    habitoEsperado.setCategoria(cat);

    when(this.repositorioHabitoMock.buscarPorTitulo(habitoEsperado.getTitulo()))
      .thenReturn(new Habito());

    assertThrows(
      HabitoExistenteExeption.class,
      () -> this.servicioHabitos.agregarHabito(habitoEsperado)
    );

    verify(this.repositorioHabitoMock, times(0)).guardar(any(Habito.class));
  }

  @Test
  public void alCrearUnHabitoParaUnUsuarioDeberiaGuardarloYAsignarloAlUsuario()
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException {
    Usuario usuario = new Usuario();
    usuario.setId(1);

    DatosRegistroHabito datosHabito = new DatosRegistroHabito();
    datosHabito.setTitulo("Dormir temprano");
    datosHabito.setCategoriaId(1);
    Habito habito = this.servicioHabitos.obtenerHabito(datosHabito);

    when(this.repositorioHabitoMock.buscarPorTitulo(habito.getTitulo())).thenReturn(null);

    this.servicioHabitos.agregarHabitoParaUsuario(habito, usuario);

    verify(this.repositorioHabitoMock, times(1)).guardar(habito);

    ArgumentCaptor<UsuarioHabito> captor = ArgumentCaptor.forClass(UsuarioHabito.class);
    verify(this.repositorioUsuarioHabitoMock, times(1)).guardar(captor.capture());

    UsuarioHabito usuarioHabitoGuardado = captor.getValue();

    assertThat(usuarioHabitoGuardado.getUsuario(), is(usuario));
    assertThat(usuarioHabitoGuardado.getHabito(), is(habito));
    assertThat(usuarioHabitoGuardado.getActivo(), is(true));
    assertThat(usuario.getUsuarioHabito().size(), equalTo(1));

    verify(this.servicioLogroMock, times(1)).verificarYAsignarLogros(usuario);
  }

  @Test
  public void alCrearUnHabitoParaUnUsuarioConCuatroHabitosDeberiaLanzarExcepcion()
    throws HabitoExistenteExeption {
    DatosRegistroHabito datosHabito = new DatosRegistroHabito();
    datosHabito.setTitulo("Dormir temprano");
    Usuario usuario = new Usuario();

    List<UsuarioHabito> usuarioHabitos = new ArrayList<>();
    usuarioHabitos.add(new UsuarioHabito());
    usuarioHabitos.add(new UsuarioHabito());
    usuarioHabitos.add(new UsuarioHabito());
    usuarioHabitos.add(new UsuarioHabito());

    usuario.setUsuarioHabito(usuarioHabitos);

    Habito habito = new Habito();
    habito.setTitulo(datosHabito.getTitulo());

    assertThrows(
      LimiteHabitosAlcanzadoException.class,
      () -> this.servicioHabitos.agregarHabitoParaUsuario(habito, usuario)
    );

    verify(this.repositorioHabitoMock, times(0)).guardar(any(Habito.class));
    verify(this.repositorioUsuarioHabitoMock, times(0)).guardar(any(UsuarioHabito.class));
  }

  @Test
  public void deberiaActualizarElProgresoEnCeroCuandoNoTieneItemsChecklist()
    throws ChecklistInsuficienteExeption {
    Habito habito = new Habito();
    habito.setCantidadDeChecklist(new ArrayList<>());

    this.servicioHabitos.actualizarProgresoActualHabito(habito);

    assertThat(habito.getProgresoActual(), is(0));
  }

  @Test
  public void deberiaActualizarElProgresoEnCeroCuandoNingunItemEstaCompletado()
    throws ChecklistInsuficienteExeption {
    Habito habito = new Habito();

    ItemChecklist itemUno = new ItemChecklist();
    itemUno.setEstadoChecklist(false);

    ItemChecklist itemDos = new ItemChecklist();
    itemDos.setEstadoChecklist(false);

    habito.setCantidadDeChecklist(Arrays.asList(itemUno, itemDos));

    this.servicioHabitos.actualizarProgresoActualHabito(habito);

    assertThat(habito.getProgresoActual(), is(0));
  }

  @Test
  public void deberiaActualizarElProgresoEnCincuentaCuandoLaMitadEstaCompletada()
    throws ChecklistInsuficienteExeption {
    Habito habito = new Habito();

    ItemChecklist itemCompletado = new ItemChecklist();
    itemCompletado.setEstadoChecklist(true);

    ItemChecklist itemPendiente = new ItemChecklist();
    itemPendiente.setEstadoChecklist(false);

    habito.setCantidadDeChecklist(Arrays.asList(itemCompletado, itemPendiente));

    this.servicioHabitos.actualizarProgresoActualHabito(habito);

    assertThat(habito.getProgresoActual(), is(50));
  }

  @Test
  public void deberiaActualizarElProgresoEnCienCuandoTodosLosItemsEstanCompletados()
    throws ChecklistInsuficienteExeption {
    Habito habito = new Habito();

    ItemChecklist itemUno = new ItemChecklist();
    itemUno.setEstadoChecklist(true);

    ItemChecklist itemDos = new ItemChecklist();
    itemDos.setEstadoChecklist(true);

    habito.setCantidadDeChecklist(Arrays.asList(itemUno, itemDos));

    this.servicioHabitos.actualizarProgresoActualHabito(habito);

    assertThat(habito.getProgresoActual(), is(100));
  }

  @Test
  public void agregarItemChecklistAlHabitoDeberiaAgregarElItemYModificarElHabito()
    throws Exception {
    Integer idHabito = 1;

    Habito habito = new Habito();
    habito.setId(idHabito);
    habito.setCantidadDeChecklist(new ArrayList<>());

    ItemChecklist item = new ItemChecklist();
    item.setDescripcion("Tomar agua");

    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    this.servicioHabitos.agregarItemChecklistAlHabito(item, idHabito);

    assertThat(habito.getCantidadDeChecklist().size(), is(1));
    assertThat(habito.getCantidadDeChecklist().get(0), is(item));
    assertThat(item.getHabito(), is(habito));
    assertThat(habito.getProgresoActual(), is(0));

    verify(this.repositorioHabitoMock, times(1)).buscarPorId(idHabito);
    verify(this.repositorioHabitoMock, times(1)).modificar(habito);
  }

  @Test
  public void eliminarItemChecklistDelHabitoDeberiaEliminarElItemYModificarElHabito()
    throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    Habito habito = new Habito();
    habito.setId(idHabito);
    habito.setCantidadDeChecklist(new ArrayList<>());

    ItemChecklist itemAEliminar = new ItemChecklist();
    itemAEliminar.setId(idItem);
    itemAEliminar.setDescripcion("Leer");
    itemAEliminar.setEstadoChecklist(false);

    ItemChecklist itemRestante = new ItemChecklist();
    itemRestante.setId(20);
    itemRestante.setDescripcion("Caminar");
    itemRestante.setEstadoChecklist(true);

    habito.agregarItemChecklist(itemAEliminar);
    habito.agregarItemChecklist(itemRestante);

    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    this.servicioHabitos.eliminarItemChecklistDelHabito(idHabito, idItem);

    assertThat(habito.getCantidadDeChecklist().size(), is(1));
    assertThat(habito.getCantidadDeChecklist().get(0), is(itemRestante));
    assertThat(itemAEliminar.getHabito(), is((Habito) null));
    assertThat(habito.getProgresoActual(), is(100));

    verify(this.repositorioHabitoMock, times(1)).buscarPorId(idHabito);
    verify(this.repositorioHabitoMock, times(1)).modificar(habito);
  }

  @Test
  public void eliminarItemChecklistDelHabitoCuandoNoExisteElItemDeberiaLanzarExcepcion()
    throws Exception {
    Integer idHabito = 1;
    Integer idItemInexistente = 99;

    Habito habito = new Habito();
    habito.setId(idHabito);
    habito.setCantidadDeChecklist(new ArrayList<>());

    ItemChecklist itemExistente = new ItemChecklist();
    itemExistente.setId(10);

    habito.agregarItemChecklist(itemExistente);

    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    assertThrows(
      ItemChecklistNoEncontradoException.class,
      () -> this.servicioHabitos.eliminarItemChecklistDelHabito(idHabito, idItemInexistente)
    );

    verify(this.repositorioHabitoMock, times(1)).buscarPorId(idHabito);
    verify(this.repositorioHabitoMock, times(0)).modificar(habito);
  }

  @Test
  public void buscarHabitoPorIdDeberiaRetornarElHabitoEncontrado() throws Exception {
    Integer idHabito = 1;

    Habito habito = new Habito();
    habito.setId(idHabito);
    habito.setTitulo("Entrenar");
    habito.setCantidadDeChecklist(new ArrayList<>());

    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    Habito resultado = this.servicioHabitos.buscarHabitoPorId(idHabito);

    assertThat(resultado, is(habito));
    assertThat(resultado.getTitulo(), equalTo("Entrenar"));

    verify(this.repositorioHabitoMock, times(1)).buscarPorId(idHabito);
  }

  @Test
  public void buscarHabitoPorIdCuandoNoExisteDeberiaLanzarExcepcion() {
    Integer idHabito = 99;

    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(null);

    assertThrows(
      HabitoNoEncontradoException.class,
      () -> this.servicioHabitos.buscarHabitoPorId(idHabito)
    );

    verify(this.repositorioHabitoMock, times(1)).buscarPorId(idHabito);
  }

  @Test
  public void actualizarEstadoItemChecklistDeberiaMarcarComoCompletadoUnItemPendiente()
    throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    Habito habito = new Habito();
    habito.setId(idHabito);
    habito.setCantidadDeChecklist(new ArrayList<>());

    ItemChecklist item = new ItemChecklist();
    item.setId(idItem);
    item.setEstadoChecklist(false);

    habito.agregarItemChecklist(item);

    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    this.servicioHabitos.actualizarEstadoItemChecklist(idItem, idHabito);

    assertThat(item.getEstadoChecklist(), is(true));
    assertThat(habito.getProgresoActual(), is(100));

    verify(this.repositorioHabitoMock, times(1)).buscarPorId(idHabito);
    verify(this.repositorioHabitoMock, times(1)).modificar(habito);
  }

  @Test
  public void actualizarEstadoItemChecklistDeberiaDesmarcarUnItemCompletado() throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    Habito habito = new Habito();
    habito.setId(idHabito);
    habito.setCantidadDeChecklist(new ArrayList<>());

    ItemChecklist item = new ItemChecklist();
    item.setId(idItem);
    item.setEstadoChecklist(true);

    habito.agregarItemChecklist(item);

    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    this.servicioHabitos.actualizarEstadoItemChecklist(idItem, idHabito);

    assertThat(item.getEstadoChecklist(), is(false));
    assertThat(habito.getProgresoActual(), is(0));

    verify(this.repositorioHabitoMock, times(1)).buscarPorId(idHabito);
    verify(this.repositorioHabitoMock, times(1)).modificar(habito);
  }

  @Test
  public void actualizarEstadoItemChecklistCuandoNoExisteElItemDeberiaLanzarExcepcion()
    throws Exception {
    Integer idHabito = 1;

    Habito habito = new Habito();
    habito.setId(idHabito);
    habito.setCantidadDeChecklist(new ArrayList<>());

    ItemChecklist item = new ItemChecklist();
    item.setId(10);

    habito.agregarItemChecklist(item);

    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    assertThrows(
      ItemChecklistNoEncontradoException.class,
      () -> this.servicioHabitos.actualizarEstadoItemChecklist(99, idHabito)
    );

    verify(this.repositorioHabitoMock, times(1)).buscarPorId(idHabito);
    verify(this.repositorioHabitoMock, times(0)).modificar(habito);
  }

  @Test
  public void editarDescripcionItemChecklistDeberiaCambiarLaDescripcionYModificarElHabito()
    throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    Habito habito = new Habito();
    habito.setId(idHabito);
    habito.setCantidadDeChecklist(new ArrayList<>());

    ItemChecklist item = new ItemChecklist();
    item.setId(idItem);
    item.setDescripcion("Descripción vieja");

    habito.agregarItemChecklist(item);

    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    this.servicioHabitos.editarDescripcionItemChecklist(idItem, idHabito, "Descripción nueva");

    assertThat(item.getDescripcion(), equalTo("Descripción nueva"));

    verify(this.repositorioHabitoMock, times(1)).buscarPorId(idHabito);
    verify(this.repositorioHabitoMock, times(1)).modificar(habito);
  }

  @Test
  public void editarDescripcionItemChecklistCuandoNoExisteElItemDeberiaLanzarExcepcion()
    throws Exception {
    Integer idHabito = 1;

    Habito habito = new Habito();
    habito.setId(idHabito);
    habito.setCantidadDeChecklist(new ArrayList<>());

    ItemChecklist item = new ItemChecklist();
    item.setId(10);

    habito.agregarItemChecklist(item);

    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    assertThrows(
      ItemChecklistNoEncontradoException.class,
      () -> this.servicioHabitos.editarDescripcionItemChecklist(99, idHabito, "Nueva descripción")
    );

    verify(this.repositorioHabitoMock, times(1)).buscarPorId(idHabito);
    verify(this.repositorioHabitoMock, times(0)).modificar(habito);
  }

  @Test
  public void editarDescripcionItemChecklistConDescripcionVaciaDeberiaLanzarExcepcion()
    throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    Habito habito = new Habito();
    habito.setId(idHabito);
    habito.setCantidadDeChecklist(new ArrayList<>());

    ItemChecklist item = new ItemChecklist();
    item.setId(idItem);
    item.setDescripcion("Vieja descripción");

    habito.agregarItemChecklist(item);

    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    assertThrows(
      DescripcionChecklistInvalidaException.class,
      () -> this.servicioHabitos.editarDescripcionItemChecklist(idItem, idHabito, "")
    );

    verify(this.repositorioHabitoMock, times(0)).modificar(habito);
  }

  @Test
  public void editarDescripcionItemChecklistConDescripcionNullDeberiaLanzarExcepcion()
    throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    Habito habito = new Habito();
    habito.setId(idHabito);
    habito.setCantidadDeChecklist(new ArrayList<>());

    ItemChecklist item = new ItemChecklist();
    item.setId(idItem);
    item.setDescripcion("Vieja descripción");

    habito.agregarItemChecklist(item);

    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    assertThrows(
      DescripcionChecklistInvalidaException.class,
      () -> this.servicioHabitos.editarDescripcionItemChecklist(idItem, idHabito, null)
    );

    verify(this.repositorioHabitoMock, times(0)).modificar(habito);
  }
}
