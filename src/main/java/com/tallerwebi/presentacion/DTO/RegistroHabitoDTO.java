package com.tallerwebi.presentacion.DTO;

import com.tallerwebi.dominio.enums.TipoHabitoEnum;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroHabitoDTO {

  private String titulo;

  private String descripcion;

  private String frecuencia;

  private Integer categoriaId;

  private TipoHabitoEnum tipoHabito;

  private Integer objetivoNumerico;

  private String unidadObjetivo;

  private LocalTime horaLimite;
}
