package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.tallerwebi.dominio.Logro;
import com.tallerwebi.dominio.RepositorioUsuarioLogro;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.UsuarioLogro;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
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
  public void deberiaGuardarUsuarioLogroCorrectamente() {
    Usuario usuario = dadoQueTengoUnUsuario();
    Logro logro = dadoQueTengoUnLogro();

    guardarUsuario(usuario);
    guardarLogro(logro);

    UsuarioLogro usuarioLogro = dadoQueTengoUnUsuarioLogro(usuario, logro);

    cuandoGuardoUsuarioLogro(usuarioLogro);

    UsuarioLogro obtenido = cuandoBuscoUsuarioLogro(usuario, logro);

    entoncesUsuarioLogroEsCorrecto(usuarioLogro, obtenido);
  }

  private Usuario dadoQueTengoUnUsuario() {
    Usuario usuario = new Usuario();
    usuario.setEmail("test@mail.com");

    return usuario;
  }

  private Logro dadoQueTengoUnLogro() {
    Logro logro = new Logro();
    logro.setNombre("Primer hábito creado");
    logro.setDescripcion("Crear un hábito");

    return logro;
  }

  private UsuarioLogro dadoQueTengoUnUsuarioLogro(Usuario usuario, Logro logro) {
    UsuarioLogro usuarioLogro = new UsuarioLogro();

    usuarioLogro.setUsuario(usuario);
    usuarioLogro.setLogro(logro);
    usuarioLogro.setDesbloqueado(true);

    return usuarioLogro;
  }

  private void guardarUsuario(Usuario usuario) {
    sessionFactory.getCurrentSession().save(usuario);
  }

  private void guardarLogro(Logro logro) {
    sessionFactory.getCurrentSession().save(logro);
  }

  private void cuandoGuardoUsuarioLogro(UsuarioLogro usuarioLogro) {
    repositorioUsuarioLogro.guardar(usuarioLogro);
  }

  private UsuarioLogro cuandoBuscoUsuarioLogro(Usuario usuario, Logro logro) {
    return (UsuarioLogro) sessionFactory
      .getCurrentSession()
      .createQuery("FROM UsuarioLogro WHERE usuario = :usuario AND logro = :logro")
      .setParameter("usuario", usuario)
      .setParameter("logro", logro)
      .getSingleResult();
  }

  private void entoncesUsuarioLogroEsCorrecto(UsuarioLogro esperado, UsuarioLogro obtenido) {
    assertThat(obtenido.getUsuario().getEmail(), is(equalTo(esperado.getUsuario().getEmail())));

    assertThat(obtenido.getLogro().getNombre(), is(equalTo(esperado.getLogro().getNombre())));

    assertThat(obtenido.getDesbloqueado(), is(true));
  }
}
