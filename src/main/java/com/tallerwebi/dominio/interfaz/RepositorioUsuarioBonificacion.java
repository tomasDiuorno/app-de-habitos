package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.UsuarioBonificacion;

public interface RepositorioUsuarioBonificacion {
  UsuarioBonificacion buscarActivaPorUsuarioId(Integer usuarioId);

  void guardar(UsuarioBonificacion usuarioBonificacion);

  void modificar(UsuarioBonificacion usuarioBonificacion);
}
