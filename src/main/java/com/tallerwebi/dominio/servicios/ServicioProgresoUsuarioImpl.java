package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.ServicioProgresoUsuario;
import com.tallerwebi.presentacion.DTO.BarraExperienciaDTO;
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

  @Override
  public BarraExperienciaDTO obtenerBarraExperiencia(Usuario usuario) {
    Integer experienciaNecesaria = experienciaNecesaria(usuario.getNivelUsuario());
    Integer porcentaje = usuario.getNivelUsuario() >= NIVEL_MAXIMO
      ? 100
      : (usuario.getExperiencia() * 100) / experienciaNecesaria;
    return new BarraExperienciaDTO(
      usuario.getNivelUsuario(),
      usuario.getExperiencia(),
      experienciaNecesaria,
      porcentaje
    );
  }
}
