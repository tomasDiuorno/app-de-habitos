package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ChecklistInsuficienteExeption;
import com.tallerwebi.dominio.excepcion.DescripcionChecklistInvalidaException;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.HabitoNoEncontradoException;
import com.tallerwebi.dominio.excepcion.ItemChecklistNoEncontradoException;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.presentacion.DatosRegistroHabito;
import java.util.List;

public interface ServicioHabito {
  List<Habito> obtenerHabitosIniciales();
  void agregarHabito(Habito habito) throws HabitoExistenteExeption;
  void agregarHabitoParaUsuario(Habito habito, Usuario usuario)
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException;
  Habito buscarHabito(String titulo);
  void actualizarProgresoActualHabito(Habito habito) throws ChecklistInsuficienteExeption;
  void agregarItemChecklistAlHabito(ItemChecklist item, Integer idHabito)
    throws ChecklistInsuficienteExeption, HabitoNoEncontradoException;
  void eliminarItemChecklistDelHabito(Integer id, Integer idHabito)
    throws ChecklistInsuficienteExeption, HabitoNoEncontradoException, ItemChecklistNoEncontradoException;
  Habito buscarHabitoPorId(Integer id) throws HabitoNoEncontradoException;
  Habito obtenerHabito(DatosRegistroHabito datos);
  void actualizarEstadoItemChecklist(Integer itemId, Integer habitoId)
    throws ChecklistInsuficienteExeption, HabitoNoEncontradoException, ItemChecklistNoEncontradoException;
  void editarDescripcionItemChecklist(Integer idItem, Integer idHabito, String nuevaDescripcion)
    throws ChecklistInsuficienteExeption, HabitoNoEncontradoException, ItemChecklistNoEncontradoException, DescripcionChecklistInvalidaException;
   
}
