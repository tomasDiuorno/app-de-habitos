package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Logro;
import com.tallerwebi.dominio.RepositorioLogro;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioLogro")
public class RepositorioLogroImpl implements RepositorioLogro {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioLogroImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(Logro logro) {
    sessionFactory.getCurrentSession().save(logro);
  }

  @Override
  public Logro buscarPorNombre(String nombre) {
  return (Logro) this.sessionFactory
    .getCurrentSession()
    .createCriteria(Logro.class)
    .add(Restrictions.eq("nombre", nombre))
    .uniqueResult();
  }
}
