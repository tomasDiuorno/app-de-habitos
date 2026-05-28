package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuarioHabito;
import com.tallerwebi.dominio.UsuarioHabito;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioUsuarioHabito")
public class RepositorioUsuarioHabitoImp implements RepositorioUsuarioHabito {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioUsuarioHabitoImp(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(UsuarioHabito usuarioHabito) {
    this.sessionFactory.getCurrentSession().save(usuarioHabito);
  }
}
