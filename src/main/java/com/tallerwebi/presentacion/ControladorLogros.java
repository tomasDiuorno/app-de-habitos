package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogro;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.UsuarioLogro;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorLogros {

  private ServicioLogro servicioLogro;

  @Autowired
  public ControladorLogros(ServicioLogro servicioLogro) {
    this.servicioLogro = servicioLogro;
  }

  @GetMapping("/logros")
  public ModelAndView irALogros(HttpServletRequest request) {
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

    List<UsuarioLogro> logros = servicioLogro.obtenerLogrosDeUsuario(usuario);

    ModelMap modelo = new ModelMap();
    modelo.put("logros", logros);

    return new ModelAndView("logros", modelo);
  }
}
