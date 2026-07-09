package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.dominio.excepcion.UsuarioYaUnidoAHabitoException;
import com.tallerwebi.dominio.interfaz.ServicioComunidad;
import com.tallerwebi.dominio.interfaz.ServicioHabito;
import com.tallerwebi.presentacion.DTO.RegistroHabitoDTO;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioHabitoCompartido")
@Transactional
public class ServicioHabitoCompartidoImpl implements ServicioHabitoCompartido {

  private ServicioHabito servicioHabito;
  private ServicioComunidad servicioComunidad;

  @Autowired
  public ServicioHabitoCompartidoImpl(
    ServicioHabito servicioHabito,
    ServicioComunidad servicioComunidad
  ) {
    this.servicioHabito = servicioHabito;
    this.servicioComunidad = servicioComunidad;
  }

  @Override
  public Habito crearHabitoGrupal(RegistroHabitoDTO datos, Usuario creador)
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException {
    Habito habito = this.servicioHabito.obtenerHabito(datos);
    habito.setEsGrupal(true);

    this.servicioHabito.agregarHabitoParaUsuario(habito, creador);
    this.servicioComunidad.publicarHabitoEnForo(habito, creador);

    return habito;
  }

  @Override
  public void unirseAHabitoGrupal(Habito habito, Usuario usuario)
    throws LimiteHabitosAlcanzadoException, UsuarioYaUnidoAHabitoException {
    boolean yaUnido = usuario
      .getUsuarioHabitos()
      .stream()
      .anyMatch(vinculo ->
        vinculo.getHabito() != null && vinculo.getHabito().getId().equals(habito.getId())
      );

    if (yaUnido) {
      throw new UsuarioYaUnidoAHabitoException();
    }

    this.servicioHabito.vincularUsuarioAHabito(habito, usuario);
  }
}
