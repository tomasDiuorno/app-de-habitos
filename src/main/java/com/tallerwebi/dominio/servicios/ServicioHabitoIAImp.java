package com.tallerwebi.dominio.servicios;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.interfaz.ServicioIA;
import com.tallerwebi.presentacion.DTO.HabitoSugeridoDTO;
import com.tallerwebi.presentacion.DTO.PlanHabitoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioHabitoIA")
public class ServicioHabitoIAImp implements ServicioHabitoIA {

  private final ServicioIA servicioIa;
  private final ObjectMapper mapper;

  @Autowired
  public ServicioHabitoIAImp(ServicioIA servicioIA, ObjectMapper mapper) {
    this.servicioIa = servicioIA;
    this.mapper = mapper;
  }

  @Override
  public HabitoSugeridoDTO recomendar(String objetivo) throws JsonProcessingException {
    String prompt = construirPrompt(objetivo);
    String respuesta = servicioIa.preguntar(prompt);
    return convertirRespuestaIA(respuesta);
  }

  private HabitoSugeridoDTO convertirRespuestaIA(String json) {
    try {
      String jsonLimpio = json.replace("```json", "").replace("```", "").trim();
      return mapper.readValue(jsonLimpio, HabitoSugeridoDTO.class);
    } catch (Exception e) {
      throw new RuntimeException("No se pudo convertir la respues correctamente " + json, e);
    }
  }

  private String construirPrompt(String objetivo) {
    return String.format(objetivo, "El usuario quiere: %s. Creá un habito recomendado.");
  }

  @Override
  public PlanHabitoDTO sugerirPlan(String nombreHabito) throws JsonProcessingException {
    String prompt = String.format(
      "Genera un plan paso a paso (baby steps) en español para completar el hábito '%s'. Devuelve estrictamente un JSON con la estructura {\"pasos\": [\"paso 1\", \"paso 2\"]}",
      nombreHabito
    );
    String respuesta = servicioIa.preguntar(prompt);
    return convertirRespuestaPlanIA(respuesta);
  }

  private PlanHabitoDTO convertirRespuestaPlanIA(String json) {
    try {
      String jsonLimpio = json.replace("```json", "").replace("```", "").trim();
      return mapper.readValue(jsonLimpio, PlanHabitoDTO.class);
    } catch (Exception e) {
      throw new RuntimeException("No se pudo convertir la respuesta correctamente " + json, e);
    }
  }
}
