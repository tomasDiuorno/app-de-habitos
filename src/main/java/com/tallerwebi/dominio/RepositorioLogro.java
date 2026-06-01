package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioLogro {
    void guardar(Logro logro);
    Logro buscarPorNombre(String nombre);
    List<Logro> obtenerTodos();
}