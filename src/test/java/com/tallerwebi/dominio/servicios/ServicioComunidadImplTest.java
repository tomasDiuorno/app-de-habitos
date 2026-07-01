package com.tallerwebi.dominio.servicios;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.entidades.Publicacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.RepositorioComentario;
import com.tallerwebi.dominio.interfaz.RepositorioPublicacion;
import com.tallerwebi.presentacion.DTO.ComentarioDTO;
import com.tallerwebi.presentacion.DTO.PublicacionDTO;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class ServicioComunidadImplTest {

  private RepositorioPublicacion repositorioPublicacion;
  private RepositorioComentario repositorioComentario;
  private ServicioComunidadImpl servicioComunidad;

  @BeforeEach
  public void init() {
    repositorioPublicacion = mock(RepositorioPublicacion.class);
    repositorioComentario = mock(RepositorioComentario.class);
    servicioComunidad = new ServicioComunidadImpl(repositorioPublicacion, repositorioComentario);
  }

  @Test
  public void crearPublicacionDeberiaGuardarUnaPublicacionConAutorTituloYContenido() {
    Usuario autor = new Usuario();

    PublicacionDTO publicacionDTO = new PublicacionDTO();
    publicacionDTO.setTitulo("Mi primer post");
    publicacionDTO.setContenido("Contenido del post");

    servicioComunidad.crearPublicacion(publicacionDTO, autor);

    ArgumentCaptor<Publicacion> captor = ArgumentCaptor.forClass(Publicacion.class);
    verify(repositorioPublicacion).guardar(captor.capture());

    Publicacion publicacionGuardada = captor.getValue();

    assertThat(publicacionGuardada.getTitulo(), equalTo("Mi primer post"));
    assertThat(publicacionGuardada.getContenido(), equalTo("Contenido del post"));
    assertThat(publicacionGuardada.getAutor(), equalTo(autor));
    assertThat(publicacionGuardada.getFechaCreacion(), notNullValue());
  }

  @Test
  public void obtenerPublicacionesDeberiaRetornarLasPublicacionesDelRepositorio() {
    Publicacion primera = new Publicacion();
    Publicacion segunda = new Publicacion();

    when(repositorioPublicacion.obtenerTodas()).thenReturn(Arrays.asList(primera, segunda));

    List<Publicacion> publicaciones = servicioComunidad.obtenerPublicaciones();

    assertThat(publicaciones.size(), equalTo(2));
    verify(repositorioPublicacion).obtenerTodas();
  }

  @Test
  public void buscarPublicacionPorIdDeberiaRetornarLaPublicacionEncontrada() {
    Publicacion publicacion = new Publicacion();
    when(repositorioPublicacion.buscarPorId(1)).thenReturn(publicacion);

    Publicacion resultado = servicioComunidad.buscarPublicacionPorId(1);

    assertThat(resultado, equalTo(publicacion));
    verify(repositorioPublicacion).buscarPorId(1);
  }

  @Test
  public void comentarPublicacionDeberiaGuardarComentarioConAutorYPublicacion() {
    Usuario autor = new Usuario();

    Publicacion publicacion = new Publicacion();
    when(repositorioPublicacion.buscarPorId(1)).thenReturn(publicacion);

    ComentarioDTO comentarioDTO = new ComentarioDTO();
    comentarioDTO.setContenido("Buen post");

    servicioComunidad.comentarPublicacion(1, comentarioDTO, autor);

    ArgumentCaptor<Comentario> captor = ArgumentCaptor.forClass(Comentario.class);
    verify(repositorioComentario).guardar(captor.capture());

    Comentario comentarioGuardado = captor.getValue();

    assertThat(comentarioGuardado.getContenido(), equalTo("Buen post"));
    assertThat(comentarioGuardado.getAutor(), equalTo(autor));
    assertThat(comentarioGuardado.getPublicacion(), equalTo(publicacion));
    assertThat(comentarioGuardado.getFechaCreacion(), notNullValue());
  }
}
