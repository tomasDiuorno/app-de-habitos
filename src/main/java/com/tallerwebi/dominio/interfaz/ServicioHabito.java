package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.ItemChecklist;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.ChecklistInsuficienteExeption;
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
  void actualizarProgresoActualHabito(Habito habito) throws ChecklistInsuficienteExeption;
  void agregarItemChecklistAlHabito(ItemChecklist item, Integer idHabito)
    throws ChecklistInsuficienteExeption;
  void eliminarItemChecklistDelHabito(ItemChecklist item, Integer idHabito)
    throws ChecklistInsuficienteExeption;
  Habito buscarHabitoPorId(Integer id);
  Habito obtenerHabito(RegistroHabitoDTO datos);
  void actualizarEstadoItemChecklist(Integer itemId, Integer habitoId)
    throws ChecklistInsuficienteExeption;
  void editarDescripcionItemChecklist(Integer idItem, Integer idHabito, String nuevaDescripcion)
    throws ChecklistInsuficienteExeption;
}
