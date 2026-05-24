package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.RepositorioHabito;
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

  @Test
  @Transactional
  @Rollback
  public void deberiaEncontrarUnHabitoCuandoLoBuscoPorSuTituloYCategoria() {
    String titulo = "Meditar";
    String categoria = "Bienestar";
    Habito habito = dadoQuetengoUnHabito(titulo, categoria);
    this.dadoQueExisteElHabito(habito);

    Habito obtenido = this.cuandoBuscoUnHabito(titulo, categoria);

    this.entoncesElUsuarioObtenidoEsCorrecto(obtenido, habito);
  }

  @Test
  @Transactional
  public void noDeberiaEncontrarUnHabitoInexistenteCuandoLoBuscoPorTituloYCategoria() {
    Habito obtenido = this.cuandoBuscoUnHabito("Prueba", "Test");
    this.entoncesElHabitoObtenidoEsNull(obtenido);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaModificarUnHabitoExistente() {
    String titulo = "Gym";
    String categoria = "Deporte";
    Habito habito = this.dadoQueTengoUnHabito(titulo, categoria);
    this.dadoQueExisteElHabito(habito);

    habito.setCategoria("Salud");

    this.cuandoLoModifico(habito);

    Habito obtenido = obtengoElHabitoPorTitulo(titulo);
    this.entoncesElHabitoObtenidoEsCorrecto(obtenido, habito);
  }

  private Habito obtengoElHabitoPorTitulo(String titulo) {
    return this.repositorioHabito.buscarPorTitulo(titulo);
  }

  private void cuandoLoModifico(Habito habito) {
    this.repositorioHabito.modificar(habito);
  }

  private void entoncesElHabitoObtenidoEsNull(Habito obtenido) {
    assertThat(obtenido, is(nullValue()));
  }

  private void entoncesElUsuarioObtenidoEsCorrecto(Habito obtenido, Habito habito) {
    assertThat(obtenido.getTitulo(), is(equalTo(habito.getTitulo())));
    assertThat(obtenido.getCategoria(), is(equalTo(habito.getCategoria())));
  }

  private void dadoQueExisteElHabito(Habito habito) {
    this.sessionFactory.getCurrentSession().save(habito);
  }

  private Habito cuandoBuscoUnHabito(String titulo, String categoria) {
    return this.repositorioHabito.buscarHabito(titulo, categoria);
  }

  private Habito dadoQuetengoUnHabito(String titulo, String categoria) {
    Habito habito = new Habito();
    habito.setTitulo(titulo);
    habito.setCategoria(categoria);
    return habito;
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
