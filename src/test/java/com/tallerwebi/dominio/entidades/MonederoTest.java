package com.tallerwebi.dominio.entidades;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class MonederoTest {

  @Test
  public void elSaldoInicialDeUnMonederoDeberiaSerCero() {
    Monedero monedero = new Monedero();
    assertThat(monedero.getSaldo(), is(equalTo(0)));
  }

  @Test
  public void deberiaAsociarUnUsuarioAlMonedero() {
    Usuario usuario = new Usuario();
    usuario.setEmail("test@mail.com");

    Monedero monedero = new Monedero();
    monedero.setUsuario(usuario);

    assertThat(monedero.getUsuario().getEmail(), is("test@mail.com"));
  }

  @Test
  public void elMonederoDeberiaIniciarSinTransacciones() {
    Monedero monedero = new Monedero();
    assertThat(monedero.getTransacciones().size(), is(0));
  }
}
