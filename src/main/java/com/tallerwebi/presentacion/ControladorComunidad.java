package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Publicacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.ServicioComunidad;
import com.tallerwebi.presentacion.DTO.ComentarioDTO;
import com.tallerwebi.presentacion.DTO.PublicacionDTO;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorComunidad {

  private static final String REDIRECT_LOGIN = "redirect:/login";
  private static final String REDIRECT_COMUNIDAD = "redirect:/comunidad";
  private static final String VISTA_COMUNIDAD = "comunidad";
  private static final String VISTA_CREAR_PUBLICACION = "crear-publicacion";
  private static final String VISTA_DETALLE_PUBLICACION = "detalle-publicacion";
  private static final String ATRIBUTO_USUARIO = "usuario";
  private static final String ATRIBUTO_PUBLICACIONES = "publicaciones";
  private static final String ATRIBUTO_PUBLICACION = "publicacion";
  private static final String ATRIBUTO_PUBLICACION_DTO = "publicacionDTO";
  private static final String ATRIBUTO_COMENTARIO_DTO = "comentarioDTO";
  private static final String ATRIBUTO_ERROR = "error";

  private final ServicioComunidad servicioComunidad;

  @Autowired
  public ControladorComunidad(ServicioComunidad servicioComunidad) {
    this.servicioComunidad = servicioComunidad;
  }

  @RequestMapping(path = "/comunidad", method = RequestMethod.GET)
  public ModelAndView irAComunidad(HttpServletRequest request) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    ModelAndView modelAndView = new ModelAndView(VISTA_COMUNIDAD);
    modelAndView.addObject(ATRIBUTO_USUARIO, usuario);
    modelAndView.addObject(ATRIBUTO_PUBLICACIONES, this.servicioComunidad.obtenerPublicaciones());

    return modelAndView;
  }

  @RequestMapping(path = "/comunidad/nueva", method = RequestMethod.GET)
  public ModelAndView irACrearPublicacion(HttpServletRequest request) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    ModelAndView modelAndView = new ModelAndView(VISTA_CREAR_PUBLICACION);
    modelAndView.addObject(ATRIBUTO_PUBLICACION_DTO, new PublicacionDTO());

    return modelAndView;
  }

  @RequestMapping(path = "/comunidad/nueva", method = RequestMethod.POST)
  public ModelAndView crearPublicacion(
    @ModelAttribute(ATRIBUTO_PUBLICACION_DTO) PublicacionDTO publicacionDTO,
    HttpServletRequest request
  ) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    if (publicacionDTO.getTitulo() == null || publicacionDTO.getTitulo().trim().isEmpty()) {
      return volverACrearPublicacionConError(publicacionDTO, "El título no puede estar vacío");
    }

    if (publicacionDTO.getContenido() == null || publicacionDTO.getContenido().trim().isEmpty()) {
      return volverACrearPublicacionConError(publicacionDTO, "El contenido no puede estar vacío");
    }

    this.servicioComunidad.crearPublicacion(publicacionDTO, usuario);

    return new ModelAndView(REDIRECT_COMUNIDAD);
  }

  @RequestMapping(path = "/comunidad/{id}", method = RequestMethod.GET)
  public ModelAndView verPublicacion(@PathVariable Integer id, HttpServletRequest request) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    Publicacion publicacion = this.servicioComunidad.buscarPublicacionPorId(id);

    if (publicacion == null) {
      return new ModelAndView(REDIRECT_COMUNIDAD);
    }

    ModelAndView modelAndView = new ModelAndView(VISTA_DETALLE_PUBLICACION);
    modelAndView.addObject(ATRIBUTO_PUBLICACION, publicacion);
    modelAndView.addObject(ATRIBUTO_COMENTARIO_DTO, new ComentarioDTO());

    return modelAndView;
  }

  @RequestMapping(path = "/comunidad/{id}/comentar", method = RequestMethod.POST)
  public ModelAndView comentarPublicacion(
    @PathVariable Integer id,
    @ModelAttribute(ATRIBUTO_COMENTARIO_DTO) ComentarioDTO comentarioDTO,
    HttpServletRequest request
  ) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    if (comentarioDTO.getContenido() == null || comentarioDTO.getContenido().trim().isEmpty()) {
      return new ModelAndView("redirect:/comunidad/" + id);
    }

    this.servicioComunidad.comentarPublicacion(id, comentarioDTO, usuario);

    return new ModelAndView("redirect:/comunidad/" + id);
  }

  private Usuario obtenerUsuarioDeSesion(HttpServletRequest request) {
    return (Usuario) request.getSession().getAttribute(ATRIBUTO_USUARIO);
  }

  private ModelAndView volverACrearPublicacionConError(
    PublicacionDTO publicacionDTO,
    String mensaje
  ) {
    ModelAndView modelAndView = new ModelAndView(VISTA_CREAR_PUBLICACION);
    modelAndView.addObject(ATRIBUTO_PUBLICACION_DTO, publicacionDTO);
    modelAndView.addObject(ATRIBUTO_ERROR, mensaje);

    return modelAndView;
  }
}
