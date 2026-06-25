package com.tallerwebi.presentacion.DTO;

<<<<<<< HEAD
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ComentarioDTO {

  private String contenido;
=======
public class ComentarioDTO {

  private String contenido;

  public String getContenido() {
    return contenido;
  }

  public void setContenido(String contenido) {
    this.contenido = contenido;
  }
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
}
