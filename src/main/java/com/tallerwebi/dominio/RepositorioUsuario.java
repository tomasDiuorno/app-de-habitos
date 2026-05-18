package com.tallerwebi.dominio;

public interface RepositorioUsuario {
  Usuario buscarUsuario(String email, String username);
  void guardar(Usuario usuario);
  Usuario buscarPorEmail(String email);
  void modificar(Usuario usuario);
}
