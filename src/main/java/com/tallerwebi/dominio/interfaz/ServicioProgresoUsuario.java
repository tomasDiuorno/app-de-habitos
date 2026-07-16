package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Usuario;

public interface ServicioProgresoUsuario {
  void otorgarExperiencia(Usuario usuario, Integer experiencia);
}
