package com.tallerwebi.dominio.servicios;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.interfaz.RepositorioCategoria;
import com.tallerwebi.dominio.interfaz.ServicioCategoria;

@Service("servicioCategoria")
@Transactional
public class ServicioCategoriaImpl implements ServicioCategoria {

  private RepositorioCategoria repositorioCategoria;

  @Autowired
  public ServicioCategoriaImpl(RepositorioCategoria repositorioCategoria) {
    this.repositorioCategoria = repositorioCategoria;
  }

  @Override
  public List<Categoria> obtenerCategorias() {
    return this.repositorioCategoria.obtenerCategorias();
  }
}
