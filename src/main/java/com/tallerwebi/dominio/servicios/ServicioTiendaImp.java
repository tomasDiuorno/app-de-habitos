package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Bonificacion;
import com.tallerwebi.dominio.entidades.Monedero;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioBonificacion;
import com.tallerwebi.dominio.interfaz.RepositorioBonificacion;
import com.tallerwebi.dominio.interfaz.RepositorioMonedero;
import com.tallerwebi.dominio.interfaz.RepositorioUsuarioBonificacion;
import com.tallerwebi.dominio.interfaz.ServicioTienda;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioTienda")
@Transactional
public class ServicioTiendaImp implements ServicioTienda {

  private static final String TIPO_TRANSACCION_BONIFICACION = "BONIFICACION";

  private RepositorioBonificacion repositorioBonificacion;
  private RepositorioUsuarioBonificacion repositorioUsuarioBonificacion;
  private RepositorioMonedero repositorioMonedero;

  @Autowired
  public ServicioTiendaImp(
    RepositorioBonificacion repositorioBonificacion,
    RepositorioUsuarioBonificacion repositorioUsuarioBonificacion,
    RepositorioMonedero repositorioMonedero
  ) {
    this.repositorioBonificacion = repositorioBonificacion;
    this.repositorioUsuarioBonificacion = repositorioUsuarioBonificacion;
    this.repositorioMonedero = repositorioMonedero;
  }

  @Override
  public List<Bonificacion> obtenerBonificacionesDisponibles() {
    return repositorioBonificacion.buscarDisponibles();
  }

  @Override
  public UsuarioBonificacion obtenerBonificacionActiva(Usuario usuario) {
    return repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(usuario.getId());
  }

  @Override
  public void activarBonificacion(Usuario usuario, Integer idBonificacion) {
    validarQueNoTengaBonificacionActiva(usuario);

    Bonificacion bonificacion = obtenerBonificacionValida(idBonificacion);
    Monedero monedero = obtenerMonederoValido(usuario);

    validarSaldoSuficiente(monedero, bonificacion);

    descontarMonedas(monedero, bonificacion);
    registrarTransaccion(monedero, bonificacion);
    guardarBonificacionActiva(usuario, bonificacion);
  }

  private void validarQueNoTengaBonificacionActiva(Usuario usuario) {
    UsuarioBonificacion bonificacionActiva =
      repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(usuario.getId());

    if (bonificacionActiva != null) {
      throw new RuntimeException("Ya tenés una bonificación activa");
    }
  }

  private Bonificacion obtenerBonificacionValida(Integer idBonificacion) {
    Bonificacion bonificacion = repositorioBonificacion.buscarPorId(idBonificacion);

    if (bonificacion == null) {
      throw new RuntimeException("La bonificación no existe");
    }

    if (!bonificacion.getDisponible()) {
      throw new RuntimeException("La bonificación no está disponible");
    }

    return bonificacion;
  }

  private Monedero obtenerMonederoValido(Usuario usuario) {
    Monedero monedero = repositorioMonedero.buscarPorUsuario(usuario);

    if (monedero == null) {
      throw new RuntimeException("El usuario no tiene monedero");
    }

    return monedero;
  }

  private void validarSaldoSuficiente(Monedero monedero, Bonificacion bonificacion) {
    if (monedero.getSaldo() < bonificacion.getPrecioMonedas()) {
      throw new RuntimeException("No tenés monedas suficientes");
    }
  }

  private void descontarMonedas(Monedero monedero, Bonificacion bonificacion) {
    Integer nuevoSaldo = monedero.getSaldo() - bonificacion.getPrecioMonedas();

    monedero.setSaldo(nuevoSaldo);
    repositorioMonedero.guardar(monedero);
  }

  private void registrarTransaccion(Monedero monedero, Bonificacion bonificacion) {
    Transaccion transaccion = new Transaccion();

    transaccion.setMonedero(monedero);
    transaccion.setMonto(-bonificacion.getPrecioMonedas());
    transaccion.setTipo(TIPO_TRANSACCION_BONIFICACION);
    transaccion.setDescripcion("Canje de bonificación: " + bonificacion.getNombre());

    repositorioMonedero.guardarTransaccion(transaccion);
  }

  private void guardarBonificacionActiva(Usuario usuario, Bonificacion bonificacion) {
    LocalDateTime fechaActivacion = LocalDateTime.now();
    LocalDateTime fechaExpiracion = fechaActivacion.plusDays(bonificacion.getDuracionEnDias());

    UsuarioBonificacion usuarioBonificacion = new UsuarioBonificacion();

    usuarioBonificacion.setUsuario(usuario);
    usuarioBonificacion.setBonificacion(bonificacion);
    usuarioBonificacion.setFechaActivacion(fechaActivacion);
    usuarioBonificacion.setFechaExpiracion(fechaExpiracion);
    usuarioBonificacion.setActiva(true);

    repositorioUsuarioBonificacion.guardar(usuarioBonificacion);
  }
}
