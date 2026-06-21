package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Bonificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioBonificacion;
import java.util.List;

public interface ServicioTienda {
  List<Bonificacion> obtenerBonificacionesDisponibles();
  UsuarioBonificacion obtenerBonificacionActiva(Usuario usuario);
  void activarBonificacion(Usuario usuario, Integer idBonificacion);
}
