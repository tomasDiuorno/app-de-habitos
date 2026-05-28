package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class DatosRecuperacionTest {

  @Test
  public void deberiaConfirmarPasswordCuandoCoinciden() {
    DatosRegistro datos = new DatosRegistro();

    datos.setPassword("Password1!");
    datos.setConfirmPassword("Password1!");

    assertThat(datos.isPasswordConfirmada(), is(true));
  }

  @Test
  public void noDeberiaConfirmarPasswordCuandoNoCoinciden() {
    DatosRegistro datos = new DatosRegistro();

    datos.setPassword("Password1!");
    datos.setConfirmPassword("Otra1!");

    assertThat(datos.isPasswordConfirmada(), is(false));
  }

  @Test
  public void noDeberiaConfirmarPasswordCuandoAlgunaEsNull() {
    DatosRegistro datos = new DatosRegistro();

    datos.setPassword("Password1!");
    datos.setConfirmPassword(null);

    assertThat(datos.isPasswordConfirmada(), is(false));
  }
}
