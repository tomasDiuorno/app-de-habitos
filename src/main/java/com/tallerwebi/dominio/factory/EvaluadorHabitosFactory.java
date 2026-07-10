package com.tallerwebi.dominio.factory;

import com.tallerwebi.dominio.enums.TipoHabitoEnum;
import com.tallerwebi.dominio.interfaz.ServicioEvaluadorTipoHabito;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EvaluadorHabitosFactory {
  private final Map<TipoHabitoEnum, ServicioEvaluadorTipoHabito> evaluadores;

  public EvaluadorHabitosFactory(List<ServicioEvaluadorTipoHabito> evaluadores) {
    this.evaluadores = evaluadores.stream().collect(Collectors.toMap(ServicioEvaluadorTipoHabito::getTipoHabito, Function.identity()));
  }

  public ServicioEvaluadorTipoHabito obtener(TipoHabitoEnum tipo) {
    ServicioEvaluadorTipoHabito evaluador = this.evaluadores.get(tipo);
    if(evaluador == null) {
      throw new IllegalArgumentException("No se encontró un evaluador para el tipo de hábito: " + tipo);
    }
    return evaluador;
  }
}
