package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.presentacion.DTO.BarraExperienciaDTO;

public interface ServicioProgresoUsuario {
  BarraExperienciaDTO obtenerBarraExperiencia(Usuario usuario);
  void otorgarExperiencia(Usuario usuario, Integer experiencia);
}
