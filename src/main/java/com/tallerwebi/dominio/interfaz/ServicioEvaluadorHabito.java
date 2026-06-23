package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.UsuarioHabito;

public interface ServicioEvaluadorHabito {
  void completarHabito(UsuarioHabito usuarioHabito, String evidencia);
}
