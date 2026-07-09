package com.tallerwebi.dominio.entidades;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class PublicacionTest {

  @Test
  public void unaPublicacionDeberiaPoderAsociarseAUnHabito() {
    Publicacion publicacion = new Publicacion();
    Habito habito = new Habito();
    habito.setTitulo("Meditar");

    publicacion.setHabitoAsociado(habito);

    assertThat(publicacion.getHabitoAsociado(), is(habito));
  }

  @Test
  public void unaPublicacionSinHabitoAsociadoDeberiaSerNull() {
    Publicacion publicacion = new Publicacion();

    assertThat(publicacion.getHabitoAsociado(), is((Habito) null));
  }
}
