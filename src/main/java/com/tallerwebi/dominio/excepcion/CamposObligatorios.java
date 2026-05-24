package com.tallerwebi.dominio.excepcion;

public class CamposObligatorios extends Exception {

  private static final long serialVersionUID = 1L;

  public CamposObligatorios(String mensaje) {
    super(mensaje);
  }
}
