package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.HabitoNoPerteneceAlUsuarioException;
import com.tallerwebi.dominio.excepcion.HabitoYaCompletadoHoyException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service("servicioHistorialHabito")
@Transactional
public class ServicioHistorialHabitoImp implements ServicioHistorialHabito {

  private RepositorioHistorialHabito repositorioHistorialHabito;
  private RepositorioHabito repositorioHabito;

  @Autowired
  public ServicioHistorialHabitoImp(RepositorioHistorialHabito repositorioHistorialHabito, RepositorioHabito repositorioHabito) {
    this.repositorioHistorialHabito = repositorioHistorialHabito;
    this.repositorioHabito = repositorioHabito;
  }

  @Override
  public void marcarHabitoComoCompletado(Usuario usuario, Integer habitoId) throws HabitoNoPerteneceAlUsuarioException, HabitoYaCompletadoHoyException {
    Habito habitoDelUsuario = null;

    if (usuario.getUsuarioHabito() != null) {
      for (UsuarioHabito uh : usuario.getUsuarioHabito()) {
        if (uh.getHabito() != null && uh.getHabito().getId().equals(habitoId)) {
          habitoDelUsuario = uh.getHabito();
          break;
        }
      }
    }

    if (habitoDelUsuario == null) {
      throw new HabitoNoPerteneceAlUsuarioException();
    }

    LocalDate hoy = LocalDate.now();
    HistorialHabito existente = repositorioHistorialHabito.obtenerPorUsuarioHabitoYFecha(usuario, habitoDelUsuario, hoy);

    if (existente != null) {
      throw new HabitoYaCompletadoHoyException();
    }

    HistorialHabito nuevo = new HistorialHabito();
    nuevo.setUsuario(usuario);
    nuevo.setHabito(habitoDelUsuario);
    nuevo.setFechaCompletado(hoy);
    repositorioHistorialHabito.guardar(nuevo);
  }

  @Override
  public List<HistorialHabito> obtenerHistorial(Usuario usuario) {
    return repositorioHistorialHabito.obtenerPorUsuario(usuario);
  }
}
