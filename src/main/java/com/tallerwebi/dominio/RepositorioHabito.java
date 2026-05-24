package com.tallerwebi.dominio;
import java.util.List;

public interface RepositorioHabito {
  Habito buscarHabito(String titulo, String categoria);
  void guardar(Habito habito);
  Habito buscarCategoria(String categoria);
  Habito buscarPorTitulo(String titulo);
  void modificar(Habito habito);
  List<Habito> obtenerHabitosIniciales();
}
