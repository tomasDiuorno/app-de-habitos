package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ChecklistInsuficienteExeption;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.presentacion.DatosRegistroHabito;
import java.util.List;

public interface ServicioHabito {
  List<Habito> obtenerHabitosIniciales();
  void agregarHabito(Habito habito) throws HabitoExistenteExeption;
  void agregarHabitoParaUsuario(Habito habito, Usuario usuario)
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException;
  Habito buscarHabito(String titulo);
<<<<<<< HEAD
  void actualizarProgresoActualHabito(Habito habito) throws ChecklistInsuficienteExeption;
  void agregarItemChecklistAlHabito(ItemChecklist item, Integer idHabito)
    throws ChecklistInsuficienteExeption;
  void eliminarItemChecklistDelHabito(ItemChecklist item, Integer idHabito)
    throws ChecklistInsuficienteExeption;
=======
>>>>>>> f322509e626b90305af443c3593a67de50f1dc67
  Habito buscarHabitoPorId(Integer id);
  Habito obtenerHabito(DatosRegistroHabito datos);
}
