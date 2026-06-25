package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Publicacion;
import com.tallerwebi.dominio.interfaz.RepositorioPublicacion;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioPublicacion")
public class RepositorioPublicacionImpl implements RepositorioPublicacion {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioPublicacionImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(Publicacion publicacion) {
    sessionFactory.getCurrentSession().save(publicacion);
  }

  @Override
  public Publicacion buscarPorId(Integer id) {
    return sessionFactory.getCurrentSession().get(Publicacion.class, id);
  }

  @Override
  public List<Publicacion> obtenerTodas() {
    return sessionFactory
      .getCurrentSession()
      .createQuery("FROM Publicacion ORDER BY fechaCreacion DESC", Publicacion.class)
      .list();
  }
}
