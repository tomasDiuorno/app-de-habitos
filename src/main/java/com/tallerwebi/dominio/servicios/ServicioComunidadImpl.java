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

  private final RepositorioPublicacion repositorioPublicacion;
  private final RepositorioComentario repositorioComentario;

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

    this.repositorioPublicacion.guardar(publicacion);
  }

  @Override
  public List<Publicacion> obtenerPublicaciones() {
    return this.repositorioPublicacion.obtenerTodas();
  }

  @Override
  public Publicacion buscarPublicacionPorId(Integer id) {
    Publicacion publicacion = this.repositorioPublicacion.buscarPorId(id);

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
    Publicacion publicacion = this.repositorioPublicacion.buscarPorId(publicacionId);

    Comentario comentario = new Comentario();
    comentario.setContenido(comentarioDTO.getContenido());
    comentario.setAutor(autor);
    comentario.setFechaCreacion(LocalDateTime.now());
    comentario.setPublicacion(publicacion);

    this.repositorioComentario.guardar(comentario);
  }
}
