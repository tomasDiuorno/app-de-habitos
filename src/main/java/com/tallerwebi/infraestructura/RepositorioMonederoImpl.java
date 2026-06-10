package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Monedero;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.RepositorioMonedero;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioMonedero")
public class RepositorioMonederoImpl implements RepositorioMonedero {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioMonederoImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(Monedero monedero) {
    sessionFactory.getCurrentSession().saveOrUpdate(monedero);
  }

  @Override
  public void guardarTransaccion(Transaccion transaccion) {
    sessionFactory.getCurrentSession().save(transaccion);
  }

  @Override
  public Monedero buscarPorUsuario(Usuario usuario) {
    return (Monedero) sessionFactory
      .getCurrentSession()
      .createQuery("FROM Monedero m WHERE m.usuario.email = :email")
      .setParameter("email", usuario.getEmail())
      .uniqueResult();
  }
}
