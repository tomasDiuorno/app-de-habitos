package com.tallerwebi.dominio;

public interface RepositorioLogro {
  void guardar(Logro logro);
  Logro buscarPorNombre(String nombre);
}
