package com.tallerwebi.dominio.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.enums.TipoHabitoEnum;
import com.tallerwebi.dominio.interfaz.ServicioEvaluadorTipoHabito;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EvaluadorHabitosFactoryTest {

  @Test
  public void dadoUnTipoHabitoHorario_cuandoObtengoElEvaluador_entoncesDevuelveElEvaluadorHorario() {
    // Given
    ServicioEvaluadorTipoHabito evaluadorHorario = mock(ServicioEvaluadorTipoHabito.class);
    when(evaluadorHorario.getTipoHabito()).thenReturn(TipoHabitoEnum.HORARIO);

    EvaluadorHabitosFactory factory = new EvaluadorHabitosFactory(List.of(evaluadorHorario));

    // When
    ServicioEvaluadorTipoHabito resultado = factory.obtener(TipoHabitoEnum.HORARIO);

    // Then
    assertThat(resultado, is(evaluadorHorario));
  }

  @Test
  public void dadoUnTipoHabitoSinEvaluador_cuandoObtengoElEvaluador_entoncesLanzaUnaExcepcion() {
    // Given
    ServicioEvaluadorTipoHabito evaluadorHorario = mock(ServicioEvaluadorTipoHabito.class);
    when(evaluadorHorario.getTipoHabito()).thenReturn(TipoHabitoEnum.HORARIO);
    EvaluadorHabitosFactory factory = new EvaluadorHabitosFactory(List.of(evaluadorHorario));

    // When
    IllegalArgumentException excepcion = assertThrows(
      IllegalArgumentException.class,
      () -> factory.obtener(TipoHabitoEnum.CANTIDAD)
    );

    // Then
    assertThat(
      excepcion.getMessage(),
      is("No se encontró un evaluador para el tipo de hábito: CANTIDAD")
    );
  }
}
