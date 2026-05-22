package com.tallerwebi.dominio;

public interface RepositorioUsuario {
  Usuario buscarUsuario(String email, String password);
  void guardar(Usuario usuario);
  Usuario buscarPorEmail(String email);
  Usuario buscarPorEmailOrUsername(String emailorusername);
  void modificar(Usuario usuario);
}
