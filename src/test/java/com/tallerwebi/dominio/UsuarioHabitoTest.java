package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import javax.servlet.http.HttpUtils;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;

public class UsuarioHabitoTest {

  @Test
  @Transactional
  @Rollback
  public void unUsuarioHabitoDeberiaRelacionarUnUsuarioConUnHabito() {
    Usuario us = new Usuario();
    us.setEmail("test@mail.com");
    Habito hab = new Habito();
    Categoria cat = new Categoria();
    cat.setNombre("Test");
    hab.setTitulo("Habito");
    hab.setCategoria(cat);

    UsuarioHabito usHab = new UsuarioHabito();
    usHab.setHabito(hab);
    usHab.setUsuario(us);
    usHab.setActivo(true);

    assertThat(usHab.getHabito(), is(hab));
    assertThat(usHab.getUsuario(), is(us));
    assertThat(usHab.getActivo(), is(true));
  }
}
