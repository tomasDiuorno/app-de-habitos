package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.UsuarioBonificacion;
import com.tallerwebi.dominio.interfaz.RepositorioUsuarioBonificacion;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioUsuarioBonificacion")
public class RepositorioUsuarioBonificacionImp implements RepositorioUsuarioBonificacion {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioUsuarioBonificacionImp(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public UsuarioBonificacion buscarActivaPorUsuarioId(Integer usuarioId) {
    String hql =
      "FROM UsuarioBonificacion ub " +
      "WHERE ub.usuario.id = :usuarioId " +
      "AND ub.activa = true " +
      "AND ub.fechaExpiracion > :fechaActual";

    List<UsuarioBonificacion> resultados = sessionFactory
      .getCurrentSession()
      .createQuery(hql, UsuarioBonificacion.class)
      .setParameter("usuarioId", usuarioId)
      .setParameter("fechaActual", LocalDateTime.now())
      .getResultList();

    if (resultados.isEmpty()) {
      return null;
    }

    return resultados.get(0);
  }

  @Override
  public void guardar(UsuarioBonificacion usuarioBonificacion) {
    sessionFactory.getCurrentSession().save(usuarioBonificacion);
  }

  @Override
  public void modificar(UsuarioBonificacion usuarioBonificacion) {
    sessionFactory.getCurrentSession().update(usuarioBonificacion);
  }
}
