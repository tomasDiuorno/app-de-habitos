package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.ServicioCategoria;
import com.tallerwebi.dominio.ServicioHabito;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.dominio.excepcion.HabitoNoPerteneceAlUsuarioException;
import com.tallerwebi.dominio.excepcion.HabitoYaCompletadoHoyException;
import com.tallerwebi.dominio.ServicioHistorialHabito;
import com.tallerwebi.dominio.HistorialHabito;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorHabitos {

  private ServicioHabito servicioHabito;
  private ServicioCategoria servicioCategoria;
  private ServicioHistorialHabito servicioHistorialHabito;

  @Autowired
  public ControladorHabitos(ServicioHabito servicioHabito, ServicioCategoria servicioCategoria, ServicioHistorialHabito servicioHistorialHabito) {
    this.servicioHabito = servicioHabito;
    this.servicioCategoria = servicioCategoria;
    this.servicioHistorialHabito = servicioHistorialHabito;
  }

  @RequestMapping(path = "/habitos", method = RequestMethod.GET)
  public ModelAndView irAHabitos(HttpServletRequest request) {
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

    if (usuario == null) {
      return new ModelAndView("redirect:/login");
    }

    ModelAndView modelAndView = new ModelAndView("habitos");
    modelAndView.addObject("usuarioHabitos", usuario.getUsuarioHabito());

    return modelAndView;
  }

  @RequestMapping(path = "/crear-habito", method = RequestMethod.GET)
  public ModelAndView irACrearHabito() {
    ModelMap model = new ModelMap();
    List<Categoria> categorias = this.servicioCategoria.obtenerCategorias();
    model.put("categorias", categorias);
    model.put("datosRegistroHabito", new DatosRegistroHabito());
    return new ModelAndView("crear-habito", model);
  }

  @RequestMapping(path = "/crear-habito", method = RequestMethod.POST)
  public ModelAndView crearHabito(
    @ModelAttribute("datosRegistroHabitos") DatosRegistroHabito datos,
    HttpServletRequest request
  ) {
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

    if (usuario == null) {
      return new ModelAndView("redirect:/login");
    }

    try {
      Habito habito = this.servicioHabito.obtenerHabito(datos);
      this.servicioHabito.agregarHabitoParaUsuario(habito, usuario);
    } catch (HabitoExistenteExeption excepcion) {
      ModelAndView modelAndView = new ModelAndView("crear-habito");
      modelAndView.addObject("error", "Ya existe un hábito con ese nombre");
      return modelAndView;
    } catch (LimiteHabitosAlcanzadoException excepcion) {
      ModelAndView modelAndView = new ModelAndView("crear-habito");
      modelAndView.addObject("error", "No podés tener más de 4 hábitos activos");
      return modelAndView;
    }
    return new ModelAndView("redirect:/habitos");
  }

  @RequestMapping(path = "/habitos/completar", method = RequestMethod.POST)
  public ModelAndView completarHabito(Integer habitoId, HttpServletRequest request) {
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
    if (usuario == null) {
      return new ModelAndView("redirect:/login");
    }

    try {
      this.servicioHistorialHabito.marcarHabitoComoCompletado(usuario, habitoId);
    } catch (HabitoNoPerteneceAlUsuarioException e) {
      request.getSession().setAttribute("errorCompletar", e.getMessage());
    } catch (HabitoYaCompletadoHoyException e) {
      request.getSession().setAttribute("errorCompletar", e.getMessage());
    }
    return new ModelAndView("redirect:/habitos");
  }

  @RequestMapping(path = "/historial", method = RequestMethod.GET)
  public ModelAndView verHistorial(HttpServletRequest request) {
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
    if (usuario == null) {
      return new ModelAndView("redirect:/login");
    }

    ModelAndView modelAndView = new ModelAndView("historial");
    List<HistorialHabito> historial = this.servicioHistorialHabito.obtenerHistorial(usuario);
    modelAndView.addObject("historial", historial);

    return modelAndView;
  }
}
