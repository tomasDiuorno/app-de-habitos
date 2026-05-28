package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioHabito {
  Habito buscarPorTitulo(String titulo);
  void guardar(Habito habito);
  void modificar(Habito habito);
  List<Habito> obtenerHabitosIniciales();
  List<Habito> buscarPorIds(List<Integer> ids);
  Habito buscarHabitoPorId(Integer id);
}
