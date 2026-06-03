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
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.presentacion.DatosRegistroHabito;
import java.util.ArrayList;
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
  public void deberiaActualizarElProgresoEnCincuentaCuandoLaMitadDeItemsEstanCompletados()
    throws ChecklistInsuficienteExeption {
    Habito habito = new Habito();
    List<ItemChecklist> items = new ArrayList<>();
    ItemChecklist itemCompletado = new ItemChecklist();
    ItemChecklist itemPendiente = new ItemChecklist();

    itemCompletado.setEstadoChecklist(true);
    itemPendiente.setEstadoChecklist(false);
    items.add(itemCompletado);
    items.add(itemPendiente);
    habito.setCantidadDeChecklist(items);

    this.servicioHabitos.actualizarProgresoActualHabito(habito);

    assertThat(habito.getProgresoActual(), is(50));
  }

  @Test
  public void deberiaActualizarElProgresoEnCeroCuandoNingunItemEstaCompletado()
    throws ChecklistInsuficienteExeption {
    Habito habito = new Habito();
    List<ItemChecklist> items = new ArrayList<>();
    ItemChecklist primerItem = new ItemChecklist();
    ItemChecklist segundoItem = new ItemChecklist();

    primerItem.setEstadoChecklist(false);
    segundoItem.setEstadoChecklist(false);
    items.add(primerItem);
    items.add(segundoItem);
    habito.setCantidadDeChecklist(items);

    this.servicioHabitos.actualizarProgresoActualHabito(habito);

    assertThat(habito.getProgresoActual(), is(0));
  }

  @Test
  public void deberiaActualizarElProgresoEnCienCuandoTodosLosItemsEstanCompletados()
    throws ChecklistInsuficienteExeption {
    Habito habito = new Habito();
    List<ItemChecklist> items = new ArrayList<>();
    ItemChecklist primerItem = new ItemChecklist();
    ItemChecklist segundoItem = new ItemChecklist();

    primerItem.setEstadoChecklist(true);
    segundoItem.setEstadoChecklist(true);
    items.add(primerItem);
    items.add(segundoItem);
    habito.setCantidadDeChecklist(items);

    this.servicioHabitos.actualizarProgresoActualHabito(habito);

    assertThat(habito.getProgresoActual(), is(100));
  }

  @Test
  public void deberiaGuardarElHabitoCuandoSeAgregaUnItemChecklist()
    throws ChecklistInsuficienteExeption {
    Integer idHabito = 1;
    Habito habito = new Habito();
    ItemChecklist item = new ItemChecklist();

    habito.setCantidadDeChecklist(new ArrayList<>());
    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    this.servicioHabitos.agregarItemChecklistAlHabito(item, idHabito);

    assertThat(habito.getCantidadDeChecklist().size(), is(1));
    assertThat(item.getHabito(), is(habito));
    verify(this.repositorioHabitoMock, times(1)).modificar(habito);
  }

  @Test
  public void deberiaGuardarElHabitoCuandoSeEliminaUnItemChecklist()
    throws ChecklistInsuficienteExeption {
    Integer idHabito = 1;
    Habito habito = new Habito();
    ItemChecklist itemAEliminar = new ItemChecklist();
    ItemChecklist itemRestante = new ItemChecklist();

    itemRestante.setEstadoChecklist(true);
    habito.setCantidadDeChecklist(new ArrayList<>());
    habito.agregarItemChecklist(itemAEliminar);
    habito.agregarItemChecklist(itemRestante);
    when(this.repositorioHabitoMock.buscarPorId(idHabito)).thenReturn(habito);

    this.servicioHabitos.eliminarItemChecklistDelHabito(itemAEliminar, idHabito);

    assertThat(habito.getCantidadDeChecklist().size(), is(1));
    assertThat(itemAEliminar.getHabito(), is((Habito) null));
    verify(this.repositorioHabitoMock, times(1)).modificar(habito);
  }

  @Test
  public void deberiaLanzarExcepcionCuandoSeActualizaProgresoSinItemsChecklist() {
    Habito habito = new Habito();
    habito.setCantidadDeChecklist(new ArrayList<>());

    assertThrows(
      ChecklistInsuficienteExeption.class,
      () -> this.servicioHabitos.actualizarProgresoActualHabito(habito)
    );

    assertThat(habito.getProgresoActual(), is(0));
  }

  @Test
  public void deberiaActualizarElProgresoEnCeroCuandoNoHayItemsChecklist() {
    Habito habito = new Habito();
    habito.setCantidadDeChecklist(new ArrayList<>());

    assertThrows(
      ChecklistInsuficienteExeption.class,
      () -> this.servicioHabitos.actualizarProgresoActualHabito(habito)
    );

    assertThat(habito.getProgresoActual(), is(0));
  }

  @Test
  public void cuandoUnUsuarioMarcaUnItemChecklistDeberiaActualizarElEstadoDelMismo()
    throws ChecklistInsuficienteExeption {
    ItemChecklist item = new ItemChecklist();
    item.setId(1);

    Habito habito = new Habito();
    habito.setId(1);
    habito.setCantidadDeChecklist(new ArrayList<>());

    habito.agregarItemChecklist(item);

    when(repositorioHabitoMock.buscarPorId(habito.getId())).thenReturn(habito);

    this.servicioHabitos.actualizarEstadoItemChecklist(item.getId(), habito.getId());

    assertThat(item.getEstadoChecklist(), is(true));
  }

  @Test
  public void cuandoUnUsuarioDesmarcaUnItemChecklistDeberiaActualizarElEstadoDelMismo()
    throws ChecklistInsuficienteExeption {
    ItemChecklist item = new ItemChecklist();
    item.setId(1);
    item.setEstadoChecklist(true);

    Habito habito = new Habito();
    habito.setId(1);
    habito.setCantidadDeChecklist(new ArrayList<>());

    habito.agregarItemChecklist(item);

    when(repositorioHabitoMock.buscarPorId(habito.getId())).thenReturn(habito);

    this.servicioHabitos.actualizarEstadoItemChecklist(item.getId(), habito.getId());

    assertThat(item.getEstadoChecklist(), is(false));
  }
}
