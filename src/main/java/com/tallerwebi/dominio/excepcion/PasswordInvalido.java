package com.tallerwebi.dominio.excepcion;

public class PasswordInvalido extends Exception {

  private static final long serialVersionUID = 1L;

  public PasswordInvalido(String mensaje) {
    super(mensaje);
  }
}
