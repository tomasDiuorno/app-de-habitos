package com.tallerwebi.dominio.interfaz;

import java.util.List;

import com.tallerwebi.dominio.entidades.Recompensa;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioRecompensa;

public interface ServicioRecompensas {
  List<Recompensa> obtenerRecompensas();
  void verificarRecompensas(Usuario usuario);
  List<UsuarioRecompensa> obtenerBaul(Usuario usuario);
  void marcarComoUtilizada(Integer idUsuarioRecompensa);
}
