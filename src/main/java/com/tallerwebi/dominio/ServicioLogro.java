package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioLogro {
  void verificarYAsignarLogros(Usuario usuario);
  List<UsuarioLogro> obtenerLogrosDeUsuario(Usuario usuario);
}
