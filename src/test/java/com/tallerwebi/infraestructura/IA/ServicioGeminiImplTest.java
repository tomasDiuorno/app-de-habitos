package com.tallerwebi.infraestructura.IA;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class ServicioGeminiImplTest {
    private ServicioGeminiImpl servicioGemini;
    private RestTemplate restTemplateMock;
    private Dotenv dotenvMock;
    private ObjectMapper mapper;
    private static final String MENSAJE = "Quiero mejorar mi alimentación";
    private static final String RESPUESTA_ESPERADA = "{\"nombre\":\"Comer saludable\"}";
    private static final String JSON_RESPONSE = "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":\""
            + RESPUESTA_ESPERADA.replace("\"", "\\\"")
            + "\"}]}}]}";

    @BeforeEach
    public void init() {
        restTemplateMock = mock(RestTemplate.class);
        dotenvMock = mock(Dotenv.class);
        mapper = new ObjectMapper();
        when(dotenvMock.get("GEMINI_API_KEY")).thenReturn("api-key-test");
        when(dotenvMock.get("GEMINI_URL")).thenReturn("http://gemini-test");
        servicioGemini = new ServicioGeminiImpl(restTemplateMock, dotenvMock, mapper);
    }

    @Test
    public void preguntarDeberiaRetornarRespuestaDeGemini() {
        this.dadoQueLaApiResponde(JSON_RESPONSE);
        String respuesta = this.cuandoPregunto(MENSAJE);
        this.entoncesLaRespuestaEs(respuesta, RESPUESTA_ESPERADA);
    }

    @Test
    public void preguntarDeberiaLanzarErrorSiLaRespuestaJsonEsInvalida() {
        this.dadoQueLaApiResponde("json roto");
        try {
            this.cuandoPregunto(MENSAJE);
        } catch (RuntimeException e) {
            assertThat(e.getMessage(),equalTo("Error de comunicacion con Gemini"));
        }
    }

    @Test
    public void preguntarDeberiaEnviarElMensajeALaApi() {
        this.dadoQueLaApiResponde(JSON_RESPONSE);
        this.cuandoPregunto(MENSAJE);
        this.entoncesLaPeticionContieneElMensaje();
    }

    @Test
    public void preguntarDeberiaLanzarErrorSiFallaLaComunicacion() {
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("Error conexion"));
        try {
            this.cuandoPregunto(MENSAJE);
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), equalTo("Error conexion"));
        }
    }

    private void dadoQueLaApiResponde(String json) {
        when(restTemplateMock.postForObject(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(json);
    }

    private String cuandoPregunto(String mensaje) {
        return servicioGemini.preguntar(mensaje);
    }

    private void entoncesLaRespuestaEs(String actual, String esperada) {
        assertThat(actual, equalTo(esperada));
    }

    private void entoncesLaPeticionContieneElMensaje() {
        verify(restTemplateMock).postForObject(anyString(), argThat((HttpEntity<String> entity) -> {
            String body = entity.getBody();
            return body != null && body.contains(MENSAJE);
        }), eq(String.class));
    }

}