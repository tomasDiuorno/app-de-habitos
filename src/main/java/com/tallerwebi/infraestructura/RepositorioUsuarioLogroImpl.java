package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Logro;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioLogro;
import com.tallerwebi.dominio.interfaz.RepositorioUsuarioLogro;
import java.util.List;
import org.hibernate.SessionFactory;
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
  public List<UsuarioLogro> buscarPorUsuario(Usuario usuario) {
    return sessionFactory
      .getCurrentSession()
      .createQuery("FROM UsuarioLogro ul WHERE ul.usuario.email = :email", UsuarioLogro.class)
      .setParameter("email", usuario.getEmail())
      .list();
  }

  @Override
  public boolean existeLogroParaUsuario(Usuario usuario, Logro logro) {
    Long count = (Long) sessionFactory
      .getCurrentSession()
      .createQuery(
        "SELECT COUNT(ul) FROM UsuarioLogro ul " +
        "WHERE ul.usuario.email = :email AND ul.logro = :logro"
      )
      .setParameter("email", usuario.getEmail())
      .setParameter("logro", logro)
      .uniqueResult();
    return count != null && count > 0;
  }
}
