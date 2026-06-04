package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioRecompensas {
  List<Recompensa> obtenerRecompensas();
  void verificarRecompensas(Usuario usuario);
  List<UsuarioRecompensa> obtenerBaul(Usuario usuario);
}
