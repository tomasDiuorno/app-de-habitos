package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.tallerwebi.dominio.entidades.RarezaEnum;
import com.tallerwebi.dominio.entidades.Recompensa;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioRecompensa;
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
public class RepositorioUsuarioRecompensaTest {

  @Autowired
  private SessionFactory sessionFactory;

  private RepositorioUsuarioRecompensaImp repositorioUsuarioRecompensa;

  @BeforeEach
  public void init() {
    repositorioUsuarioRecompensa = new RepositorioUsuarioRecompensaImp(sessionFactory);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaGuardarUnaRecompensaUsuario() {
    Usuario u = dadoQueTengoUnUsuarioConNivelDiez("user", "test@mail.com", 10);
    Recompensa r = dadoQueTengoUnaRecompensa(
      "recompensa",
      "Llegaste al nivel 5",
      "imagen",
      5,
      RarezaEnum.COMUN
    );
    this.dadoQueExisteElUsuario(u);
    this.dadoQueExisteLaRecompensa(r);

    UsuarioRecompensa ur = dadoQueTengoUnaRecompensaUsuario(u, r);

    repositorioUsuarioRecompensa.guardar(ur);
    this.entoncesDeberiaExistirLaRecompensaUsuario(ur);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaRetornarFalseCuandoLaRecompensaNoPerteneceAlUsuario() {
    Usuario usuario = dadoQueTengoUnUsuarioConNivelDiez("user", "test@mail.com", 10);

    Recompensa recompensa = dadoQueTengoUnaRecompensa(
      "recompensa",
      "descripcion",
      "imagen",
      5,
      RarezaEnum.COMUN
    );

    dadoQueExisteElUsuario(usuario);
    dadoQueExisteLaRecompensa(recompensa);

    Boolean existe = repositorioUsuarioRecompensa.existeRecompensaUsuario(recompensa, usuario);

    assertThat(existe, is(false));
  }

  private void entoncesDeberiaExistirLaRecompensaUsuario(UsuarioRecompensa ur) {
    Boolean existe = repositorioUsuarioRecompensa.existeRecompensaUsuario(
      ur.getRecompensa(),
      ur.getUsuario()
    );
    assertThat(existe, is(equalTo(true)));
  }

  private UsuarioRecompensa dadoQueTengoUnaRecompensaUsuario(Usuario u, Recompensa r) {
    UsuarioRecompensa ur = new UsuarioRecompensa();
    ur.setUsuario(u);
    ur.setRecompensa(r);
    return ur;
  }

  private void dadoQueExisteLaRecompensa(Recompensa r) {
    this.sessionFactory.getCurrentSession().save(r);
  }

  private void dadoQueExisteElUsuario(Usuario u) {
    this.sessionFactory.getCurrentSession().save(u);
  }

  private Recompensa dadoQueTengoUnaRecompensa(
    String nombre,
    String descripcion,
    String imagen,
    Integer nivelRequerido,
    RarezaEnum rareza
  ) {
    Recompensa r = new Recompensa();
    r.setNombre(nombre);
    r.setDescripcion(descripcion);
    r.setUrlImg(imagen);
    r.setNivelRequerido(nivelRequerido);
    r.setRareza(rareza);
    return r;
  }

  private Usuario dadoQueTengoUnUsuarioConNivelDiez(String nombre, String correo, Integer nivel) {
    Usuario u = new Usuario();
    u.setName(nombre);
    u.setEmail(correo);
    u.setNivelUsuario(nivel);
    return u;
  }
}
