package com.tallerwebi.dominio.interfaz;

import java.util.List;

import com.tallerwebi.dominio.entidades.Categoria;

public interface RepositorioCategoria {
  List<Categoria> obtenerCategorias();
  Categoria obtenerCategoriaPorId(Integer categoriaId);
}
