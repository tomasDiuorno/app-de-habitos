package com.tallerwebi.dominio.servicios;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tallerwebi.presentacion.DTO.HabitoSugeridoDTO;
import com.tallerwebi.presentacion.DTO.PlanHabitoDTO;

public interface ServicioHabitoIA {
  HabitoSugeridoDTO recomendar(String objetivo) throws JsonProcessingException;
  PlanHabitoDTO sugerirPlan(String nombreHabito) throws JsonProcessingException;
}
