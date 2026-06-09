package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.ServicioMonedero;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorMonedero {

    private ServicioMonedero servicioMonedero;

    @Autowired
    public ControladorMonedero(ServicioMonedero servicioMonedero) {
        this.servicioMonedero = servicioMonedero;
    }

    @RequestMapping("/transacciones")
    public ModelAndView irATransacciones(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        Integer saldo = servicioMonedero.obtenerSaldo(usuario);
        List<Transaccion> transacciones = servicioMonedero.obtenerTransacciones(usuario);

        ModelMap modelo = new ModelMap();
        modelo.put("usuario", usuario);
        modelo.put("saldo", saldo);
        modelo.put("transacciones", transacciones);

        return new ModelAndView("transacciones", modelo);
    }
}