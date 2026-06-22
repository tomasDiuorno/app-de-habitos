package com.tallerwebi.presentacion.DTO;

public class ResultadoEvaluacionDTO {

  private Boolean cumplido;
  private String detalle;

  public ResultadoEvaluacionDTO(Boolean cumplido, String detalle) {
    this.cumplido = cumplido;
    this.detalle = detalle;
  }

  public Boolean estaCumplido() {
    return cumplido;
  }

  public String getDetalle() {
    return detalle;
  }
}
