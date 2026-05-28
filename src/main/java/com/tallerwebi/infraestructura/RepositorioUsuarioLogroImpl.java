package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Logro;
import com.tallerwebi.dominio.RepositorioUsuarioLogro;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.UsuarioLogro;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioUsuarioLogro")
public class RepositorioUsuarioLogroImpl implements RepositorioUsuarioLogro {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioUsuarioLogroImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(UsuarioLogro usuarioLogro) {
    sessionFactory.getCurrentSession().save(usuarioLogro);
  }

  @Override
  public UsuarioLogro buscarPorUsuarioYLogro(Usuario usuario, Logro logro) {
    return (UsuarioLogro) sessionFactory
      .getCurrentSession()
      .createCriteria(UsuarioLogro.class)
      .add(Restrictions.eq("usuario", usuario))
      .add(Restrictions.eq("logro", logro))
      .uniqueResult();
  }
}
