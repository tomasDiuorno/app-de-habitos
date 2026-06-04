package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioCategoria {
  List<Categoria> obtenerCategorias();
  Categoria obtenerCategoriaPorId(Integer categoriaId);
}
