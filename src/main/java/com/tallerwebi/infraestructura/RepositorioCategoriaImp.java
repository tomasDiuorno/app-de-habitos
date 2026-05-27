package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.RepositorioCategoria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioCategoria")
public class RepositorioCategoriaImp implements RepositorioCategoria {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioCategoriaImp(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(Categoria categoria) {
    this.sessionFactory.getCurrentSession().save(categoria);
  }
}
