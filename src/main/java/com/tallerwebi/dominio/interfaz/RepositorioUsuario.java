package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Usuario;

public interface RepositorioUsuario {
  Usuario buscarUsuario(String email, String password);
  void guardar(Usuario usuario);
  Usuario buscarPorEmailOrUsername(String emailorusername);
  void modificar(Usuario usuario);
}
