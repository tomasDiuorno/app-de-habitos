package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioRecompensa;
import com.tallerwebi.dominio.interfaz.ServicioMonedero;
import com.tallerwebi.dominio.interfaz.ServicioRecompensas;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRecompensas {

  private ServicioRecompensas servicioRecompensas;
  private ServicioMonedero servicioMonedero;
  private String usuario = "usuario";

  @Autowired
  public ControladorRecompensas(
    ServicioRecompensas servicioRecompensas,
    ServicioMonedero servicioMonedero
  ) {
    this.servicioRecompensas = servicioRecompensas;
    this.servicioMonedero = servicioMonedero;
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

  @RequestMapping(path = "/usar-recompensa", method = RequestMethod.POST)
  public ModelAndView usarRecompensa(
    @RequestParam Integer idUsuarioRecompensa,
    HttpServletRequest request
  ) {
    UsuarioRecompensa ur = servicioRecompensas.obtenerPorId(idUsuarioRecompensa);

    if (ur != null && esRecompensaDeMonedas(ur.getRecompensa().getNombre())) {
      Usuario usuarioSesion = (Usuario) request.getSession().getAttribute(this.usuario);
      servicioMonedero.acreditarPorRecompensa(usuarioSesion);
    }

    servicioRecompensas.marcarComoUtilizada(idUsuarioRecompensa);
    return new ModelAndView("redirect:/baul");
  }

  private boolean esRecompensaDeMonedas(String nombre) {
    return nombre != null && nombre.toLowerCase(java.util.Locale.ROOT).contains("monedas");
  }
}
