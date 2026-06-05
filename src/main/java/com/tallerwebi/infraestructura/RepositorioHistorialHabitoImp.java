package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.HistorialHabito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.RepositorioHistorialHabito;

import java.time.LocalDate;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioHistorialHabito")
public class RepositorioHistorialHabitoImp implements RepositorioHistorialHabito {

  @Autowired
  private SessionFactory sessionFactory;

  public RepositorioHistorialHabitoImp(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(HistorialHabito historialHabito) {
    sessionFactory.getCurrentSession().save(historialHabito);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<HistorialHabito> obtenerPorUsuario(Usuario usuario) {
    return sessionFactory
      .getCurrentSession()
      .createCriteria(HistorialHabito.class)
      .add(Restrictions.eq("usuario", usuario))
      .list();
  }

  @Override
  public HistorialHabito obtenerPorUsuarioHabitoYFecha(
    Usuario usuario,
    Habito habito,
    LocalDate fecha
  ) {
    return (HistorialHabito) sessionFactory
      .getCurrentSession()
      .createCriteria(HistorialHabito.class)
      .add(Restrictions.eq("usuario", usuario))
      .add(Restrictions.eq("habito", habito))
      .add(Restrictions.eq("fechaCompletado", fecha))
      .uniqueResult();
  }
}
