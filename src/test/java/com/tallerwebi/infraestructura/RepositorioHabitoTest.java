package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.beans.Transient;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.RepositorioHabito;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateInfraestructuraTestConfig.class })
public class RepositorioHabitoTest {
    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioHabito repositorioHabito;
    
    @BeforeEach
    public void init() {
        repositorioHabito = new RepositorioHabitoImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaGuardarUnNuevoHabito() {
        // preparacion
        String titulo = "Hacer ejercicio";
        Habito habito = this.dadoQueTengoUnHabito(titulo, "Salud");

        // ejecucion
        this.cuandoGuardoUnHabito(habito);

        // validacion
        this.entoncesSeGuardoElHabito(titulo, habito);
    
    }

    private void entoncesSeGuardoElHabito(String titulo, Habito habitoEsperado) {
        String hql = "FROM Habito WHERE titulo = :titulo";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("titulo", titulo);
        Habito habitoObtenido = (Habito) query.getSingleResult();
        this.entoncesElHabitoObtenidoEsCorrecto(habitoEsperado, habitoObtenido);
    }

    private void entoncesElHabitoObtenidoEsCorrecto(Habito habitoEsperado, Habito habitoObtenido) {
        assertThat(habitoObtenido.getTitulo(), is(equalTo(habitoEsperado.getTitulo())));
        assertThat(habitoObtenido.getCategoria(), is(equalTo(habitoEsperado.getCategoria())));
    }

    private void cuandoGuardoUnHabito(Habito habito) {
        repositorioHabito.guardar(habito);    
    }

    private Habito dadoQueTengoUnHabito(String titulo, String categoria) {
        Habito habito = new Habito();
        habito.setTitulo(titulo);
        habito.setCategoria(categoria);
        return habito;
    }
}
