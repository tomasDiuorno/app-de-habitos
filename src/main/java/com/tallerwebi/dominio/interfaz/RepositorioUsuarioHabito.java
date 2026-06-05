package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.UsuarioHabito;

public interface RepositorioUsuarioHabito {
  void guardar(UsuarioHabito usuarioHabito);
  UsuarioHabito obtenerPorIds(Integer usuarioId, Integer habitoId);
}
