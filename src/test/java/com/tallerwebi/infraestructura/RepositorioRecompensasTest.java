package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.tallerwebi.dominio.Rareza;
import com.tallerwebi.dominio.Recompensa;
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
public class RepositorioRecompensasTest {

  @Autowired
  private SessionFactory sessionFactory;

  private RepositorioRecompensasImp repositorioRecompensas;

  @BeforeEach
  public void init() {
    repositorioRecompensas = new RepositorioRecompensasImp(sessionFactory);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaDevolverLasRecompensasCargadasEnLaBaseDeDatos() {
    Recompensa recompensa1 =
      this.dadoQueTengoUnaRecompensa(
          "recompensa",
          "llegaste al nivel 5",
          "imagen",
          5,
          Rareza.COMUN
        );
    this.dadoQueExisteLaRecompensa(recompensa1);

    List<Recompensa> recompensas = repositorioRecompensas.obtenerTodas();
    assertThat(recompensas.size(), is(equalTo(1)));
    assertThat(recompensas.get(0).getNombre(), is(equalTo("recompensa")));
    assertThat(recompensas.get(0).getDescripcion(), is(equalTo("llegaste al nivel 5")));
    assertThat(recompensas.get(0).getUrlImg(), is(equalTo("imagen")));
    assertThat(recompensas.get(0).getNivelRequerido(), is(equalTo(5)));
    assertThat(recompensas.get(0).getRareza(), is(equalTo(Rareza.COMUN)));
  }

  private void dadoQueExisteLaRecompensa(Recompensa recompensa) {
    this.sessionFactory.getCurrentSession().save(recompensa);
  }

  private Recompensa dadoQueTengoUnaRecompensa(
    String nombre,
    String descripcion,
    String imagen,
    Integer nivelRequerido,
    Rareza rareza
  ) {
    Recompensa recomp = new Recompensa();
    recomp.setNombre(nombre);
    recomp.setDescripcion(descripcion);
    recomp.setUrlImg(imagen);
    recomp.setNivelRequerido(nivelRequerido);
    recomp.setRareza(rareza);
    return recomp;
  }
}
