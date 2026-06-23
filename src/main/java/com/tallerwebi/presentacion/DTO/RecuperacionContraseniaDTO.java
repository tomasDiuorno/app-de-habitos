package com.tallerwebi.presentacion.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecuperacionContraseniaDTO {

  private String email;
  private String contrasenia1;
  private String contrasenia2;

  public RecuperacionContraseniaDTO(String email, String contrasenia1, String contrasenia2) {
    this.email = email;
    this.contrasenia1 = contrasenia1;
    this.contrasenia2 = contrasenia2;
  }
}
