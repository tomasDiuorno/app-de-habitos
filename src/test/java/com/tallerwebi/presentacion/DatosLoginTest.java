package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class DatosLoginTest {

    @Test
    public void validacionDeDatosLogin(){

        DatosLogin datos = new DatosLogin();
        datos.setEmailorusername("pedrito123");
        datos.setPassword("1234");

        assertThat(datos.getEmailorusername(), is("pedrito123"));
        assertThat(datos.getPassword(), is("1234"));
    }
    
}
