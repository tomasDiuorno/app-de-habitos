package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.interfaz.RepositorioComentario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioComentario")
public class RepositorioComentarioImpl implements RepositorioComentario {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioComentarioImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(Comentario comentario) {
    sessionFactory.getCurrentSession().save(comentario);
  }
}
