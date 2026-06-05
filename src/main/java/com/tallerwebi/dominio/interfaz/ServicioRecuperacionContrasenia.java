package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.excepcion.ContraseniasNoCoincidenException;
import com.tallerwebi.dominio.excepcion.EmailInexistenteException;
import com.tallerwebi.presentacion.DTO.RecuperacionContraseniaDTO;

public interface ServicioRecuperacionContrasenia {
  void recuperarContrasenia(RecuperacionContraseniaDTO datosRecuperacionContrasenia)
    throws ContraseniasNoCoincidenException, EmailInexistenteException;
}
