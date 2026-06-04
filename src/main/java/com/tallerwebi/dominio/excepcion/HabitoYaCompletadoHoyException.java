package com.tallerwebi.dominio.excepcion;

public class HabitoYaCompletadoHoyException extends Exception {
  public HabitoYaCompletadoHoyException() {
    super("Ya completaste este hábito hoy.");
  }
}
