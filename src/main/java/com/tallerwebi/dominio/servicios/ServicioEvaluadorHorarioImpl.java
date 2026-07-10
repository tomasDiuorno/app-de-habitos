package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.enums.TipoHabitoEnum;
import com.tallerwebi.dominio.interfaz.ServicioEvaluadorTipoHabito;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;
import java.time.LocalTime;
import org.springframework.stereotype.Service;

@Service("evaluadorHorario")
public class ServicioEvaluadorHorarioImpl implements ServicioEvaluadorTipoHabito {

  @Override
  public ResultadoEvaluacionDTO evaluar(Habito habito, String evidencia) {
    LocalTime horaReal = LocalTime.parse(evidencia);
    Boolean cumplio = horaReal.isBefore(habito.getConfiguracion().getHoraLimite());

    return new ResultadoEvaluacionDTO(cumplio, "Hora registrada" + horaReal);
  }

  @Override
  public TipoHabitoEnum getTipoHabito() {
    return TipoHabitoEnum.HORARIO;
  }
}
