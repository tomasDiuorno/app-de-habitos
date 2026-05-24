package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CamposObligatorios;
import com.tallerwebi.dominio.excepcion.FormatoEmailInvalido;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosRegistro;

public interface ServicioRegistro {
  void registrar(Usuario usuario) throws UsuarioExistente;
  void validarCamposObligatorios(Usuario usuario) throws CamposObligatorios;
  void validarEmail(Usuario usuario) throws FormatoEmailInvalido;
  void registrar(DatosRegistro datos) throws UsuarioExistente;
  void registrarHabitos(DatosRegistro datos);
}
