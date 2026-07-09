package com.tallerwebi.dominio.servicios;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioHabito;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.dominio.excepcion.UsuarioYaUnidoAHabitoException;
import com.tallerwebi.dominio.interfaz.ServicioComunidad;
import com.tallerwebi.dominio.interfaz.ServicioHabito;
import com.tallerwebi.presentacion.DTO.RegistroHabitoDTO;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioHabitoCompartidoTest {

  private ServicioHabitoCompartido servicioHabitoCompartido;
  private ServicioHabito servicioHabitoMock;
  private ServicioComunidad servicioComunidadMock;

  @BeforeEach
  public void init() {
    this.servicioHabitoMock = mock(ServicioHabito.class);
    this.servicioComunidadMock = mock(ServicioComunidad.class);

    this.servicioHabitoCompartido =
      new ServicioHabitoCompartidoImpl(this.servicioHabitoMock, this.servicioComunidadMock);
  }

  @Test
  public void crearHabitoGrupalDeberiaMarcarloComoGrupalVincularAlCreadorYPublicarEnElForo()
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException {
    Usuario creador = new Usuario();
    creador.setId(1);

    RegistroHabitoDTO datos = new RegistroHabitoDTO();
    datos.setTitulo("Meditar en grupo");

    Habito habitoBase = new Habito();
    habitoBase.setTitulo("Meditar en grupo");

    when(this.servicioHabitoMock.obtenerHabito(datos)).thenReturn(habitoBase);

    Habito resultado = this.servicioHabitoCompartido.crearHabitoGrupal(datos, creador);

    assertThat(resultado.getEsGrupal(), is(true));
    verify(this.servicioHabitoMock, times(1)).agregarHabitoParaUsuario(habitoBase, creador);
    verify(this.servicioComunidadMock, times(1)).publicarHabitoEnForo(habitoBase, creador);
  }

  @Test
  public void unirseAHabitoGrupalDeberiaVincularAlUsuarioSinVolverACrearElHabito()
    throws LimiteHabitosAlcanzadoException, UsuarioYaUnidoAHabitoException {
    Usuario usuario = new Usuario();
    usuario.setId(2);

    Habito habito = new Habito();
    habito.setTitulo("Meditar en grupo");
    habito.setEsGrupal(true);

    this.servicioHabitoCompartido.unirseAHabitoGrupal(habito, usuario);

    verify(this.servicioHabitoMock, times(1)).vincularUsuarioAHabito(habito, usuario);
  }

  @Test
  public void unirseAHabitoGrupalDeberiaPropagarLaExcepcionDeLimite()
    throws LimiteHabitosAlcanzadoException {
    Usuario usuario = new Usuario();
    Habito habito = new Habito();
    habito.setEsGrupal(true);

    org.mockito.Mockito
      .doThrow(new LimiteHabitosAlcanzadoException())
      .when(this.servicioHabitoMock)
      .vincularUsuarioAHabito(habito, usuario);

    assertThrows(
      LimiteHabitosAlcanzadoException.class,
      () -> this.servicioHabitoCompartido.unirseAHabitoGrupal(habito, usuario)
    );
  }

  @Test
  public void unirseAHabitoGrupalCuandoElUsuarioYaEstaUnidoDeberiaLanzarExcepcion()
    throws LimiteHabitosAlcanzadoException {
    Usuario usuario = new Usuario();
    usuario.setId(2);

    Habito habito = new Habito();
    habito.setId(10);
    habito.setEsGrupal(true);

    UsuarioHabito vinculoExistente = new UsuarioHabito();
    vinculoExistente.setHabito(habito);
    vinculoExistente.setUsuario(usuario);

    usuario.setUsuarioHabitos(
      new ArrayList<>(java.util.Collections.singletonList(vinculoExistente))
    );

    assertThrows(
      UsuarioYaUnidoAHabitoException.class,
      () -> this.servicioHabitoCompartido.unirseAHabitoGrupal(habito, usuario)
    );

    verify(this.servicioHabitoMock, org.mockito.Mockito.never())
      .vincularUsuarioAHabito(any(Habito.class), any(Usuario.class));
  }
}
