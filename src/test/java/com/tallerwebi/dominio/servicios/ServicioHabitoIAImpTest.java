package com.tallerwebi.dominio.servicios;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.interfaz.ServicioIA;
import com.tallerwebi.presentacion.DTO.HabitoSugeridoDTO;
import com.tallerwebi.presentacion.DTO.PlanHabitoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioHabitoIAImpTest {

  private ServicioHabitoIAImp servicioHabitoIA;
  private ServicioIA servicioIAMock;
  private ObjectMapper mapper;
  private static final String OBJETIVO = "Quiero mejorar mi alimentación";
  private static final String RESPUESTA_JSON =
    "{\"nombre\":\"Comer saludable\",\"descripcion\":\"Preparar comidas saludables diariamente\",\"frecuencia\":\"Diario\",\"categoria\":\"Salud\"}";

  @BeforeEach
  public void init() {
    this.servicioIAMock = mock(ServicioIA.class);
    this.mapper = new ObjectMapper();
    this.servicioHabitoIA = new ServicioHabitoIAImp(servicioIAMock, mapper);
  }

  @Test
  public void recomendarDeberiaRetornarHabitoSugerido() throws JsonProcessingException {
    this.dadoQueElServicioIAResponde(RESPUESTA_JSON);
    HabitoSugeridoDTO respuesta = this.cuandoRecomiendoUnHabito();
    this.entoncesElHabitoTieneLosDatosEsperados(respuesta);
  }

  @Test
  public void recomendarDeberiaLimpiarRespuestaJsonConMarkdown() throws JsonProcessingException {
    String respuestaConMarkdown = "```json" + RESPUESTA_JSON + "```";
    this.dadoQueElServicioIAResponde(respuestaConMarkdown);
    HabitoSugeridoDTO respuesta = this.cuandoRecomiendoUnHabito();
    this.entoncesElHabitoTieneLosDatosEsperados(respuesta);
  }

  @Test
  public void recomendarDeberiaLanzarErrorCuandoLaRespuestaNoEsJson()
    throws JsonProcessingException {
    this.dadoQueElServicioIAResponde("Respuesta inválida");
    try {
      this.cuandoRecomiendoUnHabito();
    } catch (RuntimeException e) {
      assertThat(
        e.getMessage(),
        equalTo("No se pudo convertir la respues correctamente Respuesta inválida")
      );
    }
  }

  private void dadoQueElServicioIAResponde(String respuesta) throws JsonProcessingException {
    when(servicioIAMock.preguntar(anyString())).thenReturn(respuesta);
  }

  private HabitoSugeridoDTO cuandoRecomiendoUnHabito() throws JsonProcessingException {
    return servicioHabitoIA.recomendar(OBJETIVO);
  }

  private void entoncesElHabitoTieneLosDatosEsperados(HabitoSugeridoDTO respuesta) {
    assertThat(respuesta.getNombre(), equalTo("Comer saludable"));
    assertThat(respuesta.getDescripcion(), equalTo("Preparar comidas saludables diariamente"));
    assertThat(respuesta.getFrecuencia(), equalTo("Diario"));
    assertThat(respuesta.getCategoria(), equalTo("Salud"));
  }

  @Test
  public void sugerirPlanDeberiaRetornarPlanHabitoDTO() throws JsonProcessingException {
    String respuestaJsonPlan = "{\"pasos\":[\"Paso 1\",\"Paso 2\"]}";
    this.dadoQueElServicioIAResponde(respuestaJsonPlan);
    PlanHabitoDTO respuesta = servicioHabitoIA.sugerirPlan("Hacer ejercicio");
    assertThat(respuesta.getPasos().size(), equalTo(2));
    assertThat(respuesta.getPasos().get(0), equalTo("Paso 1"));
    assertThat(respuesta.getPasos().get(1), equalTo("Paso 2"));
  }

  @Test
  public void sugerirPlanDeberiaManejarRespuestaInvalida() throws JsonProcessingException {
    this.dadoQueElServicioIAResponde("Invalido");
    try {
      servicioHabitoIA.sugerirPlan("Hacer ejercicio");
    } catch (RuntimeException e) {
      assertThat(e.getMessage(), equalTo("No se pudo convertir la respuesta correctamente Invalido"));
    }
  }

  @Test
  public void sugerirPlanDeberiaLimpiarRespuestaJsonConMarkdown() throws JsonProcessingException {
    String respuestaConMarkdown = "```json\n{\"pasos\":[\"Paso 1\"]}\n```";
    this.dadoQueElServicioIAResponde(respuestaConMarkdown);
    PlanHabitoDTO respuesta = servicioHabitoIA.sugerirPlan("Leer");
    assertThat(respuesta.getPasos().size(), equalTo(1));
    assertThat(respuesta.getPasos().get(0), equalTo("Paso 1"));
  }

  @Test
  public void sugerirPlanDeberiaManejarPlanConPasosVacios() throws JsonProcessingException {
    String respuestaJsonVacio = "{\"pasos\":[]}";
    this.dadoQueElServicioIAResponde(respuestaJsonVacio);
    PlanHabitoDTO respuesta = servicioHabitoIA.sugerirPlan("Meditar");
    assertThat(respuesta.getPasos().size(), equalTo(0));
  }
}
