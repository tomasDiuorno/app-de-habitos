package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class HabitoTest {
    @Test
    public void deberiaSetearYObtenerUsuarioHabitos() {
        Usuario usuario = new Usuario();
        usuario.setEmail("user@test.com");

        UsuarioHabito usuarioHabito = new UsuarioHabito();
        Habito habito = new Habito();

        usuarioHabito.setHabito(habito);
        usuarioHabito.setUsuario(usuario);

        List<UsuarioHabito> usuarioHabitos = new ArrayList<>();
        usuarioHabitos.add(usuarioHabito);

        habito.setUsuarioHabitos(usuarioHabitos);

        assertThat(habito.getUsuarioHabitos(), is(usuarioHabitos));
        assertThat(habito.getUsuarioHabitos().size(), is(1));
        assertThat(habito.getUsuarioHabitos().get(0).getUsuario().getEmail(), is("user@test.com"));
    }
}
