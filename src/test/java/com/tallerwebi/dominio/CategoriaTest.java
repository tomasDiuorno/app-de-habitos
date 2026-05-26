package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CategoriaTest {
    @Test
    public void unaCategoriaDeberiaTenerUnHabito() {
        Categoria cat = new Categoria();
        cat.setNombre("Deporte");
        Habito hab = new Habito();
        hab.setTitulo("Ir al gimnasio");
        hab.setCategoria(cat);
        List<Habito> habitos = new ArrayList<>();
        habitos.add(hab);

        cat.setHabitos(habitos);

        assertThat(cat.getHabitos().size(), is(1));
        assertThat(cat.getHabitos().get(0).getTitulo(), is("Ir al gimnasio"));
    }

    @Test
    public void deberiaSetearYObtenerLosDatosDeCategoria() {
        Categoria categoria = new Categoria();

        categoria.setId(1);
        categoria.setNombre("Bienestar");

        assertThat(categoria.getId(), is(1));
        assertThat(categoria.getNombre(), is("Bienestar"));
    }
}
