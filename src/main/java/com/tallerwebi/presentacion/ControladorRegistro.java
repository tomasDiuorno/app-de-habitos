package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioHabito;
import com.tallerwebi.dominio.ServicioRegistro;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRegistro {

  private ServicioRegistro servicioRegistro;
  private ServicioHabito servicioHabitos;

  public ControladorRegistro(ServicioRegistro servicioRegistro, ServicioHabito servicioHabitos) {
    this.servicioRegistro = servicioRegistro;
    this.servicioHabitos = servicioHabitos;
  }

  @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
  public ModelAndView registrarme(
    @Valid @ModelAttribute("datosRegistro") DatosRegistro datos,
    BindingResult result
  ) {
    ModelMap model = new ModelMap();
    if (result.hasErrors()) {
      model.put("datosRegistro", datos);
      model.put("habitos", servicioHabitos.obtenerHabitosIniciales());
      return new ModelAndView("nuevo-usuario", model);
    }
    try {
      servicioRegistro.registrar(datos);
    } catch (UsuarioExistente e) {
      model.put("error", "El usuario ya existe");
      model.put("habitos", servicioHabitos.obtenerHabitosIniciales());
      return new ModelAndView("nuevo-usuario", model);
    } catch (Exception e) {
      model.put("error", "Error al registrar el nuevo usuario");
      model.put("habitos", servicioHabitos.obtenerHabitosIniciales());
      return new ModelAndView("nuevo-usuario", model);
    }

    return new ModelAndView("redirect:/login");
  }
}
