package com.tallerwebi.dominio;

import java.util.List;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;

public interface ServicioHabito {
  List<Habito> obtenerHabitosIniciales();
  void agregarHabito(Habito habito) throws HabitoExistenteExeption;
  Habito buscarHabito(String titulo);
}
