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
  public void deberiaLanzarExcepcionCuandoSeActualizaProgresoSinItemsChecklist() {
    Habito habito = new Habito();
    habito.setCantidadDeChecklist(new ArrayList<>());

    assertThrows(
      ChecklistInsuficienteExeption.class,
      () -> this.servicioHabitos.actualizarProgresoActualHabito(habito)
    );

    assertThat(habito.getProgresoActual(), is(0));
  }
}
