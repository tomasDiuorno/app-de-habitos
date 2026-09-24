package com.tallerwebi.dominio.componentes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.tallerwebi.dominio.entidades.ConfiguracionHabito;
import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.servicios.ServicioEvaluadorCantidadImpl;
import com.tallerwebi.presentacion.DTO.EvidenciaDTO;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EvaluadorCantidadTest {

  private ServicioEvaluadorCantidadImpl evaluadorCantidad;

  @BeforeEach
  public void init() {
    evaluadorCantidad = new ServicioEvaluadorCantidadImpl();
  }

  @Test
  public void evaluarCantidadMayorOIgualAObjetivoDeberiaCumplir() {
    Habito habito = new Habito();
    EvidenciaDTO evidencia = new EvidenciaDTO();
    evidencia.setTexto("2500");
    ConfiguracionHabito configuracion = new ConfiguracionHabito();
    configuracion.setObjetivoNumero(2000);
    habito.setConfiguracion(configuracion);

    ResultadoEvaluacionDTO resultado = evaluadorCantidad.evaluar(habito, evidencia);

    assertThat(resultado.getCumplido(), is(true));
  }

  @Test
  public void evaluarCantidadMenorAObjetivoDeberiaFallar() {
    Habito habito = new Habito();
    EvidenciaDTO evidencia = new EvidenciaDTO();
    evidencia.setTexto("1500");
    ConfiguracionHabito configuracion = new ConfiguracionHabito();

    configuracion.setObjetivoNumero(2000);
    habito.setConfiguracion(configuracion);

    ResultadoEvaluacionDTO resultado = evaluadorCantidad.evaluar(habito, evidencia);

    assertThat(resultado.getCumplido(), is(false));
  }
}
