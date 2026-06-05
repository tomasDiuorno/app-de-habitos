package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioLogro;
import java.util.List;

public interface ServicioLogro {
  void verificarYAsignarLogros(Usuario usuario);
  List<UsuarioLogro> obtenerLogrosDeUsuario(Usuario usuario);
}
