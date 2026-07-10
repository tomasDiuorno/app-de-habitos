package com.tallerwebi.dominio.servicios;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.RegistroHabito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioHabito;
import com.tallerwebi.dominio.enums.TipoHabitoEnum;
import com.tallerwebi.dominio.factory.EvaluadorHabitosFactory;
import com.tallerwebi.dominio.interfaz.RepositorioRegistroHabito;
import com.tallerwebi.dominio.interfaz.ServicioEvaluadorHabito;
import com.tallerwebi.dominio.interfaz.ServicioEvaluadorTipoHabito;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioEvaluadorHabitoTest {

  private ServicioEvaluadorHabito servicioEvaluadorHabito;
  private EvaluadorHabitosFactory factoryMock;
  private RepositorioRegistroHabito registroHabitoRepositorioMock;
  private ServicioEvaluadorTipoHabito evaluadorMock;

  @BeforeEach
  public void init() {
    factoryMock = mock(EvaluadorHabitosFactory.class);
    registroHabitoRepositorioMock = mock(RepositorioRegistroHabito.class);
    evaluadorMock = mock(ServicioEvaluadorTipoHabito.class);
    servicioEvaluadorHabito =
      new ServicioEvaluadorHabitoImp(factoryMock, registroHabitoRepositorioMock);
  }

  @Test
  public void completarHabitoDeberiaEvaluarYGuardarRegistro() {
    // preparación
    Usuario usuario = new Usuario();
    Habito habito = new Habito();
    habito.setTipoHabito(TipoHabitoEnum.HORARIO);
    UsuarioHabito usuarioHabito = new UsuarioHabito();
    usuarioHabito.setUsuario(usuario);
    usuarioHabito.setHabito(habito);

    ResultadoEvaluacionDTO resultado = new ResultadoEvaluacionDTO(true, "Cumplido");

    when(factoryMock.obtener(TipoHabitoEnum.HORARIO)).thenReturn(evaluadorMock);
    when(evaluadorMock.evaluar(habito, "22:40")).thenReturn(resultado);

    // ejecución
    servicioEvaluadorHabito.completarHabito(usuarioHabito, "22:40");

    // validación
    verify(factoryMock).obtener(TipoHabitoEnum.HORARIO);
    verify(evaluadorMock).evaluar(habito, "22:40");
    verify(registroHabitoRepositorioMock).guardar(any(RegistroHabito.class));
  }

  @Test
  public void completarHabitoConEvidenciaIncorrectaDeberiaGuardarRegistroNoCompletado() {
    Usuario usuario = new Usuario();
    Habito habito = new Habito();
    habito.setTipoHabito(TipoHabitoEnum.HORARIO);
    UsuarioHabito usuarioHabito = new UsuarioHabito();
    usuarioHabito.setUsuario(usuario);
    usuarioHabito.setHabito(habito);

    ResultadoEvaluacionDTO resultado = new ResultadoEvaluacionDTO(false, "Fuera de horario");

    when(factoryMock.obtener(TipoHabitoEnum.HORARIO)).thenReturn(evaluadorMock);
    when(evaluadorMock.evaluar(habito, "23:50")).thenReturn(resultado);
    servicioEvaluadorHabito.completarHabito(usuarioHabito, "23:50");
    verify(registroHabitoRepositorioMock).guardar(argThat(registro -> !registro.getCompletado()));
  }
}
