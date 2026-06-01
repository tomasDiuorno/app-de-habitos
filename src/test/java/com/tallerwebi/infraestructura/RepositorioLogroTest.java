package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.tallerwebi.dominio.Logro;
import com.tallerwebi.dominio.RepositorioLogro;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateInfraestructuraTestConfig.class })
public class RepositorioLogroTest {

  @Autowired
  private SessionFactory sessionFactory;

  private RepositorioLogro repositorioLogro;

  @BeforeEach
  public void init() {
    repositorioLogro = new RepositorioLogroImpl(sessionFactory);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaGuardarUnNuevoLogro() {
    Logro logro = dadoQueTengoUnLogro("Primer hábito creado", "Creaste tu primer hábito");

    cuandoGuardoUnLogro(logro);

    entoncesSeGuardoElLogro(logro);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaEncontrarUnLogroCuandoLoBuscoPorNombre() {
    Logro logro = dadoQueTengoUnLogro("Primer hábito creado", "Creaste tu primer hábito");

    dadoQueExisteElLogro(logro);

    Logro obtenido = cuandoBuscoUnLogro("Primer hábito creado");

    entoncesElLogroObtenidoEsCorrecto(logro, obtenido);
  }

  @Test
  @Transactional
  public void noDeberiaEncontrarUnLogroInexistente() {
    Logro obtenido = cuandoBuscoUnLogro("Logro inexistente");

    assertThat(obtenido, is(nullValue()));
  }

  private Logro dadoQueTengoUnLogro(String nombre, String descripcion) {
    Logro logro = new Logro();
    logro.setNombre(nombre);
    logro.setDescripcion(descripcion);
    return logro;
  }

  private void cuandoGuardoUnLogro(Logro logro) {
    repositorioLogro.guardar(logro);
  }

  private void dadoQueExisteElLogro(Logro logro) {
    sessionFactory.getCurrentSession().save(logro);
  }

  private Logro cuandoBuscoUnLogro(String nombre) {
    return repositorioLogro.buscarPorNombre(nombre);
  }

  private void entoncesSeGuardoElLogro(Logro logroEsperado) {
    Query query = sessionFactory
      .getCurrentSession()
      .createQuery("FROM Logro WHERE nombre = :nombre");

    query.setParameter("nombre", logroEsperado.getNombre());

    Logro logroObtenido = (Logro) query.getSingleResult();

    entoncesElLogroObtenidoEsCorrecto(logroEsperado, logroObtenido);
  }

  private void entoncesElLogroObtenidoEsCorrecto(Logro esperado, Logro obtenido) {
    assertThat(obtenido.getNombre(), is(equalTo(esperado.getNombre())));

    assertThat(obtenido.getDescripcion(), is(equalTo(esperado.getDescripcion())));
  }
}
