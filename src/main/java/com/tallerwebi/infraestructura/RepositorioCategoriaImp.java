package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.RepositorioCategoria;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
  public List<Categoria> obtenerCategorias() {
    return this.sessionFactory.getCurrentSession()
      .createQuery("FROM Categoria", Categoria.class)
      .getResultList();
  }

  @Override
  public Categoria obtenerCategoriaPorId(Integer categoriaId) {
    return (Categoria) this.sessionFactory.getCurrentSession()
      .createCriteria(Categoria.class)
      .add(Restrictions.eq("id", categoriaId))
      .uniqueResult();
  }
}
