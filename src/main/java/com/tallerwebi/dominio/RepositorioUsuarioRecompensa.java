package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioUsuarioRecompensa {
  void guardar(UsuarioRecompensa usuarioRecompensa);
  List<UsuarioRecompensa> obtenerPorUsuario(Integer idUsuario);
  Boolean existeRecompensaUsuario(Recompensa recompensa, Usuario usuario);
}
