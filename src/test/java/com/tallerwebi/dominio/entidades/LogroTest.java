// src/test/java/com/tallerwebi/dominio/LogroTest.java
package com.tallerwebi.dominio.entidades;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class LogroTest {

  @Test
  public void deberiaSetearYObtenerDatosDeUnLogro() {
    Logro logro = new Logro();
    logro.setNombre("Primer Paso");
    logro.setDescripcion("Completaste tu primer hábito");
    logro.setCondicion("PRIMER_HABITO");

    assertThat(logro.getNombre(), is("Primer Paso"));
    assertThat(logro.getDescripcion(), is("Completaste tu primer hábito"));
    assertThat(logro.getCondicion(), is("PRIMER_HABITO"));
  }

  @Test
  public void deberiaSetearYObtenerUsuarioLogros() {
    Usuario usuario = new Usuario();
    usuario.setEmail("user@test.com");

    Logro logro = new Logro();
    logro.setNombre("Primer Paso");

    UsuarioLogro usuarioLogro = new UsuarioLogro();
    usuarioLogro.setUsuario(usuario);
    usuarioLogro.setLogro(logro);

    List<UsuarioLogro> usuarioLogros = new ArrayList<>();
    usuarioLogros.add(usuarioLogro);

    logro.setUsuarioLogros(usuarioLogros);

    assertThat(logro.getUsuarioLogros(), is(usuarioLogros));
    assertThat(logro.getUsuarioLogros().size(), is(1));
    assertThat(logro.getUsuarioLogros().get(0).getUsuario().getEmail(), is("user@test.com"));
  }
}
