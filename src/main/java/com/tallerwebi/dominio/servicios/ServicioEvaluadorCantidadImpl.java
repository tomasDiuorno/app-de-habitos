package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.enums.TipoHabitoEnum;
import com.tallerwebi.dominio.interfaz.ServicioEvaluadorTipoHabito;
import com.tallerwebi.presentacion.DTO.EvidenciaDTO;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;
import org.springframework.stereotype.Service;

@Service("evaluadorCantidad")
public class ServicioEvaluadorCantidadImpl implements ServicioEvaluadorTipoHabito {

  @Override
  public ResultadoEvaluacionDTO evaluar(Habito habito, EvidenciaDTO evidencia) {
    Integer cantidad = Integer.parseInt(evidencia.getTexto());
    Boolean cumplio = cantidad >= habito.getConfiguracion().getObjetivoNumero();

    return new ResultadoEvaluacionDTO(cumplio, "Cantidad realizada: " + cantidad);
  }

  @Override
  public TipoHabitoEnum getTipoHabito() {
    return TipoHabitoEnum.CANTIDAD;
  }
}
