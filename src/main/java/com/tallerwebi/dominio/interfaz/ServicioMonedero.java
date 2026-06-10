package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import java.util.List;

public interface ServicioMonedero {
  void inicializarMonedero(Usuario usuario);
  void acreditarPorLogro(Usuario usuario);
  void acreditarPorRecompensa(Usuario usuario);
  Integer obtenerSaldo(Usuario usuario);
  List<Transaccion> obtenerTransacciones(Usuario usuario);
  void acreditarMonedas(Usuario usuario, Integer monto, String tipo, String descripcion);
}
