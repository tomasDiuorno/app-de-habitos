package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.enums.TipoHabitoEnum;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;

public interface ServicioEvaluadorTipoHabito {
  TipoHabitoEnum getTipoHabito();
  ResultadoEvaluacionDTO evaluar(Habito habito, String evidencia);
}
