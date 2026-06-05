package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import com.tallerwebi.presentacion.DTO.RegistroDTO;

public class DatosRecuperacionTest {

  @Test
  public void deberiaConfirmarPasswordCuandoCoinciden() {
    RegistroDTO datos = new RegistroDTO();

    datos.setPassword("Password1!");
    datos.setConfirmPassword("Password1!");

    assertThat(datos.isPasswordConfirmada(), is(true));
  }

  @Test
  public void noDeberiaConfirmarPasswordCuandoNoCoinciden() {
    RegistroDTO datos = new RegistroDTO();

    datos.setPassword("Password1!");
    datos.setConfirmPassword("Otra1!");

    assertThat(datos.isPasswordConfirmada(), is(false));
  }

  @Test
  public void noDeberiaConfirmarPasswordCuandoAlgunaEsNull() {
    RegistroDTO datos = new RegistroDTO();

    datos.setPassword("Password1!");
    datos.setConfirmPassword(null);

    assertThat(datos.isPasswordConfirmada(), is(false));
  }
}
