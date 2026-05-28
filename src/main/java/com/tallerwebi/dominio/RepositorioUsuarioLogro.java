package com.tallerwebi.dominio;

public interface RepositorioUsuarioLogro {
  void guardar(UsuarioLogro usuarioLogro);
  UsuarioLogro buscarPorUsuarioYLogro(Usuario usuario, Logro logro);
}
