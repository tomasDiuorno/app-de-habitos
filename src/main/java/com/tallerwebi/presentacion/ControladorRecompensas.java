package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioRecompensas;
import com.tallerwebi.dominio.Usuario;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRecompensas {

  private ServicioRecompensas servicioRecompensas;
  private String usuario = "usuario";

  @Autowired
  public ControladorRecompensas(ServicioRecompensas servicioRecompensas) {
    this.servicioRecompensas = servicioRecompensas;
  }

  @RequestMapping(path = "/recompensas", method = RequestMethod.GET)
  public ModelAndView irARecompensas(HttpServletRequest request) {
    ModelMap model = new ModelMap();
    Usuario usuario = (Usuario) request.getSession().getAttribute(this.usuario);
    model.put(this.usuario, usuario);
    model.put("recompensas", this.servicioRecompensas.obtenerRecompensas());
    return new ModelAndView("recompensas", model);
  }

  @RequestMapping(path = "/baul", method = RequestMethod.GET)
  public ModelAndView irAlBaul(HttpServletRequest request) {
    ModelMap model = new ModelMap();
    Usuario usuario = (Usuario) request.getSession().getAttribute(this.usuario);
    this.servicioRecompensas.verificarRecompensas(usuario);
    model.put(this.usuario, usuario);
    model.put("recompensas", this.servicioRecompensas.obtenerBaul(usuario));
    return new ModelAndView("baul", model);
  }
}
