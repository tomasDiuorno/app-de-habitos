package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class DatosRecuperacionTest {

  @Test
  public void validacionDeDatosRecuperacionContrasenia() {
    DatosRecuperacionContrasenia datos = new DatosRecuperacionContrasenia(
      "hola@gmail.com",
      "pedrito123",
      "pedrito123"
    );

    assertThat(datos.getEmail(), is("hola@gmail.com"));
    assertThat(datos.getContrasenia1(), is("pedrito123"));
    assertThat(datos.getContrasenia2(), is("pedrito123"));
  }
}
