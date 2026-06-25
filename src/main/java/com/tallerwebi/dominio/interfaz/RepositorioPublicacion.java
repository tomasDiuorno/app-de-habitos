package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Publicacion;
import java.util.List;

public interface RepositorioPublicacion {
  void guardar(Publicacion publicacion);

  Publicacion buscarPorId(Integer id);

  List<Publicacion> obtenerTodas();
}
