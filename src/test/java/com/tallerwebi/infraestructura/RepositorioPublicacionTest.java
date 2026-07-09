package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Publicacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.RepositorioPublicacion;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import java.time.LocalDateTime;
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
public class RepositorioPublicacionTest {

  @Autowired
  private SessionFactory sessionFactory;

  private RepositorioPublicacion repositorioPublicacion;

  @BeforeEach
  public void init() {
    repositorioPublicacion = new RepositorioPublicacionImpl(sessionFactory);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaGuardarYRecuperarUnaPublicacionVinculadaAUnHabitoGrupal() {
    Usuario autor = this.dadoQueTengoUnUsuario("creador@test.com");
    this.dadoQueExisteElUsuario(autor);

    Habito habito = this.dadoQueTengoUnHabitoGrupal("Meditar en grupo");
    this.dadoQueExisteElHabito(habito);

    Publicacion publicacion = this.dadoQueTengoUnaPublicacionAsociada(habito, autor);

    this.cuandoGuardoLaPublicacion(publicacion);

    Publicacion obtenida = this.cuandoBuscoLaPublicacionPorId(publicacion.getId());

    this.entoncesLaPublicacionObtenidaEsCorrecta(obtenida, habito);
  }

  @Test
  @Transactional
  @Rollback
  public void unaPublicacionSinHabitoAsociadoDeberiaPersistirseConNull() {
    Usuario autor = this.dadoQueTengoUnUsuario("autor@test.com");
    this.dadoQueExisteElUsuario(autor);

    Publicacion publicacion = new Publicacion();
    publicacion.setTitulo("Publicación normal");
    publicacion.setContenido("Sin hábito asociado");
    publicacion.setAutor(autor);
    publicacion.setFechaCreacion(LocalDateTime.now());

    this.cuandoGuardoLaPublicacion(publicacion);

    Publicacion obtenida = this.cuandoBuscoLaPublicacionPorId(publicacion.getId());

    assertThat(obtenida.getHabitoAsociado(), is((Habito) null));
  }

  private Usuario dadoQueTengoUnUsuario(String email) {
    Usuario usuario = new Usuario();
    usuario.setEmail(email);
    return usuario;
  }

  private void dadoQueExisteElUsuario(Usuario usuario) {
    this.sessionFactory.getCurrentSession().save(usuario);
  }

  private Habito dadoQueTengoUnHabitoGrupal(String titulo) {
    Habito habito = new Habito();
    habito.setTitulo(titulo);
    habito.setEsGrupal(true);
    return habito;
  }

  private void dadoQueExisteElHabito(Habito habito) {
    this.sessionFactory.getCurrentSession().save(habito);
  }

  private Publicacion dadoQueTengoUnaPublicacionAsociada(Habito habito, Usuario autor) {
    Publicacion publicacion = new Publicacion();
    publicacion.setTitulo("¡Únete al reto: " + habito.getTitulo() + "!");
    publicacion.setContenido("Sumate a este hábito grupal");
    publicacion.setAutor(autor);
    publicacion.setFechaCreacion(LocalDateTime.now());
    publicacion.setHabitoAsociado(habito);
    return publicacion;
  }

  private void cuandoGuardoLaPublicacion(Publicacion publicacion) {
    repositorioPublicacion.guardar(publicacion);
  }

  private Publicacion cuandoBuscoLaPublicacionPorId(Integer id) {
    return repositorioPublicacion.buscarPorId(id);
  }

  private void entoncesLaPublicacionObtenidaEsCorrecta(
    Publicacion obtenida,
    Habito habitoEsperado
  ) {
    assertThat(obtenida.getHabitoAsociado().getTitulo(), is(equalTo(habitoEsperado.getTitulo())));
    assertThat(obtenida.getHabitoAsociado().getEsGrupal(), is(true));
  }
}
