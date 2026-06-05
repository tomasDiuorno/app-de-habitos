package com.tallerwebi.dominio.interfaz;

import java.util.List;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioLogro;

public interface ServicioLogro {
  void verificarYAsignarLogros(Usuario usuario);
  List<UsuarioLogro> obtenerLogrosDeUsuario(Usuario usuario);
}
