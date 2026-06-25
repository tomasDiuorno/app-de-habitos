package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.interfaz.RepositorioComentario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioComentario")
public class RepositorioComentarioImpl implements RepositorioComentario {

<<<<<<< HEAD
  private SessionFactory sessionFactory;
=======
  private final SessionFactory sessionFactory;
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a

  @Autowired
  public RepositorioComentarioImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(Comentario comentario) {
<<<<<<< HEAD
    sessionFactory.getCurrentSession().save(comentario);
=======
    this.sessionFactory.getCurrentSession().save(comentario);
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
  }
}
