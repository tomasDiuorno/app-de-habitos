package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class UsuarioTest {
    
    @Test
    public void queSePuedanAsignarValoresCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setName("Tomas");
        usuario.setSurname("Diuorno");
        usuario.setUsername("Tomidiu");
        usuario.setEmail("tom.diuorno@example.com");
        usuario.setPassword("password123");
        usuario.setConfirmPass("password123");
        usuario.setRol("USER");

        assertThat(usuario.getName(), is("Tomas"));
        assertThat(usuario.getSurname(), is("Diuorno"));
        assertThat(usuario.getUsername(), is("Tomidiu"));
        assertThat(usuario.getEmail(), is("tom.diuorno@example.com"));
        assertThat(usuario.getPassword(), is("password123"));
        assertThat(usuario.getConfirmPass(), is("password123"));
        assertThat(usuario.getRol(), is("USER"));
    }
}
