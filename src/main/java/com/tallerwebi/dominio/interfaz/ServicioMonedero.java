package com.tallerwebi.dominio.interfaz;

import java.util.List;

import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;

public interface ServicioMonedero {
    void inicializarMonedero(Usuario usuario);
    void acreditarPorLogro(Usuario usuario);
    void acreditarPorRecompensa(Usuario usuario);
    Integer obtenerSaldo(Usuario usuario);
    List<Transaccion> obtenerTransacciones(Usuario usuario);
    void acreditarMonedas(Usuario usuario, Integer monto, String tipo, String descripcion);
}