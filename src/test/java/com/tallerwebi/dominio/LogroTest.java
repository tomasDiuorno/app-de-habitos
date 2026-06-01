package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class LogroTest {

  @Test
  public void deberiaAsignarCorrectamenteLosValoresDelLogro() {
    Logro logro = new Logro();

    logro.setId(1);
    logro.setNombre("Primer habito");
    logro.setDescripcion("Crear un habito");

    assertThat(logro.getId(), is(1));
    assertThat(logro.getNombre(), is("Primer habito"));
    assertThat(logro.getDescripcion(), is("Crear un habito"));
  }
}
