package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Publicacion;
import com.tallerwebi.dominio.interfaz.RepositorioPublicacion;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioPublicacion")
public class RepositorioPublicacionImpl implements RepositorioPublicacion {

<<<<<<< HEAD
  private SessionFactory sessionFactory;
=======
  private final SessionFactory sessionFactory;
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a

  @Autowired
  public RepositorioPublicacionImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(Publicacion publicacion) {
<<<<<<< HEAD
    sessionFactory.getCurrentSession().save(publicacion);
=======
    this.sessionFactory.getCurrentSession().save(publicacion);
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
  }

  @Override
  public Publicacion buscarPorId(Integer id) {
<<<<<<< HEAD
    return sessionFactory.getCurrentSession().get(Publicacion.class, id);
=======
    return this.sessionFactory.getCurrentSession().get(Publicacion.class, id);
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
  }

  @Override
  public List<Publicacion> obtenerTodas() {
<<<<<<< HEAD
    return sessionFactory
      .getCurrentSession()
      .createQuery("FROM Publicacion ORDER BY fechaCreacion DESC", Publicacion.class)
      .list();
=======
    return this.sessionFactory.getCurrentSession()
      .createQuery("FROM Publicacion ORDER BY fechaCreacion DESC", Publicacion.class)
      .getResultList();
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
  }
}
