package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.tallerwebi.dominio.Logro;
import com.tallerwebi.dominio.RepositorioLogro;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import java.util.List;
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
    // preparacion
    Logro logro = dadoQueTengoUnLogro("Primer Paso", "PRIMER_HABITO");

    // ejecucion
    cuandoGuardoUnLogro(logro);

    // validacion
    entoncesSeGuardoElLogro("Primer Paso", logro);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaEncontrarUnLogroCuandoLoBuscoPorSuNombre() {
    // preparacion
    Logro logro = dadoQueTengoUnLogro("Constante", "CINCO_HABITOS");
    dadoQueExisteElLogro(logro);

    // ejecucion
    Logro obtenido = cuandoBuscoUnLogro("Constante");

    // validacion
    entoncesElLogroObtenidoEsCorrecto(obtenido, logro);
  }

  @Test
  @Transactional
  public void noDeberiaEncontrarUnLogroInexistente() {
    // ejecucion
    Logro obtenido = cuandoBuscoUnLogro("Logro Inexistente");

    // validacion
    entoncesElLogroObtenidoEsNull(obtenido);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaObtenerTodosLosLogros() {
    // preparacion
    dadoQueExisteElLogro(dadoQueTengoUnLogro("Primer Paso", "PRIMER_HABITO"));
    dadoQueExisteElLogro(dadoQueTengoUnLogro("Constante", "CINCO_HABITOS"));
    dadoQueExisteElLogro(dadoQueTengoUnLogro("Experto", "DIEZ_HABITOS"));

    // ejecucion
    List<Logro> logros = repositorioLogro.obtenerTodos();

    // validacion
    assertThat(logros.size(), is(equalTo(3)));
  }

  // --- Métodos auxiliares ---

  private Logro dadoQueTengoUnLogro(String nombre, String condicion) {
    Logro logro = new Logro();
    logro.setNombre(nombre);
    logro.setDescripcion("Descripcion de " + nombre);
    logro.setCondicion(condicion);
    return logro;
  }

  private void dadoQueExisteElLogro(Logro logro) {
    this.sessionFactory.getCurrentSession().save(logro);
  }

  private void cuandoGuardoUnLogro(Logro logro) {
    repositorioLogro.guardar(logro);
  }

  private Logro cuandoBuscoUnLogro(String nombre) {
    return repositorioLogro.buscarPorNombre(nombre);
  }

  private void entoncesSeGuardoElLogro(String nombre, Logro logroEsperado) {
    Logro logroObtenido = repositorioLogro.buscarPorNombre(nombre);
    entoncesElLogroObtenidoEsCorrecto(logroObtenido, logroEsperado);
  }

  private void entoncesElLogroObtenidoEsCorrecto(Logro obtenido, Logro esperado) {
    assertThat(obtenido.getNombre(), is(equalTo(esperado.getNombre())));
    assertThat(obtenido.getCondicion(), is(equalTo(esperado.getCondicion())));
  }

  private void entoncesElLogroObtenidoEsNull(Logro obtenido) {
    assertThat(obtenido, is(nullValue()));
  }
}
