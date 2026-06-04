package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioRecompensas {
  List<Recompensa> obtenerTodas();
  List<Recompensa> obtenerPorNivel(Integer nivelUsuario);
}
