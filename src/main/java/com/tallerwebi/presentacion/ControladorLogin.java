package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.ServicioHabito;
import com.tallerwebi.dominio.interfaz.ServicioLogin;
import com.tallerwebi.dominio.interfaz.ServicioRecuperacionContrasenia;
import com.tallerwebi.presentacion.DTO.LoginDTO;
import com.tallerwebi.presentacion.DTO.RecuperacionContraseniaDTO;
import com.tallerwebi.presentacion.DTO.RegistroDTO;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorLogin {

  private ServicioLogin servicioLogin;
  private ServicioRecuperacionContrasenia servicioRecuperacionContrasenia;
  private ServicioHabito servicioHabitos;

  @Autowired
  public ControladorLogin(
    ServicioLogin servicioLogin,
    ServicioRecuperacionContrasenia servicioRecuperacionContrasenia,
    ServicioHabito servicioHabitos
  ) {
    this.servicioLogin = servicioLogin;
    this.servicioRecuperacionContrasenia = servicioRecuperacionContrasenia;
    this.servicioHabitos = servicioHabitos;
  }

  @RequestMapping("/login")
  public ModelAndView irALogin() {
    ModelMap modelo = new ModelMap();
    modelo.put("datosLogin", new LoginDTO());
    return new ModelAndView("login", modelo);
  }

  @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
  public ModelAndView validarLogin(
    @ModelAttribute("datosLogin") LoginDTO datosLogin,
    HttpServletRequest request
  ) {
    Usuario usuarioBuscado = servicioLogin.consultarUsuario(
      datosLogin.getEmailorusername(),
      datosLogin.getPassword()
    );

    if (usuarioBuscado != null) {
      request.getSession().setAttribute("ROL", usuarioBuscado.getRol());
      request.getSession().setAttribute("usuario", usuarioBuscado);
      return new ModelAndView("redirect:/home");
    } else {
      /*
       * Se instancia el ModelMap solo cuando es necesario (en el flujo de error) para
       * evitar anomalías en el flujo de datos (DU-anomaly de PMD)
       */
      ModelMap model = new ModelMap();
      model.put("error", "Usuario o clave incorrecta");
      return new ModelAndView("login", model);
    }
  }

  @RequestMapping(path = "/nuevo-usuario", method = RequestMethod.GET)
  public ModelAndView nuevoUsuario() {
    ModelMap model = new ModelMap();
    model.put("datosRegistro", new RegistroDTO());
    model.put("habitos", servicioHabitos.obtenerHabitosIniciales());
    return new ModelAndView("nuevo-usuario", model);
  }

  @RequestMapping(path = "/home", method = RequestMethod.GET)
  public ModelAndView irAHome() {
    return new ModelAndView("home");
  }

  @RequestMapping(path = "/", method = RequestMethod.GET)
  public ModelAndView inicio() {
    return new ModelAndView("redirect:/login");
  }

  @RequestMapping(path = "/recuperacion-contrasenia", method = RequestMethod.POST)
  public ModelAndView recuperacionDeContrasenia(
    @ModelAttribute("datosRecuperacion") RecuperacionContraseniaDTO datosRecuperacionContrasenia
  ) {
    ModelMap model = new ModelMap();
    try {
      servicioRecuperacionContrasenia.recuperarContrasenia(datosRecuperacionContrasenia);

      model.put("mensaje", "Contraseña cambiada con exito");
      return new ModelAndView("redirect:/login", model);
    } catch (Exception e) {
      model.put("error", e.getMessage());

      model.put("datosRecuperacion", datosRecuperacionContrasenia);
    }

    return new ModelAndView("recuperacion-contrasenia", model);
  }

  @RequestMapping(path = "/recuperacion-contrasenia", method = RequestMethod.GET)
  public ModelAndView irARecuperacionContrasenia() {
    ModelMap modelo = new ModelMap();

    modelo.put("datosRecuperacion", new RecuperacionContraseniaDTO(null, null, null));

    return new ModelAndView("recuperacion-contrasenia", modelo);
  }
}
