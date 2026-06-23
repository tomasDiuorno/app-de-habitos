package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioHabito;

public interface ServicioUsuarioHabito {
  UsuarioHabito obtenerPorUsuarioYHabito(Usuario usuario, Habito habito);
}
