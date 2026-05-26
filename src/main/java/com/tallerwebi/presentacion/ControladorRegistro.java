package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioRegistro;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRegistro {

  private ServicioRegistro servicioRegistro;

  public ControladorRegistro(ServicioRegistro servicioRegistro) {
    this.servicioRegistro = servicioRegistro;
  }

  @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
  public ModelAndView registrarme(@ModelAttribute("datosRegistro") DatosRegistro datos) {
    ModelMap model = new ModelMap();
    try {
      servicioRegistro.registrar(datos);
    } catch (UsuarioExistente e) {
      model.put("error", "El usuario ya existe");
      return new ModelAndView("nuevo-usuario", model);
    } catch (Exception e) {
      model.put("error", "Error al registrar el nuevo usuario");
      return new ModelAndView("nuevo-usuario", model);
    }

    return new ModelAndView("redirect:/login");
  }
}
