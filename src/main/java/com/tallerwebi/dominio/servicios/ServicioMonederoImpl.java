package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.Monedero;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.interfaz.RepositorioMonedero;
import com.tallerwebi.dominio.interfaz.ServicioMonedero;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioMonedero")
@Transactional
public class ServicioMonederoImpl implements ServicioMonedero {

    private static final Integer MONEDAS_POR_LOGRO      = 20;
    private static final Integer MONEDAS_POR_RECOMPENSA = 10;

    private RepositorioMonedero repositorioMonedero;

    @Autowired
    public ServicioMonederoImpl(RepositorioMonedero repositorioMonedero) {
        this.repositorioMonedero = repositorioMonedero;
    }

    @Override
    public void inicializarMonedero(Usuario usuario) {
        Monedero existente = repositorioMonedero.buscarPorUsuario(usuario);
        if (existente != null) return;

        Monedero monedero = new Monedero();
        monedero.setUsuario(usuario);
        monedero.setSaldo(0);
        repositorioMonedero.guardar(monedero);
    }

    @Override
    public void acreditarPorLogro(Usuario usuario) {
        acreditarMonedas(usuario, MONEDAS_POR_LOGRO, "LOGRO", "Logro desbloqueado");
    }

    @Override
    public void acreditarPorRecompensa(Usuario usuario) {
        acreditarMonedas(
            usuario, MONEDAS_POR_RECOMPENSA, "RECOMPENSA", "Recompensa obtenida"
        );
    }

    @Override
    public void acreditarMonedas(
        Usuario usuario,
        Integer monto,
        String tipo,
        String descripcion
    ) {
        if (monto <= 0) return;

        Monedero monedero = repositorioMonedero.buscarPorUsuario(usuario);
        if (monedero == null) return;

        monedero.setSaldo(monedero.getSaldo() + monto);
        repositorioMonedero.guardar(monedero);

        Transaccion transaccion = new Transaccion();
        transaccion.setMonedero(monedero);
        transaccion.setMonto(monto);
        transaccion.setTipo(tipo);
        transaccion.setDescripcion(descripcion);
        repositorioMonedero.guardarTransaccion(transaccion);
    }

    @Override
    public Integer obtenerSaldo(Usuario usuario) {
        Monedero monedero = repositorioMonedero.buscarPorUsuario(usuario);
        if (monedero == null) return 0;
        return monedero.getSaldo();
    }

    @Override
    public List<Transaccion> obtenerTransacciones(Usuario usuario) {
        Monedero monedero = repositorioMonedero.buscarPorUsuario(usuario);
        if (monedero == null) return new ArrayList<>();
        return monedero.getTransacciones();
    }
}