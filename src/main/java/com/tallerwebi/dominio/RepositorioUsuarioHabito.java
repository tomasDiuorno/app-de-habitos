package com.tallerwebi.dominio;

public interface RepositorioUsuarioHabito {
  void guardar(UsuarioHabito usuarioHabito);
  UsuarioHabito obtenerPorIds(Integer usuarioId, Integer habitoId);
}
