package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.servicios.ServicioHabitoIA;
import com.tallerwebi.presentacion.DTO.HabitoObjetivoDTO;
import com.tallerwebi.presentacion.DTO.HabitoSugeridoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/habitos")
public class ControladorHabitoIA {

  private final ServicioHabitoIA servicioHabitoIA;

  @Autowired
  public ControladorHabitoIA(ServicioHabitoIA servicioHabitoIA) {
    this.servicioHabitoIA = servicioHabitoIA;
  }

  @PostMapping("/recomendar")
  public ResponseEntity<?> preguntar(@RequestBody HabitoObjetivoDTO objetivo) {
    HabitoSugeridoDTO sugerencia;
    try {
      sugerencia = servicioHabitoIA.recomendar(objetivo.getObjetivo());
      return ResponseEntity.ok(sugerencia);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(e.getMessage());
    }
  }
}
