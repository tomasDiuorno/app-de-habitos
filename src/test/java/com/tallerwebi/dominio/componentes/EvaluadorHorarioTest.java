package com.tallerwebi.dominio.componentes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.tallerwebi.dominio.entidades.ConfiguracionHabito;
import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.servicios.ServicioEvaluadorHorarioImpl;
import com.tallerwebi.presentacion.DTO.EvidenciaDTO;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EvaluadorHorarioTest {

  private ServicioEvaluadorHorarioImpl evaluadorHorario;

  @BeforeEach
  public void init() {
    evaluadorHorario = new ServicioEvaluadorHorarioImpl();
  }

  @Test
  public void evaluarHorarioAntesDeLimiteDeberiaCumplir() {
    // preparación
    Habito habito = new Habito();
    EvidenciaDTO evidencia = new EvidenciaDTO();
    evidencia.setTexto("22:40");
    ConfiguracionHabito configuracion = new ConfiguracionHabito();
    configuracion.setHoraLimite(LocalTime.of(23, 0));
    habito.setConfiguracion(configuracion);

    // ejecución
    ResultadoEvaluacionDTO resultado = evaluadorHorario.evaluar(habito, evidencia);

    // validación
    assertThat(resultado.getCumplido(), is(true));
  }

  @Test
  public void evaluarHorarioDespuesDeLimiteDeberiaFallar() {
    Habito habito = new Habito();
    EvidenciaDTO evidencia = new EvidenciaDTO();
    evidencia.setTexto("23:30");
    ConfiguracionHabito configuracion = new ConfiguracionHabito();

    configuracion.setHoraLimite(LocalTime.of(23, 0));
    habito.setConfiguracion(configuracion);

    ResultadoEvaluacionDTO resultado = evaluadorHorario.evaluar(habito, evidencia);

    assertThat(resultado.getCumplido(), is(false));
  }
}
