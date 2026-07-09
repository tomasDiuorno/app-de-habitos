package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.presentacion.DTO.RegistroHabitoDTO;
import java.util.List;

public interface ServicioHabito {
  List<Habito> obtenerHabitosIniciales();
  void agregarHabito(Habito habito) throws HabitoExistenteExeption;
  void agregarHabitoParaUsuario(Habito habito, Usuario usuario)
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException;
  Habito buscarHabito(String titulo);
  Habito buscarHabitoPorId(Integer id);
  Habito obtenerHabito(RegistroHabitoDTO datos);
  void vincularUsuarioAHabito(Habito habito, Usuario usuario)
    throws LimiteHabitosAlcanzadoException;
}
