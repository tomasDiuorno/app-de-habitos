package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Bonificacion;
import java.util.List;

public interface RepositorioBonificacion {
  List<Bonificacion> buscarDisponibles();
  Bonificacion buscarPorId(Integer id);
  void guardar(Bonificacion bonificacion);
}
