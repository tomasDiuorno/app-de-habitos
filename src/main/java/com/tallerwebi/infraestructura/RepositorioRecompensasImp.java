package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Recompensa;
import com.tallerwebi.dominio.interfaz.RepositorioRecompensas;
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

  @Override
  public List<Recompensa> obtenerPorNivel(Integer nivelUsuario) {
    String query = "FROM Recompensa WHERE nivelRequerido <= :nivelUsuario";
    return sessionFactory
      .getCurrentSession()
      .createQuery(query, Recompensa.class)
      .setParameter("nivelUsuario", nivelUsuario)
      .getResultList();
  }
}
