package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.tallerwebi.dominio.Logro;
import com.tallerwebi.dominio.RepositorioUsuarioLogro;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.UsuarioLogro;
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
public class RepositorioUsuarioLogroTest {

  @Autowired
  private SessionFactory sessionFactory;

  private RepositorioUsuarioLogro repositorioUsuarioLogro;

  @BeforeEach
  public void init() {
    repositorioUsuarioLogro = new RepositorioUsuarioLogroImpl(sessionFactory);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaGuardarUnUsuarioLogroCorrectamente() {
    // preparacion
    Usuario usuario = dadoQueTengoUnUsuario("test@email.com");
    Logro logro = dadoQueTengoUnLogro("Primer Paso", "PRIMER_HABITO");
    dadoQueExisteElUsuario(usuario);
    dadoQueExisteElLogro(logro);
    UsuarioLogro usuarioLogro = dadoQueTengoUnUsuarioLogro(usuario, logro);

    // ejecucion
    cuandoGuardoElUsuarioLogro(usuarioLogro);

    // validacion
    UsuarioLogro obtenido = cuandoBuscoElUsuarioLogro(usuario, logro);
    entoncesElUsuarioLogroObtenidoEsCorrecto(usuarioLogro, obtenido);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaObtenerTodosLosLogrosDeUnUsuario() {
    // preparacion
    Usuario usuario = dadoQueTengoUnUsuario("test@email.com");
    Logro logro1 = dadoQueTengoUnLogro("Primer Paso", "PRIMER_HABITO");
    Logro logro2 = dadoQueTengoUnLogro("Constante", "CINCO_HABITOS");
    dadoQueExisteElUsuario(usuario);
    dadoQueExisteElLogro(logro1);
    dadoQueExisteElLogro(logro2);
    cuandoGuardoElUsuarioLogro(dadoQueTengoUnUsuarioLogro(usuario, logro1));
    cuandoGuardoElUsuarioLogro(dadoQueTengoUnUsuarioLogro(usuario, logro2));

    // ejecucion
    List<UsuarioLogro> obtenidos = repositorioUsuarioLogro.buscarPorUsuario(usuario);

    // validacion
    assertThat(obtenidos.size(), is(equalTo(2)));
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaRetornarTrueSiElUsuarioYaTieneElLogro() {
    // preparacion
    Usuario usuario = dadoQueTengoUnUsuario("test@email.com");
    Logro logro = dadoQueTengoUnLogro("Primer Paso", "PRIMER_HABITO");
    dadoQueExisteElUsuario(usuario);
    dadoQueExisteElLogro(logro);
    cuandoGuardoElUsuarioLogro(dadoQueTengoUnUsuarioLogro(usuario, logro));

    // ejecucion
    boolean existe = repositorioUsuarioLogro.existeLogroParaUsuario(usuario, logro);

    // validacion
    assertThat(existe, is(true));
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaRetornarFalseSiElUsuarioNoTieneElLogro() {
    // preparacion
    Usuario usuario = dadoQueTengoUnUsuario("test@email.com");
    Logro logro = dadoQueTengoUnLogro("Primer Paso", "PRIMER_HABITO");
    dadoQueExisteElUsuario(usuario);
    dadoQueExisteElLogro(logro);
    // no guardamos el usuarioLogro

    // ejecucion
    boolean existe = repositorioUsuarioLogro.existeLogroParaUsuario(usuario, logro);

    // validacion
    assertThat(existe, is(false));
  }

  // --- Métodos auxiliares ---

  private Usuario dadoQueTengoUnUsuario(String email) {
    Usuario usuario = new Usuario();
    usuario.setEmail(email);
    return usuario;
  }

  private Logro dadoQueTengoUnLogro(String nombre, String condicion) {
    Logro logro = new Logro();
    logro.setNombre(nombre);
    logro.setCondicion(condicion);
    return logro;
  }

  private UsuarioLogro dadoQueTengoUnUsuarioLogro(Usuario usuario, Logro logro) {
    UsuarioLogro usuarioLogro = new UsuarioLogro();
    usuarioLogro.setUsuario(usuario);
    usuarioLogro.setLogro(logro);
    return usuarioLogro;
  }

  private void dadoQueExisteElUsuario(Usuario usuario) {
    this.sessionFactory.getCurrentSession().save(usuario);
  }

  private void dadoQueExisteElLogro(Logro logro) {
    this.sessionFactory.getCurrentSession().save(logro);
  }

  private void cuandoGuardoElUsuarioLogro(UsuarioLogro usuarioLogro) {
    this.repositorioUsuarioLogro.guardar(usuarioLogro);
  }

  private UsuarioLogro cuandoBuscoElUsuarioLogro(Usuario usuario, Logro logro) {
    return (UsuarioLogro) this.sessionFactory.getCurrentSession()
      .createQuery("FROM UsuarioLogro WHERE usuario = :usuario AND logro = :logro")
      .setParameter("usuario", usuario)
      .setParameter("logro", logro)
      .getSingleResult();
  }

  private void entoncesElUsuarioLogroObtenidoEsCorrecto(
    UsuarioLogro esperado,
    UsuarioLogro obtenido
  ) {
    assertThat(obtenido.getUsuario().getEmail(), is(equalTo(esperado.getUsuario().getEmail())));
    assertThat(obtenido.getLogro().getNombre(), is(equalTo(esperado.getLogro().getNombre())));
    assertThat(obtenido.getLogro().getCondicion(), is(equalTo(esperado.getLogro().getCondicion())));
  }
}
