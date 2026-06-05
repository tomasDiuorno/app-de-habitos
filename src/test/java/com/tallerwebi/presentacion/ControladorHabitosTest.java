package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.ItemChecklist;
import com.tallerwebi.dominio.ServicioCategoria;
import com.tallerwebi.dominio.ServicioHabito;
import com.tallerwebi.dominio.excepcion.DescripcionChecklistInvalidaException;
import com.tallerwebi.dominio.excepcion.HabitoNoEncontradoException;
import com.tallerwebi.dominio.excepcion.ItemChecklistNoEncontradoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ControladorHabitosTest {

  private ControladorHabitos controladorHabitos;
  private ServicioHabito servicioHabitoMock;
  private ServicioCategoria servicioCategoriaMock;

  @BeforeEach
  public void init() {
    this.servicioHabitoMock = mock(ServicioHabito.class);
    this.servicioCategoriaMock = mock(ServicioCategoria.class);

    this.controladorHabitos =
      new ControladorHabitos(this.servicioHabitoMock, this.servicioCategoriaMock);
  }

  @Test
  public void agregarChecklistDeberiaRetornarOkCuandoSeAgregaCorrectamente() throws Exception {
    Integer idHabito = 1;

    Map<String, Object> respuesta =
      this.controladorHabitos.agregarChecklist(idHabito, "Tomar agua");

    assertThat(respuesta.get("status"), equalTo("success"));
    assertThat(respuesta.get("mensaje"), equalTo("Checklist agregado correctamente"));

    verify(this.servicioHabitoMock, times(1))
      .agregarItemChecklistAlHabito(any(ItemChecklist.class), eq(idHabito));
  }

  @Test
  public void agregarChecklistDeberiaRetornarErrorCuandoNoExisteElHabito() throws Exception {
    Integer idHabito = 1;

    doThrow(new HabitoNoEncontradoException())
      .when(this.servicioHabitoMock)
      .agregarItemChecklistAlHabito(any(ItemChecklist.class), eq(idHabito));

    Map<String, Object> respuesta =
      this.controladorHabitos.agregarChecklist(idHabito, "Tomar agua");

    assertThat(respuesta.get("status"), equalTo("error"));
    assertThat(respuesta.get("mensaje"), equalTo("No se pudo agregar el checklist"));

    verify(this.servicioHabitoMock, times(1))
      .agregarItemChecklistAlHabito(any(ItemChecklist.class), eq(idHabito));
  }

  @Test
  public void eliminarChecklistDeberiaRetornarMensajeCuandoSeEliminaCorrectamente()
    throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    Map<String, Object> respuesta = this.controladorHabitos.eliminarChecklist(idHabito, idItem);

    assertThat(respuesta.get("status"), equalTo("success"));
    assertThat(respuesta.get("mensaje"), equalTo("Checklist eliminado"));

    verify(this.servicioHabitoMock, times(1)).eliminarItemChecklistDelHabito(idHabito, idItem);
  }

  @Test
  public void eliminarChecklistDeberiaRetornarErrorCuandoNoExisteElHabito() throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    doThrow(new HabitoNoEncontradoException())
      .when(this.servicioHabitoMock)
      .eliminarItemChecklistDelHabito(idHabito, idItem);

    Map<String, Object> respuesta = this.controladorHabitos.eliminarChecklist(idHabito, idItem);

    assertThat(respuesta.get("status"), equalTo("error"));
    assertThat(respuesta.get("error"), equalTo("No se encontró el hábito"));

    verify(this.servicioHabitoMock, times(1)).eliminarItemChecklistDelHabito(idHabito, idItem);
  }

  @Test
  public void eliminarChecklistDeberiaRetornarErrorCuandoNoExisteElItem() throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    doThrow(new ItemChecklistNoEncontradoException())
      .when(this.servicioHabitoMock)
      .eliminarItemChecklistDelHabito(idHabito, idItem);

    Map<String, Object> respuesta = this.controladorHabitos.eliminarChecklist(idHabito, idItem);

    assertThat(respuesta.get("status"), equalTo("error"));
    assertThat(respuesta.get("error"), equalTo("No se encontró el item"));

    verify(this.servicioHabitoMock, times(1)).eliminarItemChecklistDelHabito(idHabito, idItem);
  }

  @Test
  public void alternarEstadoChecklistDeberiaRetornarSuccessCuandoActualizaCorrectamente()
    throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    Map<String, Object> respuesta =
      this.controladorHabitos.alternarEstadoDelChecklist(idHabito, idItem);

    assertThat(respuesta.get("status"), equalTo("success"));
    assertThat(respuesta.get("mensaje"), equalTo("Estado actualizado"));

    verify(this.servicioHabitoMock, times(1)).actualizarEstadoItemChecklist(idItem, idHabito);
  }

  @Test
  public void alternarEstadoChecklistDeberiaRetornarErrorCuandoNoExisteElItem() throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    doThrow(new ItemChecklistNoEncontradoException())
      .when(this.servicioHabitoMock)
      .actualizarEstadoItemChecklist(idItem, idHabito);

    Map<String, Object> respuesta =
      this.controladorHabitos.alternarEstadoDelChecklist(idHabito, idItem);

    assertThat(respuesta.get("status"), equalTo("error"));
    assertThat(respuesta.get("mensaje"), equalTo("No se pudo actualizar el estado"));

    verify(this.servicioHabitoMock, times(1)).actualizarEstadoItemChecklist(idItem, idHabito);
  }

  @Test
  public void editarChecklistDeberiaRetornarSuccessCuandoEditaCorrectamente() throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    Map<String, Object> respuesta =
      this.controladorHabitos.editarChecklist(idHabito, idItem, "Nueva descripción");

    assertThat(respuesta.get("status"), equalTo("success"));
    assertThat(respuesta.get("mensaje"), equalTo("Checklist editado correctamente"));

    verify(this.servicioHabitoMock, times(1))
      .editarDescripcionItemChecklist(idItem, idHabito, "Nueva descripción");
  }

  @Test
  public void editarChecklistDeberiaRetornarErrorCuandoLaDescripcionEsInvalida() throws Exception {
    Integer idHabito = 1;
    Integer idItem = 10;

    doThrow(new DescripcionChecklistInvalidaException())
      .when(this.servicioHabitoMock)
      .editarDescripcionItemChecklist(idItem, idHabito, "");

    Map<String, Object> respuesta = this.controladorHabitos.editarChecklist(idHabito, idItem, "");

    assertThat(respuesta.get("status"), equalTo("error"));
    assertThat(respuesta.get("mensaje"), equalTo("No se pudo editar el checklist"));

    verify(this.servicioHabitoMock, times(1)).editarDescripcionItemChecklist(idItem, idHabito, "");
  }

  @Test
  public void obtenerHabitoDeberiaRetornarMapaConDatosDelHabito() throws Exception {
    Integer idHabito = 1;

    Habito habito = new Habito();
    habito.setId(idHabito);
    habito.setTitulo("Leer");
    habito.setDescripcion("Leer 10 páginas");
    habito.setFrecuencia("Diaria");
    habito.setDuracionEstimada(20);
    habito.setCantidadDeChecklist(new ArrayList<>());

    ItemChecklist item = new ItemChecklist();
    item.setId(10);
    item.setDescripcion("Buscar libro");
    item.setEstadoChecklist(false);

    habito.agregarItemChecklist(item);

    when(this.servicioHabitoMock.buscarHabitoPorId(idHabito)).thenReturn(habito);

    Map<String, Object> respuesta = this.controladorHabitos.obtenerHabito(idHabito);

    assertThat(respuesta.get("titulo"), equalTo("Leer"));
    assertThat(respuesta.get("descripcion"), equalTo("Leer 10 páginas"));
    assertThat(respuesta.get("frecuencia"), equalTo("Diaria"));
    assertThat(respuesta.get("duracionEstimada"), equalTo(20));
    assertThat(respuesta.get("checklist"), instanceOf(List.class));

    List checklist = (List) respuesta.get("checklist");

    assertThat(checklist.size(), is(1));

    Map itemJson = (Map) checklist.get(0);

    assertThat(itemJson.get("id"), equalTo(10));
    assertThat(itemJson.get("descripcion"), equalTo("Buscar libro"));
    assertThat(itemJson.get("estadoChecklist"), equalTo(false));

    verify(this.servicioHabitoMock, times(1)).buscarHabitoPorId(idHabito);
  }
}
