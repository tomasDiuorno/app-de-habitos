package com.tallerwebi.presentacion.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

  private String emailorusername;
  private String password;

  public LoginDTO(String emailorusername, String password) {
    this.emailorusername = emailorusername;
    this.password = password;
  }

  public LoginDTO() {}
}
