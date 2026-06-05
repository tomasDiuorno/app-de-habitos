package com.tallerwebi.dominio.interfaz;

import java.util.List;

import com.tallerwebi.dominio.entidades.Habito;

public interface RepositorioHabito {
  Habito buscarPorTitulo(String titulo);
  void guardar(Habito habito);
  void modificar(Habito habito);
  List<Habito> obtenerHabitosIniciales();
  List<Habito> buscarPorIds(List<Integer> ids);
  Habito buscarPorId(Integer id);
}
