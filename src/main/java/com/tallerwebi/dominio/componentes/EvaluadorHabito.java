package com.tallerwebi.dominio.componentes;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;

public interface EvaluadorHabito {
  ResultadoEvaluacionDTO evaluar(Habito habito, String evidencia);
}
