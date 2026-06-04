package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.ItemChecklist;
import com.tallerwebi.dominio.ServicioCategoria;
import com.tallerwebi.dominio.ServicioHabito;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.ChecklistInsuficienteExeption;
import com.tallerwebi.dominio.excepcion.DescripcionChecklistInvalidaException;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.HabitoNoEncontradoException;
import com.tallerwebi.dominio.excepcion.ItemChecklistNoEncontradoException;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private static final String STATUS = "status";
  private static final String SUCCESS = "success";
  private static final String ERROR = "error";
  private static final String MENSAJE = "mensaje";

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
      HttpServletRequest request) {
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

    if (usuario == null) {
      return new ModelAndView(REDIRECT_LOGIN);
    }

    Integer cantidadHabitosAntes = usuario.getUsuarioHabito().size();

    try {
      Habito habito = this.servicioHabito.obtenerHabito(datos);
      this.servicioHabito.agregarHabitoParaUsuario(habito, usuario);

      Integer cantidadHabitosDespues = usuario.getUsuarioHabito().size();

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
      int cantidadHabitosDespues) {
    if (cantidadHabitosAntes == CERO_HABITOS && cantidadHabitosDespues == UN_HABITO) {
      cargarDatosDelLogro(
          modelAndView,
          "Primer hábito creado",
          "Creaste tu primer hábito. Tu rutina acaba de empezar.");
    }

    if (cantidadHabitosAntes == DOS_HABITOS && cantidadHabitosDespues == TRES_HABITOS) {
      cargarDatosDelLogro(
          modelAndView,
          "Constante",
          "Ya tenés 3 hábitos activos. Estás construyendo una rutina.");
    }

    if (cantidadHabitosAntes == TRES_HABITOS && cantidadHabitosDespues == CUATRO_HABITOS) {
      cargarDatosDelLogro(modelAndView, "Experto", "Llegaste al máximo de 4 hábitos activos.");
    }
  }

  private void cargarDatosDelLogro(
      ModelAndView modelAndView,
      String tituloLogro,
      String descripcionLogro) {
    modelAndView.addObject(ATRIBUTO_MOSTRAR_LOGRO, true);
    modelAndView.addObject(ATRIBUTO_TITULO_LOGRO, tituloLogro);
    modelAndView.addObject(ATRIBUTO_DESCRIPCION_LOGRO, descripcionLogro);
  }

  @RequestMapping(path = "/habito/{idHabito}/agregar-checklist", method = RequestMethod.POST, produces = PRODUCES_JSON)
  @ResponseBody
  public Map<String, Object> agregarChecklist(
      @PathVariable Integer idHabito, @RequestParam("descripcion") String descripcion)
      throws ChecklistInsuficienteExeption {

    Map<String, Object> respuesta = new HashMap<>();
    ItemChecklist itemChecklist = new ItemChecklist();
    itemChecklist.setDescripcion(descripcion);

    try {
      servicioHabito.agregarItemChecklistAlHabito(itemChecklist, idHabito);
      respuesta.put("ok", true);
      respuesta.put("mensaje", "Item agregado correctamente");
    } catch (HabitoNoEncontradoException e) {
      respuesta.put("ok", false);
      respuesta.put("error", "No se encontró el hábito");
    }
    return respuesta;
  }

  @RequestMapping(path = "/habito/{idHabito}/eliminar-checklist/{idItem}", method = RequestMethod.POST, produces = PRODUCES_JSON)
  @ResponseBody
  public Map<String, Object> eliminarChecklist(@PathVariable Integer idHabito, @PathVariable Integer idItem) {
    Map<String, Object> respuesta = new HashMap<>();

    try {
      servicioHabito.eliminarItemChecklistDelHabito(idHabito, idItem);
      respuesta.put("mensaje", "Checklist eliminado");
    } catch (HabitoNoEncontradoException exception) {
      respuesta.put("error", "No se encontró el hábito");
    } catch (ItemChecklistNoEncontradoException exception) {
      respuesta.put("error", "No se encontró el item");
    } catch (Exception exception) {
      respuesta.put("error", "No se pudo eliminar el checklist");
    }

    return respuesta;
  }

  @RequestMapping(path = "/habito/{idHabito}/toggle-checklist/{idItem}", method = RequestMethod.POST, produces = PRODUCES_JSON)
  @ResponseBody
  public Map<String, Object> alternarEstadoChecklist(@PathVariable Integer idHabito,
      @PathVariable Integer idItem) {
    Map<String, Object> respuesta = new HashMap<>();

    try {
      servicioHabito.actualizarEstadoItemChecklist(idItem, idHabito);

      respuesta.put(STATUS, SUCCESS);
      respuesta.put(MENSAJE, "Estado actualizado");

    } catch (HabitoNoEncontradoException | ItemChecklistNoEncontradoException exception) {
      respuesta.put(STATUS, ERROR);
      respuesta.put(MENSAJE, "No se pudo actualizar la tarea. Intentá nuevamente.");

    } catch (Exception exception) {
      respuesta.put(STATUS, ERROR);
      respuesta.put(MENSAJE, "Ocurrió un error inesperado al actualizar la tarea.");
    }

    return respuesta;
  }

  @RequestMapping(path = "/habito/{idHabito}/editar-checklist/{idItem}", method = RequestMethod.POST, produces = PRODUCES_JSON)
  @ResponseBody
  public Map<String, Object> editarChecklist(@PathVariable Integer idHabito,
      @PathVariable Integer idItem, @RequestParam("nuevaDescripcion") String nuevaDescripcion) {
    Map<String, Object> respuesta = new HashMap<>();

    try {
      servicioHabito.editarDescripcionItemChecklist(idItem, idHabito, nuevaDescripcion);

      respuesta.put(STATUS, SUCCESS);
      respuesta.put("mensaje", "Checklist editado correctamente");

    } catch (HabitoNoEncontradoException | ItemChecklistNoEncontradoException exception) {
      respuesta.put(STATUS, ERROR);
      respuesta.put("mensaje", "No se pudo editar la tarea. Intentá nuevamente.");

    } catch (DescripcionChecklistInvalidaException exception) {
      respuesta.put(STATUS, ERROR);
      respuesta.put("mensaje", "La descripción no puede estar vacía.");

    } catch (Exception exception) {
       respuesta.put(STATUS, ERROR);
      respuesta.put("mensaje", "Ocurrió un error inesperado al editar la tarea.");
    }

    return respuesta;
  }

  @RequestMapping(path = "/habito/{id}", method = RequestMethod.GET, produces = PRODUCES_JSON)
  @ResponseBody
  public Map<String, Object> obtenerHabito(@PathVariable Integer id) throws HabitoNoEncontradoException {
    Habito habito = servicioHabito.buscarHabitoPorId(id);

    Map<String, Object> respuesta = new HashMap<>();

    respuesta.put("titulo", habito.getTitulo());
    respuesta.put("descripcion", habito.getDescripcion());
    respuesta.put("frecuencia", habito.getFrecuencia());
    respuesta.put("duracionEstimada", habito.getDuracionEstimada());

    List<Map<String, Object>> checklist = new ArrayList<>();

    if (habito.getCantidadDeChecklist() != null) {
      for (ItemChecklist item : habito.getCantidadDeChecklist()) {
        checklist.add(convertirItemChecklistAJson(item));
      }
    }

    respuesta.put("checklist", checklist);

    return respuesta;
  }

  private Map<String, Object> convertirItemChecklistAJson(ItemChecklist item) {
    Map<String, Object> itemJson = new HashMap<>();

    itemJson.put("id", item.getId());
    itemJson.put("descripcion", item.getDescripcion());
    itemJson.put("estadoChecklist", item.getEstadoChecklist());

    return itemJson;
  }
}
