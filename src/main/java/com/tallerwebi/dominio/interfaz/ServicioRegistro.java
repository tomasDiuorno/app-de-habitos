package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DTO.RegistroDTO;

public interface ServicioRegistro {
  void registrar(RegistroDTO datos) throws UsuarioExistente;
}
