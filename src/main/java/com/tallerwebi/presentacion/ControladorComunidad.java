package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Publicacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.ServicioComunidad;
import com.tallerwebi.presentacion.DTO.ComentarioDTO;
import com.tallerwebi.presentacion.DTO.PublicacionDTO;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorComunidad {

  private static final String ATRIBUTO_USUARIO = "usuario";
  private static final String REDIRECT_LOGIN = "redirect:/login";
  private static final String REDIRECT_COMUNIDAD = "redirect:/comunidad";

  private ServicioComunidad servicioComunidad;

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

    ModelMap modelo = new ModelMap();
    modelo.put(ATRIBUTO_USUARIO, usuario);
    modelo.put("publicaciones", servicioComunidad.obtenerPublicaciones());

    return new ModelAndView("comunidad", modelo);
  }

  @RequestMapping(path = "/comunidad/nueva", method = RequestMethod.GET)
  public ModelAndView irACrearPublicacion(HttpServletRequest request) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    ModelMap modelo = new ModelMap();
    modelo.put("publicacionDTO", new PublicacionDTO());

    return new ModelAndView("crear-publicacion", modelo);
  }

  @RequestMapping(path = "/comunidad/nueva", method = RequestMethod.POST)
  public ModelAndView crearPublicacion(
    @ModelAttribute("publicacionDTO") PublicacionDTO publicacionDTO,
    HttpServletRequest request
  ) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    if (estaVacio(publicacionDTO.getTitulo())) {
      return volverACrearPublicacionConError(publicacionDTO, "El título no puede estar vacío");
    }

    if (estaVacio(publicacionDTO.getContenido())) {
      return volverACrearPublicacionConError(publicacionDTO, "El contenido no puede estar vacío");
    }

    servicioComunidad.crearPublicacion(publicacionDTO, usuario);

    return new ModelAndView(REDIRECT_COMUNIDAD);
  }

  @RequestMapping(path = "/comunidad/{id}", method = RequestMethod.GET)
  public ModelAndView verPublicacion(@PathVariable Integer id, HttpServletRequest request) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    Publicacion publicacion = servicioComunidad.buscarPublicacionPorId(id);

    if (publicacion == null) {
      return new ModelAndView(REDIRECT_COMUNIDAD);
    }

    ModelMap modelo = new ModelMap();
    modelo.put("publicacion", publicacion);
    modelo.put("comentarioDTO", new ComentarioDTO());

    return new ModelAndView("detalle-publicacion", modelo);
  }

  @RequestMapping(path = "/comunidad/{id}/comentar", method = RequestMethod.POST)
  public ModelAndView comentarPublicacion(
    @PathVariable Integer id,
    @ModelAttribute("comentarioDTO") ComentarioDTO comentarioDTO,
    HttpServletRequest request
  ) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    if (!estaVacio(comentarioDTO.getContenido())) {
      servicioComunidad.comentarPublicacion(id, comentarioDTO, usuario);
    }

    return new ModelAndView("redirect:/comunidad/" + id);
  }

  private Usuario obtenerUsuarioDeSesion(HttpServletRequest request) {
    return (Usuario) request.getSession().getAttribute(ATRIBUTO_USUARIO);
  }

  private boolean estaVacio(String texto) {
    return texto == null || texto.trim().isEmpty();
  }

  private ModelAndView volverACrearPublicacionConError(
    PublicacionDTO publicacionDTO,
    String mensaje
  ) {
    ModelMap modelo = new ModelMap();
    modelo.put("publicacionDTO", publicacionDTO);
    modelo.put("error", mensaje);

    return new ModelAndView("crear-publicacion", modelo);
  }
}
