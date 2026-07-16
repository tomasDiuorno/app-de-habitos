package com.tallerwebi.presentacion.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BarraExperienciaDTO {

  private Integer nivel;
  private Integer experienciaActual;
  private Integer experienciaNecesaria;
  private Integer porcentaje;
}
