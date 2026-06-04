package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.HistorialHabito;
import com.tallerwebi.dominio.RepositorioHistorialHabito;
import com.tallerwebi.dominio.Usuario;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioHistorialHabito")
public class RepositorioHistorialHabitoImp implements RepositorioHistorialHabito {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioHistorialHabitoImp(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(HistorialHabito historialHabito) {
    sessionFactory.getCurrentSession().save(historialHabito);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<HistorialHabito> obtenerPorUsuario(Usuario usuario) {
    Session session = sessionFactory.getCurrentSession();
    return session.createCriteria(HistorialHabito.class)
        .add(Restrictions.eq("usuario", usuario))
        .list();
  }

  @Override
  public HistorialHabito obtenerPorUsuarioHabitoYFecha(Usuario usuario, Habito habito, LocalDate fecha) {
    Session session = sessionFactory.getCurrentSession();
    return (HistorialHabito) session.createCriteria(HistorialHabito.class)
        .add(Restrictions.eq("usuario", usuario))
        .add(Restrictions.eq("habito", habito))
        .add(Restrictions.eq("fechaCompletado", fecha))
        .uniqueResult();
  }
}
