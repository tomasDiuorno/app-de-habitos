package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.HistorialHabito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.ServicioHistorialHabito;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorHistorial {

  private ServicioHistorialHabito servicioHistorialHabito;

  @Autowired
  public ControladorHistorial(ServicioHistorialHabito servicioHistorialHabito) {
    this.servicioHistorialHabito = servicioHistorialHabito;
  }

  @RequestMapping(path = "/historial", method = RequestMethod.GET)
  public ModelAndView irAHistorial(HttpServletRequest request) {
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

    if (usuario == null) {
      return new ModelAndView("redirect:/login");
    }

    List<HistorialHabito> historial = servicioHistorialHabito.obtenerHistorial(usuario);

    ModelMap modelo = new ModelMap();
    modelo.put("historial", historial);

    return new ModelAndView("historial", modelo);
  }
}
