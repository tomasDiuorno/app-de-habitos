package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UsuarioTest {

  @Test
  public void queSePuedanAsignarValoresCorrectamente() {
    Usuario usuario = new Usuario();
    usuario.setId(1);
    usuario.setName("Tomas");
    usuario.setUsername("Tomidiu");
    usuario.setEmail("tom.diuorno@example.com");
    usuario.setPassword("password123");
    usuario.setRol("USER");
    usuario.activar();
    usuario.setGender("Masculino");

    assertThat(usuario.getId(), is(1));
    assertThat(usuario.getName(), is("Tomas"));
    assertThat(usuario.getUsername(), is("Tomidiu"));
    assertThat(usuario.getEmail(), is("tom.diuorno@example.com"));
    assertThat(usuario.getPassword(), is("password123"));
    assertThat(usuario.getRol(), is("USER"));
    assertThat(usuario.getActivo(), is(true));
    assertThat(usuario.getGender(), is("Masculino"));
  }

  @Test
  public void deberiaSetearYObtenerUsuarioHabitos() {
    Usuario usuario = new Usuario();

    UsuarioHabito usuarioHabito = new UsuarioHabito();
    Habito habito = new Habito();
    habito.setTitulo("Meditar");

    usuarioHabito.setHabito(habito);
    usuarioHabito.setUsuario(usuario);

    List<UsuarioHabito> usuarioHabitos = new ArrayList<>();
    usuarioHabitos.add(usuarioHabito);

    usuario.setUsuarioHabito(usuarioHabitos);

    assertThat(usuario.getUsuarioHabito(), is(usuarioHabitos));
    assertThat(usuario.getUsuarioHabito().size(), is(1));
    assertThat(usuario.getUsuarioHabito().get(0).getHabito().getTitulo(), is("Meditar"));
  }
}
