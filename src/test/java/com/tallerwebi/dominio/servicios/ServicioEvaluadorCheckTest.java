package com.tallerwebi.dominio.servicios;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.interfaz.ServicioIA;
import com.tallerwebi.presentacion.DTO.EvidenciaDTO;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;

public class ServicioEvaluadorCheckTest {
    private ServicioIA servicioIAMock;
    private MultipartFile imagenMock;
    private ServicioEvaluadorCheckImpl servicioEvaluadorCheck;

    @BeforeEach
    public void init() {
        servicioIAMock = mock(ServicioIA.class);
        imagenMock = mock(MultipartFile.class);
        servicioEvaluadorCheck = new ServicioEvaluadorCheckImpl(
                servicioIAMock);
    }

    @Test
    public void dadoQueNoSeEnviaImagenCuandoSeEvaluaEntoncesDevuelveError() {
        // GIVEN
        Habito habito = new Habito();
        EvidenciaDTO evidencia = new EvidenciaDTO();

        // WHEN
        ResultadoEvaluacionDTO resultado = servicioEvaluadorCheck.evaluar(habito, evidencia);

        // THEN
        assertThat(resultado.getCumplido(), is(false));
        assertThat(resultado.getDetalle(), equalTo("No se proporcionó una imagen para evaluar."));

        verifyNoInteractions(servicioIAMock);
    }

    @Test
    public void dadoQueLaImagenEstaVaciaCuandoSeEvaluaEntoncesDevuelveError() {

        // GIVEN
        Habito habito = new Habito();

        EvidenciaDTO evidencia = new EvidenciaDTO();
        evidencia.setImagen(imagenMock);

        when(imagenMock.isEmpty()).thenReturn(true);

        // WHEN
        ResultadoEvaluacionDTO resultado = servicioEvaluadorCheck.evaluar(habito, evidencia);

        // THEN
        assertThat(resultado.getCumplido(), is(false));
        assertThat(resultado.getDetalle(), equalTo("No se proporcionó una imagen para evaluar."));

        verifyNoInteractions(servicioIAMock);
    }

    @Test
    public void dadoQueLaImagenCumpleCuandoLaIARespondeSiEntoncesElHabitoSeCompleta()
            throws IOException {

        // GIVEN
        Habito habito = new Habito();
        habito.setTitulo("Ir al gimnasio");
        habito.setDescripcion("Entrenar");

        EvidenciaDTO evidencia = new EvidenciaDTO();
        evidencia.setImagen(imagenMock);

        byte[] bytes = "imagen".getBytes();

        when(imagenMock.isEmpty()).thenReturn(false);
        when(imagenMock.getBytes()).thenReturn(bytes);
        when(imagenMock.getContentType()).thenReturn("image/jpeg");

        when(servicioIAMock.evaluarImagen(
                anyString(),
                eq("Ir al gimnasio"),
                eq("Entrenar"),
                eq("image/jpeg")))
                .thenReturn("SI");

        // WHEN
        ResultadoEvaluacionDTO resultado = servicioEvaluadorCheck.evaluar(habito, evidencia);

        // THEN
        assertThat(resultado.getCumplido(), is(true));
        assertThat(resultado.getDetalle(), equalTo("SI"));

        verify(servicioIAMock).evaluarImagen(
                anyString(),
                eq("Ir al gimnasio"),
                eq("Entrenar"),
                eq("image/jpeg"));
    }

    @Test
public void dadoQueLaImagenNoCumpleCuandoLaIARespondeNoEntoncesElHabitoNoSeCompleta()
    throws IOException {

    // GIVEN
    Habito habito = new Habito();
    habito.setTitulo("Ir al gimnasio");
    habito.setDescripcion("Entrenar");

    EvidenciaDTO evidencia = new EvidenciaDTO();
    evidencia.setImagen(imagenMock);

    when(imagenMock.isEmpty()).thenReturn(false);
    when(imagenMock.getBytes()).thenReturn("imagen".getBytes());
    when(imagenMock.getContentType()).thenReturn("image/jpeg");

    when(servicioIAMock.evaluarImagen(anyString(), anyString(), anyString(), anyString()))
        .thenReturn("NO");

    // WHEN
    ResultadoEvaluacionDTO resultado = servicioEvaluadorCheck.evaluar(habito, evidencia);

    // THEN
    assertThat(resultado.getCumplido(), is(false));
    assertThat(resultado.getDetalle(), equalTo("NO"));
}
}
