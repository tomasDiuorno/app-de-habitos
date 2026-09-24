package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Publicacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.dominio.excepcion.UsuarioYaUnidoAHabitoException;
import com.tallerwebi.dominio.interfaz.ServicioComunidad;
import com.tallerwebi.dominio.interfaz.ServicioLogro;
import com.tallerwebi.dominio.servicios.ServicioHabitoCompartido;
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
  private static final String VISTA_DETALLE_PUBLICACION = "detalle-publicacion";
  private static final String ATRIBUTO_COMENTARIO_DTO = "comentarioDTO";

  private ServicioComunidad servicioComunidad;
  private ServicioHabitoCompartido servicioHabitoCompartido;
  private ServicioLogro servicioLogro;

  @Autowired
  public ControladorComunidad(
    ServicioComunidad servicioComunidad,
    ServicioHabitoCompartido servicioHabitoCompartido,
    ServicioLogro servicioLogro
  ) {
    this.servicioComunidad = servicioComunidad;
    this.servicioHabitoCompartido = servicioHabitoCompartido;
    this.servicioLogro = servicioLogro;
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
    modelo.put(ATRIBUTO_COMENTARIO_DTO, new ComentarioDTO());

    return new ModelAndView(VISTA_DETALLE_PUBLICACION, modelo);
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

    if (!estaVacio(comentarioDTO.getContenido())) {
      servicioComunidad.comentarPublicacion(id, comentarioDTO, usuario);
    }

    return new ModelAndView("redirect:/comunidad/" + id);
  }

  @RequestMapping(path = "/comunidad/{id}/unirse-habito", method = RequestMethod.POST)
  public ModelAndView unirseAHabitoGrupal(@PathVariable Integer id, HttpServletRequest request) {
    Usuario usuario = obtenerUsuarioDeSesion(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    Publicacion publicacion = servicioComunidad.buscarPublicacionPorId(id);

    if (publicacion == null || publicacion.getHabitoAsociado() == null) {
      return new ModelAndView(REDIRECT_COMUNIDAD);
    }

    return this.unirseYObtenerResultado(publicacion, usuario, id);
  }

  // El cálculo de cantidadHabitosAntes ocurre antes del try porque necesitamos el valor previo
  // a la operación; los catch retornan antes de usarlo, lo cual PMD marca como falso positivo.
  @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
  private ModelAndView unirseYObtenerResultado(
    Publicacion publicacion,
    Usuario usuario,
    Integer id
  ) {
    int cantidadHabitosAntes = usuario.getUsuarioHabitos().size();

    try {
      servicioHabitoCompartido.unirseAHabitoGrupal(publicacion.getHabitoAsociado(), usuario);
    } catch (LimiteHabitosAlcanzadoException e) {
      return volverADetalleConError(publicacion, "Alcanzaste el límite de hábitos permitidos");
    } catch (UsuarioYaUnidoAHabitoException e) {
      return volverADetalleConError(publicacion, "Ya estás participando en este hábito grupal");
    }

    int cantidadHabitosDespues = cantidadHabitosAntes + 1;
    this.servicioLogro.verificarYAsignarLogros(usuario, cantidadHabitosDespues);

    ModelAndView modelAndView = this.volverADetalle(publicacion);
    CargadorLogroDesbloqueado.cargarLogroDesbloqueado(
      modelAndView,
      cantidadHabitosAntes,
      cantidadHabitosDespues
    );

    if (modelAndView.getModel().containsKey(CargadorLogroDesbloqueado.ATRIBUTO_MOSTRAR_LOGRO)) {
      return modelAndView;
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

  private ModelAndView volverADetalleConError(Publicacion publicacion, String mensaje) {
    ModelMap modelo = new ModelMap();
    modelo.put("publicacion", publicacion);
    modelo.put(ATRIBUTO_COMENTARIO_DTO, new ComentarioDTO());
    modelo.put("error", mensaje);

    return new ModelAndView(VISTA_DETALLE_PUBLICACION, modelo);
  }

  private ModelAndView volverADetalle(Publicacion publicacion) {
    ModelMap modelo = new ModelMap();
    modelo.put("publicacion", publicacion);
    modelo.put(ATRIBUTO_COMENTARIO_DTO, new ComentarioDTO());

    return new ModelAndView(VISTA_DETALLE_PUBLICACION, modelo);
  }
}
