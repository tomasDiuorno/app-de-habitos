package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Usuario;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorPerfil {

  @RequestMapping("/perfil")
  public ModelAndView irAPerfil(HttpServletRequest request) {

    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

    if (usuario == null) {
      return new ModelAndView("redirect:/login");
    }

    ModelMap modelo = new ModelMap();
    modelo.put("usuario", usuario);

    return new ModelAndView("perfil", modelo);
  }
}