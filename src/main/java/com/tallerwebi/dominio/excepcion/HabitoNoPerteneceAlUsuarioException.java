package com.tallerwebi.dominio.excepcion;

public class HabitoNoPerteneceAlUsuarioException extends Exception {
  public HabitoNoPerteneceAlUsuarioException() {
    super("El usuario no posee este hábito activo.");
  }
}
