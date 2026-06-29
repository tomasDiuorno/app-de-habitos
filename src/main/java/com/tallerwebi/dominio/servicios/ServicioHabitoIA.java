package com.tallerwebi.dominio.servicios;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tallerwebi.presentacion.DTO.HabitoSugeridoDTO;

public interface ServicioHabitoIA {
  HabitoSugeridoDTO recomendar(String objetivo) throws JsonProcessingException;
}
