package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class UsuarioTest {

  @Test
  public void queSePuedanAsignarValoresCorrectamente() {
    Usuario usuario = new Usuario();
    usuario.setId(1);
    usuario.setName("Tomas");
    usuario.setSurname("Diuorno");
    usuario.setUsername("Tomidiu");
    usuario.setEmail("tom.diuorno@example.com");
    usuario.setPassword("password123");
    usuario.setRol("USER");
    usuario.activar();
    usuario.setGender("Masculino");

    assertThat(usuario.getId(), is(1));
    assertThat(usuario.getName(), is("Tomas"));
    assertThat(usuario.getSurname(), is("Diuorno"));
    assertThat(usuario.getUsername(), is("Tomidiu"));
    assertThat(usuario.getEmail(), is("tom.diuorno@example.com"));
    assertThat(usuario.getPassword(), is("password123"));
    assertThat(usuario.getRol(), is("USER"));
    assertThat(usuario.getActivo(), is(true));
    assertThat(usuario.getGender(), is("Masculino"));

  }
}
