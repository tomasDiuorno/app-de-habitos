package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Recompensa;
import com.tallerwebi.dominio.RepositorioRecompensas;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioRecompensas")
public class RepositorioRecompensasImp implements RepositorioRecompensas {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioRecompensasImp(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public List<Recompensa> obtenerTodas() {
    return sessionFactory
      .getCurrentSession()
      .createQuery("FROM Recompensa", Recompensa.class)
      .getResultList();
  }
}
