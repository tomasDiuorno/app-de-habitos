package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Monedero;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;

public interface RepositorioMonedero {
  void guardar(Monedero monedero);
  void guardarTransaccion(Transaccion transaccion);
  Monedero buscarPorUsuario(Usuario usuario);
}
