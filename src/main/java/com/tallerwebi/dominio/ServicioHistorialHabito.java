package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.HabitoNoPerteneceAlUsuarioException;
import com.tallerwebi.dominio.excepcion.HabitoYaCompletadoHoyException;
import java.util.List;

public interface ServicioHistorialHabito {
  void marcarHabitoComoCompletado(Usuario usuario, Integer habitoId)
    throws HabitoNoPerteneceAlUsuarioException, HabitoYaCompletadoHoyException;
  List<HistorialHabito> obtenerHistorial(Usuario usuario);
}
