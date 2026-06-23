package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.HistorialHabito;
import com.tallerwebi.dominio.entidades.Usuario;
import java.time.LocalDate;
import java.util.List;

public interface RepositorioHistorialHabito {
  void guardar(HistorialHabito historialHabito);
  List<HistorialHabito> obtenerPorUsuario(Usuario usuario);
  HistorialHabito obtenerPorUsuarioHabitoYFecha(Usuario usuario, Habito habito, LocalDate fecha);
}
