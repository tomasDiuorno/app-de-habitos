package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioHabito;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.dominio.interfaz.ServicioCategoria;
import com.tallerwebi.dominio.interfaz.ServicioEvaluadorHabito;
import com.tallerwebi.dominio.interfaz.ServicioHabito;
import com.tallerwebi.dominio.interfaz.ServicioLogro;
import com.tallerwebi.dominio.interfaz.ServicioUsuarioHabito;
import com.tallerwebi.dominio.servicios.ServicioHabitoIA;
import com.tallerwebi.presentacion.DTO.PlanHabitoDTO;
import com.tallerwebi.presentacion.DTO.RegistroHabitoDTO;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorHabitos {

  private static final String VISTA_CREAR_HABITO = "crear-habito";
  private static final String VISTA_HABITOS = "habitos";
  private static final String REDIRECT_LOGIN = "redirect:/login";
  private static final String REDIRECT_HABITOS = "redirect:/habitos";

  private static final String ATRIBUTO_CATEGORIAS = "categorias";
  private static final String ATRIBUTO_DATOS_REGISTRO_HABITO = "datosRegistroHabito";
  private static final String ATRIBUTO_USUARIO_HABITOS = "usuarioHabitos";
  private static final String ATRIBUTO_MOSTRAR_LOGRO = "mostrarLogro";
  private static final String ATRIBUTO_TITULO_LOGRO = "tituloLogro";
  private static final String ATRIBUTO_DESCRIPCION_LOGRO = "descripcionLogro";
  private static final String ATRIBUTO_ERROR = "error";
  private static final String ATRIBUTO_USUARIO = "usuario";

  private static final int CERO_HABITOS = 0;
  private static final int UN_HABITO = 1;
  private static final int DOS_HABITOS = 2;
  private static final int TRES_HABITOS = 3;
  private static final int CUATRO_HABITOS = 4;

  private ServicioHabito servicioHabito;
  private ServicioCategoria servicioCategoria;
  private ServicioLogro servicioLogro;
  private ServicioEvaluadorHabito servicioEvaluadorHabito;
  private ServicioUsuarioHabito servicioUsuarioHabito;
  private ServicioHabitoIA servicioHabitoIA;

  @Autowired
  public ControladorHabitos(
    ServicioHabito servicioHabito,
    ServicioCategoria servicioCategoria,
    ServicioLogro servicioLogro,
    ServicioEvaluadorHabito servicioEvaluadorHabito,
    ServicioUsuarioHabito servicioUsuarioHabito,
    ServicioHabitoIA servicioHabitoIA
  ) {
    this.servicioHabito = servicioHabito;
    this.servicioCategoria = servicioCategoria;
    this.servicioLogro = servicioLogro;
    this.servicioEvaluadorHabito = servicioEvaluadorHabito;
    this.servicioUsuarioHabito = servicioUsuarioHabito;
    this.servicioHabitoIA = servicioHabitoIA;
  }

  @RequestMapping(path = "/habitos", method = RequestMethod.GET)
  public ModelAndView irAHabitos(HttpServletRequest request) {
    Usuario usuario = this.obtenerUsuario(request);

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    ModelAndView modelAndView = new ModelAndView(VISTA_HABITOS);
    modelAndView.addObject(ATRIBUTO_USUARIO_HABITOS, usuario.getUsuarioHabitos());

    return modelAndView;
  }

  @RequestMapping(path = "/completar-habito", method = RequestMethod.POST)
  public ModelAndView completarHabito(
    @RequestParam Integer habitoId,
    @RequestParam String evidencia,
    HttpServletRequest request
  ) {
    Usuario usuario = this.obtenerUsuario(request);
    Habito habito = servicioHabito.buscarHabitoPorId(habitoId);
    UsuarioHabito usuarioHabito =
      this.servicioUsuarioHabito.obtenerPorUsuarioYHabito(usuario, habito);
    servicioEvaluadorHabito.completarHabito(usuarioHabito, evidencia);

    return new ModelAndView(REDIRECT_HABITOS);
  }

  @RequestMapping(path = "/habito/plan", method = RequestMethod.GET)
  public ModelAndView irAPlanHabito(@RequestParam Integer habitoId, HttpServletRequest request) {
    Usuario usuario = this.obtenerUsuario(request);
    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    Habito habito = servicioHabito.buscarHabitoPorId(habitoId);
    if (habito == null) {
      return new ModelAndView(REDIRECT_HABITOS);
    }

    ModelAndView modelAndView = new ModelAndView("plan-habito");
    modelAndView.addObject("habito", habito);

    try {
      PlanHabitoDTO plan = servicioHabitoIA.sugerirPlan(habito.getTitulo());
      modelAndView.addObject("plan", plan);
    } catch (Exception e) {
      modelAndView.addObject(
        "error",
        "Hubo un problema al generar los pasos. Intentá de nuevo más tarde."
      );
    }

    return modelAndView;
  }

  @RequestMapping(path = "/crear-habito", method = RequestMethod.GET)
  public ModelAndView irACrearHabito() {
    return crearVistaCrearHabito(new RegistroHabitoDTO());
  }

  @RequestMapping(path = "/crear-habito", method = RequestMethod.POST)
  public ModelAndView crearHabito(
    @ModelAttribute(ATRIBUTO_DATOS_REGISTRO_HABITO) RegistroHabitoDTO datos,
    HttpServletRequest request
  ) {
    Usuario usuario = this.obtenerUsuario(request);
    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    Integer cantidadHabitosAntes = usuario.getUsuarioHabitos().size();

    try {
      Habito habito = this.servicioHabito.obtenerHabito(datos);
      this.servicioHabito.agregarHabitoParaUsuario(habito, usuario);

      Integer cantidadHabitosDespues = cantidadHabitosAntes + 1;
      this.servicioLogro.verificarYAsignarLogros(usuario, cantidadHabitosDespues);

      ModelAndView modelAndView = crearVistaCrearHabito(new RegistroHabitoDTO());
      cargarLogroDesbloqueado(modelAndView, cantidadHabitosAntes, cantidadHabitosDespues);

      if (modelAndView.getModel().containsKey(ATRIBUTO_MOSTRAR_LOGRO)) {
        return modelAndView;
      }

      return new ModelAndView(REDIRECT_HABITOS);
    } catch (HabitoExistenteExeption excepcion) {
      ModelAndView modelAndView = crearVistaCrearHabito(datos);
      modelAndView.addObject(ATRIBUTO_ERROR, "Ya existe un hábito con ese nombre");
      return modelAndView;
    } catch (LimiteHabitosAlcanzadoException excepcion) {
      ModelAndView modelAndView = crearVistaCrearHabito(datos);
      modelAndView.addObject(ATRIBUTO_ERROR, "No podés tener más de 4 hábitos activos");
      return modelAndView;
    }
  }

  private Usuario obtenerUsuario(HttpServletRequest request) {
    return (Usuario) request.getSession().getAttribute(ATRIBUTO_USUARIO);
  }

  private ModelAndView crearVistaCrearHabito(RegistroHabitoDTO datosRegistroHabito) {
    ModelAndView modelAndView = new ModelAndView(VISTA_CREAR_HABITO);

    modelAndView.addObject(ATRIBUTO_CATEGORIAS, this.servicioCategoria.obtenerCategorias());
    modelAndView.addObject(ATRIBUTO_DATOS_REGISTRO_HABITO, datosRegistroHabito);

    return modelAndView;
  }

  private void cargarLogroDesbloqueado(
    ModelAndView modelAndView,
    int cantidadHabitosAntes,
    int cantidadHabitosDespues
  ) {
    if (cantidadHabitosAntes == CERO_HABITOS && cantidadHabitosDespues == UN_HABITO) {
      cargarDatosDelLogro(
        modelAndView,
        "Primer hábito creado",
        "Creaste tu primer hábito. Tu rutina acaba de empezar."
      );
    }

    if (cantidadHabitosAntes == DOS_HABITOS && cantidadHabitosDespues == TRES_HABITOS) {
      cargarDatosDelLogro(
        modelAndView,
        "Constante",
        "Ya tenés 3 hábitos activos. Estás construyendo una rutina."
      );
    }

    if (cantidadHabitosAntes == TRES_HABITOS && cantidadHabitosDespues == CUATRO_HABITOS) {
      cargarDatosDelLogro(modelAndView, "Experto", "Llegaste al máximo de 4 hábitos activos.");
    }
  }

  private void cargarDatosDelLogro(
    ModelAndView modelAndView,
    String tituloLogro,
    String descripcionLogro
  ) {
    modelAndView.addObject(ATRIBUTO_MOSTRAR_LOGRO, true);
    modelAndView.addObject(ATRIBUTO_TITULO_LOGRO, tituloLogro);
    modelAndView.addObject(ATRIBUTO_DESCRIPCION_LOGRO, descripcionLogro);
  }
}
