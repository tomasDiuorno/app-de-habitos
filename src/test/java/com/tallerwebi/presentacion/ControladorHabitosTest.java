package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioCategoria;
import com.tallerwebi.dominio.ServicioHabito;
import com.tallerwebi.dominio.ServicioHistorialHabito;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.HistorialHabito;
import com.tallerwebi.dominio.excepcion.HabitoNoPerteneceAlUsuarioException;
import com.tallerwebi.dominio.excepcion.HabitoYaCompletadoHoyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorHabitosTest {

    private ControladorHabitos controladorHabitos;
    private ServicioHabito servicioHabitoMock;
    private ServicioCategoria servicioCategoriaMock;
    private ServicioHistorialHabito servicioHistorialHabitoMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init() {
        servicioHabitoMock = mock(ServicioHabito.class);
        servicioCategoriaMock = mock(ServicioCategoria.class);
        servicioHistorialHabitoMock = mock(ServicioHistorialHabito.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);

        controladorHabitos = new ControladorHabitos(servicioHabitoMock, servicioCategoriaMock, servicioHistorialHabitoMock);
    }

    @Test
    public void siElUsuarioNoEstaLogueadoAlCompletarHabitoDebeRedirigirALogin() {
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        ModelAndView modelAndView = controladorHabitos.completarHabito(1, requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void alCompletarHabitoCorrectamenteDebeRedirigirAHabitos() throws HabitoNoPerteneceAlUsuarioException, HabitoYaCompletadoHoyException {
        Usuario usuarioMock = new Usuario();
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);

        ModelAndView modelAndView = controladorHabitos.completarHabito(1, requestMock);

        verify(servicioHistorialHabitoMock, times(1)).marcarHabitoComoCompletado(usuarioMock, 1);
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/habitos"));
    }

    @Test
    public void siOcurreErrorAlCompletarHabitoDebeGuardarErrorEnSesionYRedirigirAHabitos() throws HabitoNoPerteneceAlUsuarioException, HabitoYaCompletadoHoyException {
        Usuario usuarioMock = new Usuario();
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        
        doThrow(new HabitoYaCompletadoHoyException()).when(servicioHistorialHabitoMock).marcarHabitoComoCompletado(usuarioMock, 1);

        ModelAndView modelAndView = controladorHabitos.completarHabito(1, requestMock);

        verify(sessionMock, times(1)).setAttribute(eq("errorCompletar"), any());
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/habitos"));
    }

    @Test
    public void siElUsuarioNoEstaLogueadoAlVerHistorialDebeRedirigirALogin() {
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        ModelAndView modelAndView = controladorHabitos.verHistorial(requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void alVerHistorialDebeIrALaVistaHistorialYPasarLaLista() {
        Usuario usuarioMock = new Usuario();
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        
        List<HistorialHabito> historialEsperado = new ArrayList<>();
        when(servicioHistorialHabitoMock.obtenerHistorial(usuarioMock)).thenReturn(historialEsperado);

        ModelAndView modelAndView = controladorHabitos.verHistorial(requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("historial"));
        assertThat(modelAndView.getModel().get("historial"), equalTo(historialEsperado));
    }
}
