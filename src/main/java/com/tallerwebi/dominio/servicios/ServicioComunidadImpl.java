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

  private RepositorioPublicacion repositorioPublicacion;
  private RepositorioComentario repositorioComentario;

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

    repositorioPublicacion.guardar(publicacion);
  }

  @Override
  public List<Publicacion> obtenerPublicaciones() {
    return repositorioPublicacion.obtenerTodas();
  }

  @Override
  public Publicacion buscarPublicacionPorId(Integer id) {
    Publicacion publicacion = repositorioPublicacion.buscarPorId(id);

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
    Publicacion publicacion = repositorioPublicacion.buscarPorId(publicacionId);

    if (publicacion == null) {
      return;
    }

    Comentario comentario = new Comentario();
    comentario.setContenido(comentarioDTO.getContenido());
    comentario.setAutor(autor);
    comentario.setFechaCreacion(LocalDateTime.now());
    comentario.setPublicacion(publicacion);

    repositorioComentario.guardar(comentario);
  }
}
