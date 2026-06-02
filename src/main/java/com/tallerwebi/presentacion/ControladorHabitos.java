package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Habito;
import com.tallerwebi.dominio.ServicioCategoria;
import com.tallerwebi.dominio.ServicioHabito;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorHabitos {

  private ServicioHabito servicioHabito;
  private ServicioCategoria servicioCategoria;

  @Autowired
  public ControladorHabitos(ServicioHabito servicioHabito, ServicioCategoria servicioCategoria) {
    this.servicioHabito = servicioHabito;
    this.servicioCategoria = servicioCategoria;
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

  @RequestMapping(path = "/habito/{id}", method = RequestMethod.GET)
  @ResponseBody
  public String obtenerHabito(@PathVariable Integer id) {
    Habito habito = servicioHabito.buscarHabitoPorId(id);

    return (
      "{" +
      "\"titulo\":\"" +
      habito.getTitulo() +
      "\"," +
      "\"descripcion\":\"" +
      habito.getDescripcion() +
      "\"," +
      "\"frecuencia\":\"" +
      habito.getFrecuencia() +
      "\"," +
      "\"duracionEstimada\":\"" +
      habito.getDuracionEstimada() +
      "\"" +
      "}"
    );
  }
}
