package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.entidades.Publicacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.RepositorioComentario;
import com.tallerwebi.dominio.interfaz.RepositorioPublicacion;
import com.tallerwebi.dominio.interfaz.ServicioComunidad;
import com.tallerwebi.presentacion.DTO.ComentarioDTO;
import com.tallerwebi.presentacion.DTO.PublicacionDTO;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioComunidad")
@Transactional
public class ServicioComunidadImpl implements ServicioComunidad {

<<<<<<< HEAD
  private RepositorioPublicacion repositorioPublicacion;
  private RepositorioComentario repositorioComentario;
=======
  private final RepositorioPublicacion repositorioPublicacion;
  private final RepositorioComentario repositorioComentario;
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a

  @Autowired
  public ServicioComunidadImpl(
    RepositorioPublicacion repositorioPublicacion,
    RepositorioComentario repositorioComentario
  ) {
    this.repositorioPublicacion = repositorioPublicacion;
    this.repositorioComentario = repositorioComentario;
  }

  @Override
  public void crearPublicacion(PublicacionDTO publicacionDTO, Usuario autor) {
    Publicacion publicacion = new Publicacion();
    publicacion.setTitulo(publicacionDTO.getTitulo());
    publicacion.setContenido(publicacionDTO.getContenido());
    publicacion.setAutor(autor);
    publicacion.setFechaCreacion(LocalDateTime.now());

<<<<<<< HEAD
    repositorioPublicacion.guardar(publicacion);
=======
    this.repositorioPublicacion.guardar(publicacion);
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
  }

  @Override
  public List<Publicacion> obtenerPublicaciones() {
<<<<<<< HEAD
    return repositorioPublicacion.obtenerTodas();
=======
    return this.repositorioPublicacion.obtenerTodas();
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
  }

  @Override
  public Publicacion buscarPublicacionPorId(Integer id) {
<<<<<<< HEAD
    Publicacion publicacion = repositorioPublicacion.buscarPorId(id);
=======
    Publicacion publicacion = this.repositorioPublicacion.buscarPorId(id);
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a

    if (publicacion != null) {
      publicacion.getComentarios().size();
    }

    return publicacion;
  }

  @Override
  public void comentarPublicacion(
    Integer publicacionId,
    ComentarioDTO comentarioDTO,
    Usuario autor
  ) {
<<<<<<< HEAD
    Publicacion publicacion = repositorioPublicacion.buscarPorId(publicacionId);

    if (publicacion == null) {
      return;
    }
=======
    Publicacion publicacion = this.repositorioPublicacion.buscarPorId(publicacionId);
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a

    Comentario comentario = new Comentario();
    comentario.setContenido(comentarioDTO.getContenido());
    comentario.setAutor(autor);
    comentario.setFechaCreacion(LocalDateTime.now());
    comentario.setPublicacion(publicacion);

<<<<<<< HEAD
    repositorioComentario.guardar(comentario);
=======
    this.repositorioComentario.guardar(comentario);
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
  }
}
