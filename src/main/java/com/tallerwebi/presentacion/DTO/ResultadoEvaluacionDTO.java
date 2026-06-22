package com.tallerwebi.presentacion.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultadoEvaluacionDTO {

  private Boolean cumplido;
  private String detalle;

  public ResultadoEvaluacionDTO(Boolean cumplido, String detalle) {
    this.cumplido = cumplido;
    this.detalle = detalle;
  }
}
