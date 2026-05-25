package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ContraseniasNoCoincidenException;
import com.tallerwebi.dominio.excepcion.EmailInexistenteException;
import com.tallerwebi.presentacion.DatosRecuperacionContrasenia;

public interface ServicioRecuperacionContrasenia {
  void recuperarContrasenia(DatosRecuperacionContrasenia datosRecuperacionContrasenia)
    throws ContraseniasNoCoincidenException, EmailInexistenteException;
}
