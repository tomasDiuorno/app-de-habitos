package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.RepositorioHabito;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioHabito")
public class RepositorioHabitoImpl implements RepositorioHabito {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioHabitoImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public Habito buscarPorTitulo(String titulo) {
    return (Habito) sessionFactory
      .getCurrentSession()
      .createCriteria(Habito.class)
      .add(Restrictions.eq("titulo", titulo))
      .uniqueResult();
  }

  @Override
  public void guardar(Habito habito) {
    sessionFactory.getCurrentSession().save(habito);
  }

  @Override
  public void modificar(Habito habito) {
    sessionFactory.getCurrentSession().update(habito);
  }

  @Override
  public List<Habito> obtenerHabitosIniciales() {
    return (List<Habito>) sessionFactory
      .getCurrentSession()
      .createQuery("FROM Habito", Habito.class)
      .getResultList();
  }

  @Override
  public List<Habito> buscarPorIds(List<Integer> ids) {
    return sessionFactory
      .getCurrentSession()
      .createQuery("FROM Habito WHERE id IN (:ids)", Habito.class)
      .setParameterList("ids", ids)
      .getResultList();
  }

  @Override
  public Habito buscarHabitoPorId(Integer id) {
    return (Habito) sessionFactory
      .getCurrentSession()
      .createCriteria(Habito.class)
      .add(Restrictions.eq("id", id))
      .uniqueResult();
  }
}
