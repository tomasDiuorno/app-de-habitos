package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CamposObligatorios;
import com.tallerwebi.dominio.excepcion.FormatoEmailInvalido;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServicioRegistro {
  void registrar(Usuario usuario) throws UsuarioExistente;
  void validarCamposObligatorios(Usuario usuario) throws CamposObligatorios;
  void validarEmail(Usuario usuario) throws FormatoEmailInvalido;
 
  
}
