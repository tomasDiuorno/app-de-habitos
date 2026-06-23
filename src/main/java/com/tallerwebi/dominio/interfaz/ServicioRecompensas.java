package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Recompensa;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioRecompensa;
import java.util.List;

public interface ServicioRecompensas {
  List<Recompensa> obtenerRecompensas();
  void verificarRecompensas(Usuario usuario);
  List<UsuarioRecompensa> obtenerBaul(Usuario usuario);
  void marcarComoUtilizada(Integer idUsuarioRecompensa);
  UsuarioRecompensa obtenerPorId(Integer idUsuarioRecompensa);
}
