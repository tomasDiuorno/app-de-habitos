package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServicioRegistro {
  void registrar(Usuario usuario) throws UsuarioExistente;
}
