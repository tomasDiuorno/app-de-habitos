package com.tallerwebi.dominio.componentes;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class EvaluadorHorario implements EvaluadorHabito {

  @Override
  public ResultadoEvaluacionDTO evaluar(Habito habito, String evidencia) {
    LocalTime horaReal = LocalTime.parse(evidencia);
    Boolean cumplio = horaReal.isBefore(habito.getConfiguracion().getHoraLimite());

    return new ResultadoEvaluacionDTO(cumplio, "Hora registrada" + horaReal);
  }
}
