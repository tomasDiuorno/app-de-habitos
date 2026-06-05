package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Logro;
import java.util.List;

public interface RepositorioLogro {
  void guardar(Logro logro);
  Logro buscarPorNombre(String nombre);
  List<Logro> obtenerTodos();
}
