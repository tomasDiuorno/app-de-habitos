package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.RepositorioUsuarioHabito;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.UsuarioHabito;
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
public class RepositorioUsuarioHabitoTest {

  @Autowired
  private SessionFactory sessionFactory;

  private RepositorioUsuarioHabito repositorioUsuarioHabito;

  @BeforeEach
  public void init() {
    repositorioUsuarioHabito = new RepositorioUsuarioHabitoImp(sessionFactory);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaGuardarUnHabitoUsuarioCorrectamente() {
    Usuario us = dadoQueTengoUnUsuario("test@email.com", "userTest");
    Habito hab = dadoQueTengoElHabito("titulo al Gym");
    this.cuandoGuardoElHabito(hab);
    this.cuandoGuardoElUsuario(us);
    UsuarioHabito usuarioHabito = dadoQueTengoUnUsuarioHabito(us, hab);
    this.cuandoGuardoElUsuarioHabito(usuarioHabito);

    UsuarioHabito obtenido = this.cuandoBuscoUnUsuarioHabito(us, hab);

    this.entoncesElUsuarioHabitoObtenidoEsCorrecto(usuarioHabito, obtenido);
  }

  private void entoncesElUsuarioHabitoObtenidoEsCorrecto(
    UsuarioHabito usuarioHabito,
    UsuarioHabito obtenido
  ) {
    assertThat(
      obtenido.getHabito().getTitulo(),
      is(equalTo(usuarioHabito.getHabito().getTitulo()))
    );

    assertThat(
      obtenido.getUsuario().getEmail(),
      is(equalTo(usuarioHabito.getUsuario().getEmail()))
    );
    assertThat(
      obtenido.getUsuario().getUsername(),
      is(equalTo(usuarioHabito.getUsuario().getUsername()))
    );
  }

  private UsuarioHabito cuandoBuscoUnUsuarioHabito(Usuario us, Habito hab) {
    return (UsuarioHabito) this.sessionFactory.getCurrentSession()
      .createQuery("FROM UsuarioHabito WHERE usuario = :usuario AND habito = :habito")
      .setParameter("usuario", us)
      .setParameter("habito", hab)
      .getSingleResult();
  }

  private void cuandoGuardoElUsuarioHabito(UsuarioHabito usuarioHabito) {
    this.repositorioUsuarioHabito.guardar(usuarioHabito);
  }

  private UsuarioHabito dadoQueTengoUnUsuarioHabito(Usuario usuario, Habito habito) {
    UsuarioHabito usuarioHabito = new UsuarioHabito();
    usuarioHabito.setUsuario(usuario);
    usuarioHabito.setHabito(habito);
    return usuarioHabito;
  }

  private void cuandoGuardoElHabito(Habito hab) {
    this.sessionFactory.getCurrentSession().save(hab);
  }

  private Habito dadoQueTengoElHabito(String titulo) {
    Habito habito = new Habito();
    habito.setTitulo(titulo);
    return habito;
  }

  private void cuandoGuardoElUsuario(Usuario us) {
    this.sessionFactory.getCurrentSession().save(us);
  }

  private Usuario dadoQueTengoUnUsuario(String email, String username) {
    Usuario usuario = new Usuario();
    usuario.setEmail(email);
    usuario.setUsername(username);
    return usuario;
  }
}
