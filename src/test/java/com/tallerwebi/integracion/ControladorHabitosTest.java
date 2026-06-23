package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.ItemChecklist;
import com.tallerwebi.dominio.ServicioCategoria;
import com.tallerwebi.dominio.ServicioHabito;
import com.tallerwebi.dominio.ServicioHistorialHabito;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.ControladorHabitos;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class ControladorHabitosTest {

  private Usuario usuarioMock;
  private ServicioHabito servicioHabitoMock;
  private ControladorHabitos controladorHabitos;
  private ServicioCategoria servicioCategoria;
  private ServicioHistorialHabito servicioHistorialHabitoMock;

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    usuarioMock = mock(Usuario.class);
    servicioHabitoMock = mock(ServicioHabito.class);
    servicioCategoria = mock(ServicioCategoria.class);
    servicioHistorialHabitoMock = mock(ServicioHistorialHabito.class);
    controladorHabitos = new ControladorHabitos(servicioHabitoMock, servicioCategoria, servicioHistorialHabitoMock);

    when(usuarioMock.getEmail()).thenReturn("test@mail.com");
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void deberiaRetornarLaPaginaHabitosCuandoNavegoAHabitos() throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/habitos").sessionAttr("usuario", usuarioMock))
        .andExpect(status().isOk())
        .andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assert modelAndView != null;
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("habitos"));
  }

  @Test
  public void deberiaRetornarLaPaginaCrearHabitoCuandoQuieroCrearUnHaibto() throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/crear-habito").sessionAttr("usuario", usuarioMock))
        .andExpect(status().isOk())
        .andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assert modelAndView != null;
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("crear-habito"));
  }

  @Test
  public void deberiaRetornarAlLoginCuandoNoHayUnUsuarioLogueado() throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/habitos")).andExpect(status().is3xxRedirection()).andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assert modelAndView != null;
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
  }

  @Test
  public void agregarChecklistDeberiaAgregarUnItemAlHabitoYResponderSuccess() throws Exception {
    String respuesta = controladorHabitos.agregarChecklist(1, "Tomar agua");

    ArgumentCaptor<ItemChecklist> captor = ArgumentCaptor.forClass(ItemChecklist.class);
    verify(servicioHabitoMock).agregarItemChecklistAlHabito(captor.capture(), eq(1));

    assertEquals("Tomar agua", captor.getValue().getDescripcion());
    assertEquals(
      "{\"status\":\"success\", \"mensaje\":\"Checklist agregado correctamente\"}",
      respuesta
    );
  }

  @Test
  public void agregarChecklistSiFallaElServicioDeberiaResponderError() throws Exception {
    doThrow(new RuntimeException())
      .when(servicioHabitoMock)
      .agregarItemChecklistAlHabito(any(ItemChecklist.class), eq(1));

    String respuesta = controladorHabitos.agregarChecklist(1, "Tomar agua");

    assertEquals(
      "{\"status\":\"error\", \"mensaje\":\"No se pudo agregar el checklist\"}",
      respuesta
    );
  }

  @Test
  public void eliminarChecklistDeberiaEliminarElItemYResponderSuccess() throws Exception {
    Habito habito = new Habito();

    ItemChecklist item = new ItemChecklist();
    item.setId(10);
    item.setDescripcion("Leer");

    habito.setCantidadDeChecklist(Arrays.asList(item));

    when(servicioHabitoMock.buscarHabitoPorId(1)).thenReturn(habito);

    String respuesta = controladorHabitos.eliminarChecklist(1, 10);

    verify(servicioHabitoMock).eliminarItemChecklistDelHabito(item, 1);
    assertEquals("{\"status\":\"success\", \"mensaje\":\"Checklist eliminado\"}", respuesta);
  }

  @Test
  public void eliminarChecklistSiElItemNoExisteDeberiaResponderError() {
    Habito habito = new Habito();

    ItemChecklist item = new ItemChecklist();
    item.setId(99);
    item.setDescripcion("Leer");

    habito.setCantidadDeChecklist(Arrays.asList(item));

    when(servicioHabitoMock.buscarHabitoPorId(1)).thenReturn(habito);

    String respuesta = controladorHabitos.eliminarChecklist(1, 10);

    assertEquals("{\"status\":\"error\", \"mensaje\":\"Item no encontrado\"}", respuesta);
  }

  @Test
  public void eliminarChecklistSiFallaElServicioDeberiaResponderError() {
    when(servicioHabitoMock.buscarHabitoPorId(1)).thenThrow(new RuntimeException());

    String respuesta = controladorHabitos.eliminarChecklist(1, 10);

    assertEquals(
      "{\"status\":\"error\", \"mensaje\":\"No se pudo eliminar el checklist\"}",
      respuesta
    );
  }

  @Test
  public void toggleChecklistDeberiaActualizarEstadoYResponderSuccess() throws Exception {
    String respuesta = controladorHabitos.toggleChecklist(1, 10);

    verify(servicioHabitoMock).actualizarEstadoItemChecklist(10, 1);
    assertEquals("{\"status\":\"success\", \"mensaje\":\"Estado actualizado\"}", respuesta);
  }

  @Test
  public void toggleChecklistSiFallaElServicioDeberiaResponderError() throws Exception {
    doThrow(new RuntimeException()).when(servicioHabitoMock).actualizarEstadoItemChecklist(10, 1);

    String respuesta = controladorHabitos.toggleChecklist(1, 10);

    assertEquals(
      "{\"status\":\"error\", \"mensaje\":\"No se pudo actualizar el estado\"}",
      respuesta
    );
  }

  @Test
  public void editarChecklistDeberiaEditarDescripcionYResponderSuccess() throws Exception {
    String respuesta = controladorHabitos.editarChecklist(1, 10, "Nueva descripcion");

    verify(servicioHabitoMock).editarDescripcionItemChecklist(10, 1, "Nueva descripcion");
    assertEquals(
      "{\"status\":\"success\", \"mensaje\":\"Checklist editado correctamente\"}",
      respuesta
    );
  }

  @Test
  public void editarChecklistSiFallaElServicioDeberiaResponderError() throws Exception {
    doThrow(new RuntimeException())
      .when(servicioHabitoMock)
      .editarDescripcionItemChecklist(10, 1, "Nueva descripcion");

    String respuesta = controladorHabitos.editarChecklist(1, 10, "Nueva descripcion");

    assertEquals(
      "{\"status\":\"error\", \"mensaje\":\"No se pudo editar el checklist\"}",
      respuesta
    );
  }

  @Test
  public void obtenerHabitoDeberiaDevolverElHabitoEnFormatoJson() {
    Habito habito = new Habito();
    habito.setTitulo("Entrenar");
    habito.setDescripcion("Ir al gimnasio");
    habito.setFrecuencia("Diaria");
    habito.setDuracionEstimada(60);

    ItemChecklist item = new ItemChecklist();
    item.setId(10);
    item.setDescripcion("Hacer cardio");
    item.setEstadoChecklist(false);

    habito.setCantidadDeChecklist(Arrays.asList(item));

    when(servicioHabitoMock.buscarHabitoPorId(1)).thenReturn(habito);

    String respuesta = controladorHabitos.obtenerHabito(1);

    assertTrue(respuesta.contains("\"titulo\":\"Entrenar\""));
    assertTrue(respuesta.contains("\"descripcion\":\"Ir al gimnasio\""));
    assertTrue(respuesta.contains("\"frecuencia\":\"Diaria\""));
    assertTrue(respuesta.contains("\"duracionEstimada\":\"60\""));
    assertTrue(respuesta.contains("\"checklist\":["));
    assertTrue(respuesta.contains("\"id\": 10"));
    assertTrue(respuesta.contains("\"descripcion\": \"Hacer cardio\""));
    assertTrue(respuesta.contains("\"estadoChecklist\": false"));
  }

  @Test
  public void obtenerHabitoDeberiaEscaparCaracteresEspecialesDelJson() {
    Habito habito = new Habito();
    habito.setTitulo("Habito con \"comillas\"");
    habito.setDescripcion("Linea 1\nLinea 2");
    habito.setFrecuencia("Diaria");
    habito.setDuracionEstimada(30);

    ItemChecklist item = new ItemChecklist();
    item.setId(5);
    item.setDescripcion("Texto con \"comillas\" y salto\n");
    item.setEstadoChecklist(true);

    habito.setCantidadDeChecklist(Arrays.asList(item));

    when(servicioHabitoMock.buscarHabitoPorId(1)).thenReturn(habito);

    String respuesta = controladorHabitos.obtenerHabito(1);

    assertTrue(respuesta.contains("Habito con \\\"comillas\\\""));
    assertTrue(respuesta.contains("Linea 1\\nLinea 2"));
    assertTrue(respuesta.contains("Texto con \\\"comillas\\\" y salto\\n"));
  }

  @Test
  public void deberiaRetornarAlLoginCuandoQuieroCrearUnHabitoPeroNoHayUnUsuarioLogueado()
    throws Exception {
    MvcResult result =
      this.mockMvc.perform(post("/crear-habito").param("nombre", "Habito de prueba"))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assert modelAndView != null;
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
  }
}
