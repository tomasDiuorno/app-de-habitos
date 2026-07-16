package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.ServicioProgresoUsuario;
import org.springframework.stereotype.Service;

@Service("servicioProgresoUsuario")
public class ServicioProgresoUsuarioImpl implements ServicioProgresoUsuario {

  private static final Integer NIVEL_MAXIMO = 100;

  @Override
  public void otorgarExperiencia(Usuario usuario, Integer experiencia) {
    if (usuario.getNivelUsuario() >= NIVEL_MAXIMO) {
      return;
    }
    Integer experienciaActual = usuario.getExperiencia() + experiencia;

    while (
      usuario.getNivelUsuario() < NIVEL_MAXIMO &&
      experienciaActual >= experienciaNecesaria(usuario.getNivelUsuario())
    ) {
      experienciaActual -= experienciaNecesaria(usuario.getNivelUsuario());
      usuario.setNivelUsuario(usuario.getNivelUsuario() + 1);
    }
  }

  private Integer experienciaNecesaria(Integer nivel) {
    return (int) (100 + Math.pow(nivel, 1.6) * 35);
  }
}
