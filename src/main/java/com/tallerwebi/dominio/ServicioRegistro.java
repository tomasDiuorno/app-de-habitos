package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CamposObligatorios;
import com.tallerwebi.dominio.excepcion.ContraseniasNoCoincidenException;
import com.tallerwebi.dominio.excepcion.FormatoEmailInvalido;
import com.tallerwebi.dominio.excepcion.PasswordInvalido;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosRegistro;

public interface ServicioRegistro {
  void registrar(Usuario usuario) throws UsuarioExistente;
  void validarCamposObligatorios(DatosRegistro datos) throws CamposObligatorios;
  void validarCreedenciales(DatosRegistro datos) throws FormatoEmailInvalido, PasswordInvalido;
  void registrar(DatosRegistro datos) throws UsuarioExistente;
  void registrarHabitos(DatosRegistro datos);
  void validarSiLasContraseniasSonIguales(DatosRegistro datos) throws ContraseniasNoCoincidenException;
}
