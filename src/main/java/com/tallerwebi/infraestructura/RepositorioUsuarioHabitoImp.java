package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.UsuarioHabito;
import com.tallerwebi.dominio.interfaz.RepositorioUsuarioHabito;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioUsuarioHabito")
public class RepositorioUsuarioHabitoImp implements RepositorioUsuarioHabito {

  @Autowired
  private SessionFactory sessionFactory;

  public RepositorioUsuarioHabitoImp(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(UsuarioHabito usuarioHabito) {
    this.sessionFactory.getCurrentSession().save(usuarioHabito);
  }

  @Override
  public UsuarioHabito obtenerPorIds(Integer usuarioId, Integer habitoId) {
    String hql = "FROM UsuarioHabito WHERE habito_id = :habitoId AND usuario_id = :usuarioId";
    return this.sessionFactory.getCurrentSession()
      .createQuery(hql, UsuarioHabito.class)
      .setParameter("habitoId", habitoId)
      .setParameter("usuarioId", usuarioId)
      .getSingleResult();
  }
}
