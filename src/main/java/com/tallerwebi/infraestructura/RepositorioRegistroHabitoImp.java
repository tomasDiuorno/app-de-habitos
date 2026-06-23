package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.RegistroHabito;
import com.tallerwebi.dominio.interfaz.RepositorioRegistroHabito;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioRegistroHabito")
public class RepositorioRegistroHabitoImp implements RepositorioRegistroHabito {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioRegistroHabitoImp(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(RegistroHabito registroHabito) {
    this.sessionFactory.getCurrentSession().save(registroHabito);
  }
}
