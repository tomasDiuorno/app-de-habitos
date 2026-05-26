package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class DatosRegistroTest {

  @Test
  public void validacionDeDatosRegistro() {
    DatosRegistro datos = new DatosRegistro();

    datos.setEmail("test@gmail.com");
    datos.setPassword("123");
    datos.setName("Pedrito");
    datos.setSurname("Perez");
    datos.setGender("Masculino");
    datos.setUsername("pedritoPe");

    assertThat(datos.getEmail(), is("test@gmail.com"));
    assertThat(datos.getPassword(), is("123"));
    assertThat(datos.getName(), is("Pedrito"));
    assertThat(datos.getSurname(), is("Perez"));
    assertThat(datos.getGender(), is("Masculino"));
    assertThat(datos.getUsername(), is("pedritoPe"));
  }
}
