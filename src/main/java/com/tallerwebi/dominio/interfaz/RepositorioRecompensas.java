package com.tallerwebi.dominio.interfaz;

import java.util.List;

import com.tallerwebi.dominio.entidades.Recompensa;

public interface RepositorioRecompensas {
  List<Recompensa> obtenerTodas();
  List<Recompensa> obtenerPorNivel(Integer nivelUsuario);
}
