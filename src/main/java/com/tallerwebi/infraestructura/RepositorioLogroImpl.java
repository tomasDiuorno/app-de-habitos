package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Logro;
import com.tallerwebi.dominio.RepositorioLogro;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("repositorioLogro")
public class RepositorioLogroImpl implements RepositorioLogro {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioLogroImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Logro logro) {
        sessionFactory.getCurrentSession().save(logro);
    }

    @Override
    public Logro buscarPorNombre(String nombre) {
        return (Logro) sessionFactory.getCurrentSession()
            .createQuery("FROM Logro WHERE nombre = :nombre")
            .setParameter("nombre", nombre)
            .uniqueResult();
    }

    @Override
    public List<Logro> obtenerTodos() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Logro", Logro.class)
            .list();
    }
}