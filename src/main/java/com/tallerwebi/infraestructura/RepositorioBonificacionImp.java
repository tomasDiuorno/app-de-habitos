package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Bonificacion;
import com.tallerwebi.dominio.interfaz.RepositorioBonificacion;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioBonificacion")
public class RepositorioBonificacionImp implements RepositorioBonificacion {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioBonificacionImp(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public List<Bonificacion> buscarDisponibles() {
    String hql = "FROM Bonificacion b WHERE b.disponible = true ORDER BY b.porcentaje ASC";

    return sessionFactory.getCurrentSession().createQuery(hql, Bonificacion.class).getResultList();
  }

  @Override
  public Bonificacion buscarPorId(Integer id) {
    return sessionFactory.getCurrentSession().get(Bonificacion.class, id);
  }

  @Override
  public void guardar(Bonificacion bonificacion) {
    sessionFactory.getCurrentSession().saveOrUpdate(bonificacion);
  }
}
