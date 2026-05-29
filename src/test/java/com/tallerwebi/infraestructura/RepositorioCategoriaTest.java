package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.RepositorioCategoria;
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
public class RepositorioCategoriaTest {

  @Autowired
  private SessionFactory sessionFactory;

  private RepositorioCategoria repositorioCategoria;

  @BeforeEach
  public void init() {
    repositorioCategoria = new RepositorioCategoriaImp(sessionFactory);
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaGuardarUnaNuevaCategoria() {
    Categoria cat = new Categoria();
    String nombre = "Bienestar";
    cat.setId(1);
    cat.setNombre(nombre);

    this.dadoQueExisteUnCategoria(cat);
    Categoria categoria = dadoQueTengoUnaCategoria(1);

    Categoria obtenida = obtengoUnaCategoria(nombre);
    this.entoncesLaCategoriaObtenidaEsCorrecta(obtenida, categoria);
  }

  
  @Test
  @Transactional
  @Rollback
  public void deberiaObtenerUnaCategoriaYAsignarseaAUnHabito() {
    Categoria cat = new Categoria();
    String nombre = "Bienestar";
    cat.setId(1);
    cat.setNombre(nombre);
    this.dadoQueExisteUnCategoria(cat);

    Categoria categoria = dadoQueTengoUnaCategoria(1);
    
    Categoria obtenida = obtengoUnaCategoria(nombre);
    this.entoncesLaCategoriaObtenidaEsCorrecta(obtenida, categoria);
  }
  
  private void dadoQueExisteUnCategoria(Categoria cat) {
    this.sessionFactory.getCurrentSession().save(cat);
  }

  private void entoncesLaCategoriaObtenidaEsCorrecta(Categoria obtenida, Categoria categoria) {
    assertThat(obtenida.getNombre(), is(equalTo(categoria.getNombre())));
  }
  
  private Categoria obtengoUnaCategoria(String nombre) {
    return (Categoria) this.sessionFactory.getCurrentSession()
    .createQuery("FROM Categoria WHERE nombre = :nombre")
    .setParameter("nombre", nombre)
    .getSingleResult();
  }
  
  private Categoria dadoQueTengoUnaCategoria(Integer id) {
    Categoria cat = repositorioCategoria.obtenerCategoriaPorId(id);
    return cat;
  }
  
}
