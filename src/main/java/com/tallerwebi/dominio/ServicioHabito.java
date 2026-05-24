package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import java.util.List;

public interface ServicioHabito {
  List<Habito> obtenerHabitosIniciales();
  void agregarHabito(Habito habito) throws HabitoExistenteExeption;
  Habito buscarHabito(String titulo);
}
