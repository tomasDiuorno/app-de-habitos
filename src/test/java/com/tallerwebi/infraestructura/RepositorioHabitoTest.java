package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.RepositorioHabito;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import java.util.Arrays;
import java.util.List;
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
    String categoria = "Deporte";
    Categoria cat = dadoQueTengoUnaCategoria(categoria);
    this.dadoQueExisteLaCategoria(cat);
    Habito habito = this.dadoQueTengoUnHabito(titulo, cat);

    // ejecucion
    this.cuandoGuardoUnHabito(habito);

    // validacion
    this.entoncesSeGuardoElHabito(titulo, habito);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaEncontrarUnHabitoCuandoLoBuscoPorSuTitulo() {
    String titulo = "Meditar";

    Habito habito = dadoQuetengoUnHabito(titulo);
    this.dadoQueExisteElHabito(habito);

    Habito obtenido = this.cuandoBuscoUnHabito(titulo);

    this.entoncesElUsuarioObtenidoEsCorrecto(obtenido, habito);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaEncontrarUnHabitoCuandoLoBuscoPorSuId() {
    String titulo = "Meditar";

    Habito habito = dadoQuetengoUnHabito(titulo);
    this.dadoQueExisteElHabito(habito);

    Habito obtenido = this.cuandoBuscoUnHabitoPorId(habito.getId());

    this.entoncesElUsuarioObtenidoEsCorrecto(obtenido, habito);
  }

  @Test
  @Transactional
  public void noDeberiaEncontrarUnHabitoInexistenteCuandoLoBuscoPorTituloYCategoria() {
    Habito obtenido = this.cuandoBuscoUnHabito("Prueba");
    this.entoncesElHabitoObtenidoEsNull(obtenido);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaModificarUnHabitoExistente() {
    String titulo = "Gym";
    String categoria = "Deporte";
    Categoria cat = dadoQueTengoUnaCategoria(categoria);
    this.dadoQueExisteLaCategoria(cat);
    Habito habito = this.dadoQueTengoUnHabito(titulo, cat);
    this.dadoQueExisteElHabito(habito);

    habito.setCategoria(cat);

    this.cuandoLoModifico(habito);

    Habito obtenido = obtengoElHabitoPorTitulo(titulo);
    this.entoncesElHabitoObtenidoEsCorrecto(obtenido, habito);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaObtenerHabitosAlBuscarlosPorSusIds() {
    Categoria categoria = this.dadoQueTengoUnaCategoria("Deporte");
    this.dadoQueExisteLaCategoria(categoria);
    Habito habito1 = this.dadoQueTengoUnHabito("Gymnasio", categoria);
    Habito habito2 = this.dadoQueTengoUnHabito("Andar en bicicleta", categoria);

    this.dadoQueExisteElHabito(habito1);
    this.dadoQueExisteElHabito(habito2);

    List<Habito> obtenidos = this.cuandoObtendoLosHabitosPorIds(habito1, habito2);

    this.entoncesLosHabitosObtenidosSonCorrectos(obtenidos, habito1, habito2);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaObtenerTodosLosHabitosIniciales() {
    Categoria categoria = dadoQueTengoUnaCategoria("Bienestar");
    dadoQueExisteLaCategoria(categoria);

    dadoQueExisteElHabito(dadoQueTengoUnHabito("Meditar", categoria));
    dadoQueExisteElHabito(dadoQueTengoUnHabito("Leer un libro", categoria));
    dadoQueExisteElHabito(dadoQueTengoUnHabito("Hacer ejercicio", categoria));

    List<Habito> habitosIniciales = repositorioHabito.obtenerHabitosIniciales();

    assertThat(habitosIniciales.get(0).getTitulo(), is(equalTo("Meditar")));
    assertThat(habitosIniciales.get(1).getTitulo(), is(equalTo("Leer un libro")));
    assertThat(habitosIniciales.get(2).getTitulo(), is(equalTo("Hacer ejercicio")));
  }

  private void entoncesLosHabitosObtenidosSonCorrectos(
    List<Habito> obtenidos,
    Habito habito1,
    Habito habito2
  ) {
    assertThat(obtenidos.size(), is(equalTo(2)));
    assertThat(obtenidos.contains(habito1), is(true));
    assertThat(obtenidos.contains(habito2), is(true));
  }

  private List<Habito> cuandoObtendoLosHabitosPorIds(Habito habito1, Habito habito2) {
    return this.repositorioHabito.buscarPorIds(Arrays.asList(habito1.getId(), habito2.getId()));
  }

  private void dadoQueExisteLaCategoria(Categoria cat) {
    this.sessionFactory.getCurrentSession().save(cat);
  }

  private Categoria dadoQueTengoUnaCategoria(String categoria) {
    Categoria cat = new Categoria();
    cat.setNombre(categoria);
    return cat;
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

  private Habito cuandoBuscoUnHabito(String titulo) {
    return this.repositorioHabito.buscarPorTitulo(titulo);
  }

  private Habito cuandoBuscoUnHabitoPorId(Integer id) {
    return this.repositorioHabito.buscarPorId(id);
  }

  private Habito dadoQuetengoUnHabito(String titulo) {
    Habito habito = new Habito();
    habito.setTitulo(titulo);
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

  private Habito dadoQueTengoUnHabito(String titulo, Categoria categoria) {
    Habito habito = new Habito();
    habito.setTitulo(titulo);
    habito.setCategoria(categoria);
    return habito;
  }
}
