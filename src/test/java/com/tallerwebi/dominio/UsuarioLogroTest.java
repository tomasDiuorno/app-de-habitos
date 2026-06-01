package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class UsuarioLogroTest {

  @Test
  public void unUsuarioLogroDeberiaRelacionarUnUsuarioConUnLogro() {
    Usuario usuario = new Usuario();
    usuario.setEmail("test@mail.com");

    Logro logro = new Logro();
    logro.setNombre("Primer habito");

    UsuarioLogro usuarioLogro = new UsuarioLogro();

    usuarioLogro.setUsuario(usuario);
    usuarioLogro.setLogro(logro);

    assertThat(usuarioLogro.getUsuario(), is(usuario));
    assertThat(usuarioLogro.getLogro(), is(logro));
  }
}
