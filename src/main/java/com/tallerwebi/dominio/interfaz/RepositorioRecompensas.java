package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Recompensa;
import java.util.List;

public interface RepositorioRecompensas {
  List<Recompensa> obtenerTodas();
  List<Recompensa> obtenerPorNivel(Integer nivelUsuario);
}
