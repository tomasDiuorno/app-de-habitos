package com.tallerwebi.dominio.excepcion;

public class UsuarioYaUnidoAHabitoException extends Exception {

  private static final long serialVersionUID = 1L;

  public UsuarioYaUnidoAHabitoException() {
    super("El usuario ya está unido a este hábito grupal");
  }
}
