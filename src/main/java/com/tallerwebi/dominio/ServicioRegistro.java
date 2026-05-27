package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosRegistro;

public interface ServicioRegistro {
  void registrar(DatosRegistro datos) throws UsuarioExistente;
}
