package com.tallerwebi.dominio;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioCategoria")
@Transactional
public class ServicioCategoriaImp implements ServicioCategoria {

  private RepositorioCategoria repositorioCategoria;

  @Autowired
  public ServicioCategoriaImp(RepositorioCategoria repositorioCategoria) {
    this.repositorioCategoria = repositorioCategoria;
  }

  @Override
  public List<Categoria> obtenerCategorias() {
    return this.repositorioCategoria.obtenerCategorias();
  }
}
