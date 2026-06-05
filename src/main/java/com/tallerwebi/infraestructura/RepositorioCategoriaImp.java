package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.interfaz.RepositorioCategoria;

import java.util.List;
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
  public List<Categoria> obtenerCategorias() {
    return this.sessionFactory.getCurrentSession()
      .createQuery("FROM Categoria", Categoria.class)
      .getResultList();
  }

  @Override
  public Categoria obtenerCategoriaPorId(Integer categoriaId) {
    return (Categoria) this.sessionFactory.getCurrentSession()
      .createQuery("FROM Categoria WHERE id = :id")
      .setParameter("id", categoriaId)
      .uniqueResult();
  }
}
