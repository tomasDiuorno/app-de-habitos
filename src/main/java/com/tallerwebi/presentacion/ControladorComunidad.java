package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Publicacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.ServicioComunidad;
import com.tallerwebi.presentacion.DTO.ComentarioDTO;
import com.tallerwebi.presentacion.DTO.PublicacionDTO;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
<<<<<<< HEAD
import org.springframework.ui.ModelMap;
=======
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorComunidad {

<<<<<<< HEAD
  private static final String ATRIBUTO_USUARIO = "usuario";
  private static final String REDIRECT_LOGIN = "redirect:/login";
  private static final String REDIRECT_COMUNIDAD = "redirect:/comunidad";

  private ServicioComunidad servicioComunidad;
=======
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
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a

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

<<<<<<< HEAD
    ModelMap modelo = new ModelMap();
    modelo.put(ATRIBUTO_USUARIO, usuario);
    modelo.put("publicaciones", servicioComunidad.obtenerPublicaciones());

    return new ModelAndView("comunidad", modelo);
=======
    ModelAndView modelAndView = new ModelAndView(VISTA_COMUNIDAD);
    modelAndView.addObject(ATRIBUTO_USUARIO, usuario);
    modelAndView.addObject(ATRIBUTO_PUBLICACIONES, this.servicioComunidad.obtenerPublicaciones());

    return modelAndView;
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
  }

  @RequestMapping(path = "/comunidad/nueva", method = RequestMethod.GET)
  public ModelAndView irACrearPublicacion(HttpServletRequest request) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

<<<<<<< HEAD
    ModelMap modelo = new ModelMap();
    modelo.put("publicacionDTO", new PublicacionDTO());

    return new ModelAndView("crear-publicacion", modelo);
=======
    ModelAndView modelAndView = new ModelAndView(VISTA_CREAR_PUBLICACION);
    modelAndView.addObject(ATRIBUTO_PUBLICACION_DTO, new PublicacionDTO());

    return modelAndView;
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
  }

  @RequestMapping(path = "/comunidad/nueva", method = RequestMethod.POST)
  public ModelAndView crearPublicacion(
<<<<<<< HEAD
    @ModelAttribute("publicacionDTO") PublicacionDTO publicacionDTO,
=======
    @ModelAttribute(ATRIBUTO_PUBLICACION_DTO) PublicacionDTO publicacionDTO,
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
    HttpServletRequest request
  ) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

<<<<<<< HEAD
    if (estaVacio(publicacionDTO.getTitulo())) {
      return volverACrearPublicacionConError(publicacionDTO, "El título no puede estar vacío");
    }

    if (estaVacio(publicacionDTO.getContenido())) {
      return volverACrearPublicacionConError(publicacionDTO, "El contenido no puede estar vacío");
    }

    servicioComunidad.crearPublicacion(publicacionDTO, usuario);
=======
    if (publicacionDTO.getTitulo() == null || publicacionDTO.getTitulo().trim().isEmpty()) {
      return volverACrearPublicacionConError(publicacionDTO, "El título no puede estar vacío");
    }

    if (publicacionDTO.getContenido() == null || publicacionDTO.getContenido().trim().isEmpty()) {
      return volverACrearPublicacionConError(publicacionDTO, "El contenido no puede estar vacío");
    }

    this.servicioComunidad.crearPublicacion(publicacionDTO, usuario);
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a

    return new ModelAndView(REDIRECT_COMUNIDAD);
  }

  @RequestMapping(path = "/comunidad/{id}", method = RequestMethod.GET)
  public ModelAndView verPublicacion(@PathVariable Integer id, HttpServletRequest request) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

<<<<<<< HEAD
    Publicacion publicacion = servicioComunidad.buscarPublicacionPorId(id);
=======
    Publicacion publicacion = this.servicioComunidad.buscarPublicacionPorId(id);
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a

    if (publicacion == null) {
      return new ModelAndView(REDIRECT_COMUNIDAD);
    }

<<<<<<< HEAD
    ModelMap modelo = new ModelMap();
    modelo.put("publicacion", publicacion);
    modelo.put("comentarioDTO", new ComentarioDTO());

    return new ModelAndView("detalle-publicacion", modelo);
=======
    ModelAndView modelAndView = new ModelAndView(VISTA_DETALLE_PUBLICACION);
    modelAndView.addObject(ATRIBUTO_PUBLICACION, publicacion);
    modelAndView.addObject(ATRIBUTO_COMENTARIO_DTO, new ComentarioDTO());

    return modelAndView;
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
  }

  @RequestMapping(path = "/comunidad/{id}/comentar", method = RequestMethod.POST)
  public ModelAndView comentarPublicacion(
    @PathVariable Integer id,
<<<<<<< HEAD
    @ModelAttribute("comentarioDTO") ComentarioDTO comentarioDTO,
=======
    @ModelAttribute(ATRIBUTO_COMENTARIO_DTO) ComentarioDTO comentarioDTO,
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
    HttpServletRequest request
  ) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

<<<<<<< HEAD
    if (!estaVacio(comentarioDTO.getContenido())) {
      servicioComunidad.comentarPublicacion(id, comentarioDTO, usuario);
    }

=======
    if (comentarioDTO.getContenido() == null || comentarioDTO.getContenido().trim().isEmpty()) {
      return new ModelAndView("redirect:/comunidad/" + id);
    }

    this.servicioComunidad.comentarPublicacion(id, comentarioDTO, usuario);

>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
    return new ModelAndView("redirect:/comunidad/" + id);
  }

  private Usuario obtenerUsuarioDeSesion(HttpServletRequest request) {
    return (Usuario) request.getSession().getAttribute(ATRIBUTO_USUARIO);
  }

<<<<<<< HEAD
  private boolean estaVacio(String texto) {
    return texto == null || texto.trim().isEmpty();
  }

=======
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
  private ModelAndView volverACrearPublicacionConError(
    PublicacionDTO publicacionDTO,
    String mensaje
  ) {
<<<<<<< HEAD
    ModelMap modelo = new ModelMap();
    modelo.put("publicacionDTO", publicacionDTO);
    modelo.put("error", mensaje);

    return new ModelAndView("crear-publicacion", modelo);
=======
    ModelAndView modelAndView = new ModelAndView(VISTA_CREAR_PUBLICACION);
    modelAndView.addObject(ATRIBUTO_PUBLICACION_DTO, publicacionDTO);
    modelAndView.addObject(ATRIBUTO_ERROR, mensaje);

    return modelAndView;
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
  }
}
