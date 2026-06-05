package com.tallerwebi.dominio.interfaz;

import java.util.List;

import com.tallerwebi.dominio.entidades.Logro;

public interface RepositorioLogro {
  void guardar(Logro logro);
  Logro buscarPorNombre(String nombre);
  List<Logro> obtenerTodos();
}
