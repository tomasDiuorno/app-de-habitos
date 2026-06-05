package com.tallerwebi.presentacion.DTO;

public class LoginDTO {

  private String emailorusername;
  private String password;

  public LoginDTO(String emailorusername, String password) {
    this.emailorusername = emailorusername;
    this.password = password;
  }

  public LoginDTO() {}

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
