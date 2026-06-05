package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Recompensa;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioRecompensa;
import java.util.List;

public interface RepositorioUsuarioRecompensa {
  void guardar(UsuarioRecompensa usuarioRecompensa);
  List<UsuarioRecompensa> obtenerPorUsuario(Integer idUsuario);
  Boolean existeRecompensaUsuario(Recompensa recompensa, Usuario usuario);
  void utilizar(Integer idUsuarioRecompensa);
}
