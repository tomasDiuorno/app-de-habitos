package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tallerwebi.dominio.servicios.ServicioHabitoIA;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.ControladorHabitoIA;
import com.tallerwebi.presentacion.DTO.HabitoObjetivoDTO;
import com.tallerwebi.presentacion.DTO.HabitoSugeridoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class ControladorHabitoIATest {

  private ServicioHabitoIA servicioHabitoIAMock;
  private ControladorHabitoIA controladorHabitoIA;
  private static final String OBJETIVO = "Quiero mejorar mi alimentación";

  @BeforeEach
  public void init() {
    servicioHabitoIAMock = Mockito.mock(ServicioHabitoIA.class);
    controladorHabitoIA = new ControladorHabitoIA(servicioHabitoIAMock);
  }

  @Test
  public void recomendarDeberiaRetornarRespuestaExitosa() throws JsonProcessingException {
    HabitoObjetivoDTO dto = dadoQueTengoUnObjetivo(OBJETIVO);
    HabitoSugeridoDTO sugerencia = new HabitoSugeridoDTO();

    sugerencia.setNombre("Comer saludable");
    sugerencia.setDescripcion("Preparar comidas saludables diariamente");
    sugerencia.setFrecuencia("Diario");
    sugerencia.setCategoria("Salud");

    dadoQueElServicioResponde(sugerencia);

    ResponseEntity<?> response = cuandoRecomiendoUnHabito(dto);
    entoncesLaRespuestaEsExitosa(response);
  }

  @Test
  public void recomendarDeberiaRetornarError500SiOcurreUnaExcepcion()
    throws JsonProcessingException {
    HabitoObjetivoDTO dto = dadoQueTengoUnObjetivo(OBJETIVO);
    dadoQueElServicioLanzaUnaExcepcion("Error comunicando con Gemini");

    ResponseEntity<?> response = cuandoRecomiendoUnHabito(dto);
    entoncesObtengoError500(response);
  }

  private HabitoObjetivoDTO dadoQueTengoUnObjetivo(String objetivo) {
    HabitoObjetivoDTO dto = new HabitoObjetivoDTO();
    dto.setObjetivo(objetivo);
    return dto;
  }

  private void dadoQueElServicioResponde(HabitoSugeridoDTO sugerencia)
    throws JsonProcessingException {
    when(servicioHabitoIAMock.recomendar(eq(OBJETIVO))).thenReturn(sugerencia);
  }

  private void dadoQueElServicioLanzaUnaExcepcion(String mensaje) throws JsonProcessingException {
    when(servicioHabitoIAMock.recomendar(eq(OBJETIVO))).thenThrow(new RuntimeException(mensaje));
  }

  private ResponseEntity<?> cuandoRecomiendoUnHabito(HabitoObjetivoDTO dto) {
    return controladorHabitoIA.preguntar(dto);
  }

  private void entoncesLaRespuestaEsExitosa(ResponseEntity<?> response) {
    entoncesLaRespuestaTieneStatusCode(response, HttpStatus.OK);
    HabitoSugeridoDTO body = (HabitoSugeridoDTO) response.getBody();
    assertThat(body.getNombre(), equalTo("Comer saludable"));
    assertThat(body.getCategoria(), equalTo("Salud"));
  }

  private void entoncesObtengoError500(ResponseEntity<?> response) {
    entoncesLaRespuestaTieneStatusCode(response, HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody().toString(), equalTo("Error comunicando con Gemini"));
  }

  private void entoncesLaRespuestaTieneStatusCode(ResponseEntity<?> response, HttpStatus status) {
    assertThat(response.getStatusCodeValue(), equalTo(status.value()));
  }
}
