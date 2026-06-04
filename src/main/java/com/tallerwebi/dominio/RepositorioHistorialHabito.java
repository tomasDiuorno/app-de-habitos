package com.tallerwebi.dominio;

import java.time.LocalDate;
import java.util.List;

public interface RepositorioHistorialHabito {
  void guardar(HistorialHabito historialHabito);
  List<HistorialHabito> obtenerPorUsuario(Usuario usuario);
  HistorialHabito obtenerPorUsuarioHabitoYFecha(Usuario usuario, Habito habito, LocalDate fecha);
}
