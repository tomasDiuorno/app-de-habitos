package com.tallerwebi.presentacion;

public class DatosLogin {

  private String emailorusername;
  private String password;

  public DatosLogin(String emailorusername, String password) {
    this.emailorusername = emailorusername;
    this.password = password;
  }

  public DatosLogin() {}

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmailorusername() {
    return emailorusername;
  }

  public void setEmailorusername(String emailorusername) {
    this.emailorusername = emailorusername;
  }
}
