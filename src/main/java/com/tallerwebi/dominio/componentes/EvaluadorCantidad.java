package com.tallerwebi.dominio.componentes;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;
import org.springframework.stereotype.Component;

@Component
public class EvaluadorCantidad implements EvaluadorHabito {

  @Override
  public ResultadoEvaluacionDTO evaluar(Habito habito, String evidencia) {
    Integer cantidad = Integer.parseInt(evidencia);
    Boolean cumplio = cantidad >= habito.getConfiguracion().getObjetivoNumero();

    return new ResultadoEvaluacionDTO(cumplio, "Cantidad realizada: " + cantidad);
  }
}
