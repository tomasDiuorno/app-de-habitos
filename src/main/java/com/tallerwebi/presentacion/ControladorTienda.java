package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.ServicioMonedero;
import com.tallerwebi.dominio.interfaz.ServicioTienda;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorTienda {

  private ServicioTienda servicioTienda;
  private ServicioMonedero servicioMonedero;

  @Autowired
  public ControladorTienda(ServicioTienda servicioTienda, ServicioMonedero servicioMonedero) {
    this.servicioTienda = servicioTienda;
    this.servicioMonedero = servicioMonedero;
  }

  @RequestMapping(path = "/tienda", method = RequestMethod.GET)
  public ModelAndView irATienda(HttpServletRequest request) {
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

    if (usuario == null) {
      return new ModelAndView("redirect:/login");
    }

    ModelMap modelo = new ModelMap();

    modelo.put("bonificaciones", servicioTienda.obtenerBonificacionesDisponibles());
    modelo.put("bonificacionActiva", servicioTienda.obtenerBonificacionActiva(usuario));
    modelo.put("monedasUsuario", servicioMonedero.obtenerSaldo(usuario));

    return new ModelAndView("tienda", modelo);
  }

  @RequestMapping(path = "/tienda/activar/{idBonificacion}", method = RequestMethod.POST)
  public ModelAndView activarBonificacion(
    @PathVariable Integer idBonificacion,
    HttpServletRequest request
  ) {
    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

    if (usuario == null) {
      return new ModelAndView("redirect:/login");
    }

    try {
      servicioTienda.activarBonificacion(usuario, idBonificacion);
      return new ModelAndView("redirect:/tienda?activada=true");
    } catch (RuntimeException e) {
      return new ModelAndView("redirect:/tienda?error=true");
    }
  }
}
