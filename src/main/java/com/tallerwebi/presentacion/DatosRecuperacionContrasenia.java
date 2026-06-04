package com.tallerwebi.presentacion;

public class DatosRecuperacionContrasenia {

  private String email;
  private String contrasenia1;
  private String contrasenia2;

  public DatosRecuperacionContrasenia(String email, String contrasenia1, String contrasenia2) {
    this.email = email;
    this.contrasenia1 = contrasenia1;
    this.contrasenia2 = contrasenia2;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getContrasenia1() {
    return contrasenia1;
  }

  public void setContrasenia1(String contrasenia1) {
    this.contrasenia1 = contrasenia1;
  }

  public String getContrasenia2() {
    return contrasenia2;
  }

  public void setContrasenia2(String contrasenia2) {
    this.contrasenia2 = contrasenia2;
  }
}
