package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;

public class ServicioHabitoTest {

    private ServicioHabito servicioHabitos;
    private RepositorioHabito repositorioHabitoMock;

    @BeforeEach
    public void init() {
        this.repositorioHabitoMock = mock(RepositorioHabito.class);
        this.servicioHabitos = new ServicioHabitoImp(this.repositorioHabitoMock);
    }

    @Test
    public void obtenerHabitosInicialesDeberiaRetornarUnaListaDeHabitos(){
        // preparacion
        List<Habito> habitosMock = new ArrayList<>();
        Habito habito1 = new Habito();
        habito1.setTitulo("Meditar");
        Habito habito2 = new Habito();
        habito2.setTitulo("Leer un libro");
        Habito habito3 = new Habito();
        habito3.setTitulo("Hacer ejercicio");

        habitosMock.add(habito1);
        habitosMock.add(habito2);
        habitosMock.add(habito3);

        when(this.repositorioHabitoMock.obtenerHabitosIniciales()).thenReturn(habitosMock);

        // ejecucion
        List<Habito> habitosObtenidos = this.servicioHabitos.obtenerHabitosIniciales();

        // validacion
        assertThat(habitosObtenidos.size(), equalTo(3));
        assertThat(habitosObtenidos.get(0).getTitulo(), equalTo("Meditar"));
        assertThat(habitosObtenidos.get(1).getTitulo(), equalTo("Leer un libro"));
        assertThat(habitosObtenidos.get(2).getTitulo(), equalTo("Hacer ejercicio"));
    }

    @Test
    public void alCrearUnHabitoDeberiaGuardarloExisosamente() throws HabitoExistenteExeption{
        // preparacion
        Habito habitoEsperado = new Habito();
        habitoEsperado.setTitulo("Correr");
        habitoEsperado.setCategoria("Deportes");
        
        when(this.repositorioHabitoMock.buscarPorTitulo(habitoEsperado.getTitulo())).thenReturn(null);

        // ejecucion
        this.servicioHabitos.agregarHabito(habitoEsperado);

        // validacion
        verify(this.repositorioHabitoMock, times(1)).guardar(habitoEsperado);
    }

    @Test
    public void alBuscarUnHabitoExistenteDeberiaRetornarlo(){
        // preparacion
        String titulo = "Correr";
        Habito habitoEsperado = new Habito();
        habitoEsperado.setTitulo(titulo);
        habitoEsperado.setCategoria("Deportes");

        when(this.repositorioHabitoMock.buscarPorTitulo(habitoEsperado.getTitulo())).thenReturn(new Habito());

        // validacion
        assertThrows(HabitoExistenteExeption.class, () -> this.servicioHabitos.agregarHabito(habitoEsperado));
        verify(this.repositorioHabitoMock, times(0)).guardar(any(Habito.class));
    }
}
