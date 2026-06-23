package com.tallerwebi.dominio.factory;

import com.tallerwebi.dominio.componentes.EvaluadorCantidad;
import com.tallerwebi.dominio.componentes.EvaluadorHabito;
import com.tallerwebi.dominio.componentes.EvaluadorHorario;
import com.tallerwebi.dominio.enums.TipoHabitoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EvaluadorHabitosFactory {

  @Autowired
  private EvaluadorCantidad cantidad;

  @Autowired
  private EvaluadorHorario horario;

  public EvaluadorHabito obtener(TipoHabitoEnum tipo) {
    switch (tipo) {
      case HORARIO:
        return horario;
      case CANTIDAD:
        return cantidad;
      default:
        throw new IllegalArgumentException("Tipo de hábito no soportado: " + tipo);
    }
  }
}
