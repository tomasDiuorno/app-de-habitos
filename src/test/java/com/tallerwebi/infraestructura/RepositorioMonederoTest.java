package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.tallerwebi.dominio.entidades.Monedero;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.RepositorioMonedero;
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
public class RepositorioMonederoTest {

  @Autowired
  private SessionFactory sessionFactory;

  private RepositorioMonedero repositorioMonedero;

  @BeforeEach
  public void init() {
    repositorioMonedero = new RepositorioMonederoImpl(sessionFactory);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaGuardarUnMonederoCorrectamente() {
    Usuario usuario = dadoQueTengoUnUsuario("test@mail.com");
    dadoQueExisteElUsuario(usuario);
    Monedero monedero = dadoQueTengoUnMonedero(usuario);

    cuandoGuardoElMonedero(monedero);

    Monedero obtenido = repositorioMonedero.buscarPorUsuario(usuario);
    entoncesElMonederoObtenidoEsCorrecto(obtenido, monedero);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaRetornarNullSiElUsuarioNoTieneMonedero() {
    Usuario usuario = dadoQueTengoUnUsuario("sinmonedero@mail.com");
    dadoQueExisteElUsuario(usuario);

    Monedero obtenido = repositorioMonedero.buscarPorUsuario(usuario);

    assertThat(obtenido, is(nullValue()));
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaGuardarUnaTransaccionAsociadaAlMonedero() {
    Usuario usuario = dadoQueTengoUnUsuario("test@mail.com");
    dadoQueExisteElUsuario(usuario);
    Monedero monedero = dadoQueTengoUnMonedero(usuario);
    cuandoGuardoElMonedero(monedero);

    Transaccion transaccion = dadoQueTengoUnaTransaccion(monedero, 20, "LOGRO");

    repositorioMonedero.guardarTransaccion(transaccion);

    Monedero obtenido = repositorioMonedero.buscarPorUsuario(usuario);
    assertThat(obtenido, is(notNullValue()));
  }

  private Usuario dadoQueTengoUnUsuario(String email) {
    Usuario usuario = new Usuario();
    usuario.setEmail(email);
    return usuario;
  }

  private void dadoQueExisteElUsuario(Usuario usuario) {
    this.sessionFactory.getCurrentSession().save(usuario);
  }

  private Monedero dadoQueTengoUnMonedero(Usuario usuario) {
    Monedero monedero = new Monedero();
    monedero.setUsuario(usuario);
    monedero.setSaldo(0);
    return monedero;
  }

  private void cuandoGuardoElMonedero(Monedero monedero) {
    repositorioMonedero.guardar(monedero);
  }

  private Transaccion dadoQueTengoUnaTransaccion(Monedero monedero, Integer monto, String tipo) {
    Transaccion transaccion = new Transaccion();
    transaccion.setMonedero(monedero);
    transaccion.setMonto(monto);
    transaccion.setTipo(tipo);
    transaccion.setDescripcion("Transaccion de " + tipo);
    return transaccion;
  }

  private void entoncesElMonederoObtenidoEsCorrecto(Monedero obtenido, Monedero esperado) {
    assertThat(obtenido, is(notNullValue()));
    assertThat(obtenido.getSaldo(), is(equalTo(esperado.getSaldo())));
    assertThat(obtenido.getUsuario().getEmail(), is(equalTo(esperado.getUsuario().getEmail())));
  }
}
