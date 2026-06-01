package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioUsuarioLogro {
  void guardar(UsuarioLogro usuarioLogro);
  UsuarioLogro buscarPorUsuarioYLogro(Usuario usuario, Logro logro);
  List<UsuarioLogro> buscarPorUsuario(Integer idUsuario);
}
