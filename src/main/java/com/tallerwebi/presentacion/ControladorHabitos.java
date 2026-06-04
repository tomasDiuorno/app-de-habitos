package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.ItemChecklist;
import com.tallerwebi.dominio.ServicioCategoria;
import com.tallerwebi.dominio.ServicioHabito;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

  private static final int CERO_HABITOS = 0;
  private static final int UN_HABITO = 1;
  private static final int DOS_HABITOS = 2;
  private static final int TRES_HABITOS = 3;
  private static final int CUATRO_HABITOS = 4;

  private ServicioHabito servicioHabito;
  private ServicioCategoria servicioCategoria;
  private static final String PRODUCES_JSON = "application/json";

  @Autowired
  public ControladorHabitos(ServicioHabito servicioHabito, ServicioCategoria servicioCategoria) {
    this.servicioHabito = servicioHabito;
    this.servicioCategoria = servicioCategoria;
  }

  @RequestMapping(path = "/habitos", method = RequestMethod.GET)
  public ModelAndView irAHabitos(HttpServletRequest request) {
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    ModelAndView modelAndView = new ModelAndView(VISTA_HABITOS);
    modelAndView.addObject(ATRIBUTO_USUARIO_HABITOS, usuario.getUsuarioHabito());

    return modelAndView;
  }

  @RequestMapping(path = "/crear-habito", method = RequestMethod.GET)
  public ModelAndView irACrearHabito() {
    return crearVistaCrearHabito(new DatosRegistroHabito());
  }

  @RequestMapping(path = "/crear-habito", method = RequestMethod.POST)
  public ModelAndView crearHabito(
    @ModelAttribute(ATRIBUTO_DATOS_REGISTRO_HABITO) DatosRegistroHabito datos,
    HttpServletRequest request
  ) {
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    int cantidadHabitosAntes = usuario.getUsuarioHabito().size();

    try {
      Habito habito = this.servicioHabito.obtenerHabito(datos);
      this.servicioHabito.agregarHabitoParaUsuario(habito, usuario);

      int cantidadHabitosDespues = usuario.getUsuarioHabito().size();

      ModelAndView modelAndView = crearVistaCrearHabito(new DatosRegistroHabito());
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

  private ModelAndView crearVistaCrearHabito(DatosRegistroHabito datosRegistroHabito) {
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

  @RequestMapping(
    path = "/habito/{idHabito}/agregar-checklist",
    method = RequestMethod.POST,
    produces = PRODUCES_JSON
  )
  @ResponseBody
  public String agregarChecklist(
    @PathVariable Integer idHabito,
    @RequestParam("descripcion") String descripcion
  ) {
    try {
      ItemChecklist nuevoItem = new ItemChecklist();
      nuevoItem.setDescripcion(descripcion);

      servicioHabito.agregarItemChecklistAlHabito(nuevoItem, idHabito);

      return "{\"status\":\"success\", \"mensaje\":\"Checklist agregado correctamente\"}";
    } catch (Exception excepcion) {
      return "{\"status\":\"error\", \"mensaje\":\"No se pudo agregar el checklist\"}";
    }
  }

  @RequestMapping(
    path = "/habito/{idHabito}/eliminar-checklist/{idItem}",
    method = RequestMethod.POST,
    produces = PRODUCES_JSON
  )
  @ResponseBody
  public String eliminarChecklist(@PathVariable Integer idHabito, @PathVariable Integer idItem) {
    try {
      Habito habito = servicioHabito.buscarHabitoPorId(idHabito);

      ItemChecklist itemAEliminar = habito
        .getCantidadDeChecklist()
        .stream()
        .filter(item -> item.getId().equals(idItem))
        .findFirst()
        .orElse(null);

      if (itemAEliminar == null) {
        return "{\"status\":\"error\", \"mensaje\":\"Item no encontrado\"}";
      }

      servicioHabito.eliminarItemChecklistDelHabito(itemAEliminar, idHabito);

      return "{\"status\":\"success\", \"mensaje\":\"Checklist eliminado\"}";
    } catch (Exception excepcion) {
      return "{\"status\":\"error\", \"mensaje\":\"No se pudo eliminar el checklist\"}";
    }
  }

  @RequestMapping(
    path = "/habito/{idHabito}/toggle-checklist/{idItem}",
    method = RequestMethod.POST,
    produces = PRODUCES_JSON
  )
  @ResponseBody
  public String toggleChecklist(@PathVariable Integer idHabito, @PathVariable Integer idItem) {
    try {
      servicioHabito.actualizarEstadoItemChecklist(idItem, idHabito);

      return "{\"status\":\"success\", \"mensaje\":\"Estado actualizado\"}";
    } catch (Exception excepcion) {
      return "{\"status\":\"error\", \"mensaje\":\"No se pudo actualizar el estado\"}";
    }
  }

  @RequestMapping(
    path = "/habito/{idHabito}/editar-checklist/{idItem}",
    method = RequestMethod.POST,
    produces = PRODUCES_JSON
  )
  @ResponseBody
  public String editarChecklist(
    @PathVariable Integer idHabito,
    @PathVariable Integer idItem,
    @RequestParam("nuevaDescripcion") String nuevaDescripcion
  ) {
    try {
      servicioHabito.editarDescripcionItemChecklist(idItem, idHabito, nuevaDescripcion);

      return "{\"status\":\"success\", \"mensaje\":\"Checklist editado correctamente\"}";
    } catch (Exception excepcion) {
      return "{\"status\":\"error\", \"mensaje\":\"No se pudo editar el checklist\"}";
    }
  }

  @RequestMapping(path = "/habito/{id}", method = RequestMethod.GET, produces = PRODUCES_JSON)
  @ResponseBody
  public String obtenerHabito(@PathVariable Integer id) {
    Habito habito = servicioHabito.buscarHabitoPorId(id);

    StringBuilder checklistJson = new StringBuilder("[");
    List<ItemChecklist> items = habito.getCantidadDeChecklist();

    if (items != null && !items.isEmpty()) {
      for (int i = 0; i < items.size(); i++) {
        ItemChecklist item = items.get(i);

        checklistJson.append(
          String.format(
            "{\"id\": %d, \"descripcion\": \"%s\", \"estadoChecklist\": %b}",
            item.getId(),
            escaparJson(item.getDescripcion()),
            item.getEstadoChecklist()
          )
        );

        if (i < items.size() - 1) {
          checklistJson.append(",");
        }
      }
    }

    checklistJson.append("]");

    return String.format(
      "{\"titulo\":\"%s\",\"descripcion\":\"%s\",\"frecuencia\":\"%s\",\"duracionEstimada\":\"%s\",\"checklist\":%s}",
      escaparJson(habito.getTitulo()),
      escaparJson(habito.getDescripcion()),
      escaparJson(habito.getFrecuencia()),
      habito.getDuracionEstimada(),
      checklistJson.toString()
    );
  }

  private String escaparJson(String texto) {
    if (texto == null) {
      return "";
    }

    return texto
      .replace("\\", "\\\\")
      .replace("\"", "\\\"")
      .replace("\n", "\\n")
      .replace("\r", "\\r");
  }
}
