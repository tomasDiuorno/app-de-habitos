package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.HistorialHabito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioHabito;
import com.tallerwebi.dominio.excepcion.HabitoNoPerteneceAlUsuarioException;
import com.tallerwebi.dominio.excepcion.HabitoYaCompletadoHoyException;
import com.tallerwebi.dominio.interfaz.RepositorioHistorialHabito;
import com.tallerwebi.dominio.interfaz.RepositorioUsuarioHabito;
import com.tallerwebi.dominio.interfaz.ServicioHistorialHabito;
import java.time.LocalDate;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioHistorialHabito")
@Transactional
public class ServicioHistorialHabitoImpl implements ServicioHistorialHabito {

  private RepositorioHistorialHabito repositorioHistorialHabito;
  private RepositorioUsuarioHabito repositorioUsuarioHabito;

  @Autowired
  public ServicioHistorialHabitoImpl(
    RepositorioHistorialHabito repositorioHistorialHabito,
    RepositorioUsuarioHabito repositorioUsuarioHabito
  ) {
    this.repositorioHistorialHabito = repositorioHistorialHabito;
    this.repositorioUsuarioHabito = repositorioUsuarioHabito;
  }

  @Override
  public void marcarHabitoComoCompletado(Usuario usuario, Integer habitoId)
    throws HabitoNoPerteneceAlUsuarioException, HabitoYaCompletadoHoyException {
    UsuarioHabito usuarioHabito = repositorioUsuarioHabito.obtenerPorIds(usuario.getId(), habitoId);

    if (usuarioHabito == null) {
      throw new HabitoNoPerteneceAlUsuarioException();
    }

    LocalDate hoy = LocalDate.now();
    HistorialHabito existente = repositorioHistorialHabito.obtenerPorUsuarioHabitoYFecha(
      usuarioHabito.getUsuario(),
      usuarioHabito.getHabito(),
      hoy
    );

    if (existente != null) {
      throw new HabitoYaCompletadoHoyException();
    }

    HistorialHabito nuevo = new HistorialHabito();
    nuevo.setUsuario(usuarioHabito.getUsuario());
    nuevo.setHabito(usuarioHabito.getHabito());
    nuevo.setFechaCompletado(hoy);
    repositorioHistorialHabito.guardar(nuevo);
  }

  @Override
  public List<HistorialHabito> obtenerHistorial(Usuario usuario) {
    return repositorioHistorialHabito.obtenerPorUsuario(usuario);
  }
}
