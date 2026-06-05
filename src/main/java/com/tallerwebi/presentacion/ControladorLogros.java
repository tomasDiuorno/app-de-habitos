package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioLogro;
import com.tallerwebi.dominio.interfaz.ServicioLogro;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorLogros {

  private ServicioLogro servicioLogro;

  @Autowired
  public ControladorLogros(ServicioLogro servicioLogro) {
    this.servicioLogro = servicioLogro;
  }

  @RequestMapping(path = "/logros", method = RequestMethod.GET)
  public ModelAndView irALogros(HttpServletRequest request) {
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

    if (usuario == null) {
      return new ModelAndView("redirect:/login");
    }

    List<UsuarioLogro> logrosDesbloqueados = (usuario.getId() != null)
      ? servicioLogro.obtenerLogrosDeUsuario(usuario)
      : new ArrayList<>();

    List<String> nombresDesbloqueados = logrosDesbloqueados
      .stream()
      .map(ul -> ul.getLogro().getNombre())
      .collect(java.util.stream.Collectors.toList());

    ModelAndView modelAndView = new ModelAndView("logros");
    modelAndView.addObject("logrosDesbloqueados", logrosDesbloqueados);
    modelAndView.addObject("nombresDesbloqueados", nombresDesbloqueados);
    return modelAndView;
  }
}
