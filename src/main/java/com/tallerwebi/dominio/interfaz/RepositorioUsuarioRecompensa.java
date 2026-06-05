package com.tallerwebi.dominio.interfaz;

import java.util.List;

import com.tallerwebi.dominio.entidades.Recompensa;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioRecompensa;

public interface RepositorioUsuarioRecompensa {
  void guardar(UsuarioRecompensa usuarioRecompensa);
  List<UsuarioRecompensa> obtenerPorUsuario(Integer idUsuario);
  Boolean existeRecompensaUsuario(Recompensa recompensa, Usuario usuario);
  void utilizar(Integer idUsuarioRecompensa);
}
