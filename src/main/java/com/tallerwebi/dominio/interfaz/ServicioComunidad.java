package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Publicacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.presentacion.DTO.ComentarioDTO;
import com.tallerwebi.presentacion.DTO.PublicacionDTO;
import java.util.List;

public interface ServicioComunidad {
  void crearPublicacion(PublicacionDTO publicacionDTO, Usuario autor);

  List<Publicacion> obtenerPublicaciones();

  Publicacion buscarPublicacionPorId(Integer id);

  void comentarPublicacion(Integer publicacionId, ComentarioDTO comentarioDTO, Usuario autor);
}
