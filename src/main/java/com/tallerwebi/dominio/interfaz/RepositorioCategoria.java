package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Categoria;
import java.util.List;

public interface RepositorioCategoria {
  List<Categoria> obtenerCategorias();
  Categoria obtenerCategoriaPorId(Integer categoriaId);
}
