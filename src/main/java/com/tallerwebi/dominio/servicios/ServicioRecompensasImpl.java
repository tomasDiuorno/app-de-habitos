package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Recompensa;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioRecompensa;
import com.tallerwebi.dominio.interfaz.RepositorioRecompensas;
import com.tallerwebi.dominio.interfaz.RepositorioUsuarioRecompensa;
import com.tallerwebi.dominio.interfaz.ServicioRecompensas;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioRecompensas")
@Transactional
public class ServicioRecompensasImpl implements ServicioRecompensas {

  private RepositorioRecompensas repositorioRecompensas;
  private RepositorioUsuarioRecompensa repositorioUsuarioRecompensa;

  @Autowired
  public ServicioRecompensasImpl(
    RepositorioRecompensas repositorioRecompensas,
    RepositorioUsuarioRecompensa repositorioUsuarioRecompensa
  ) {
    this.repositorioRecompensas = repositorioRecompensas;
    this.repositorioUsuarioRecompensa = repositorioUsuarioRecompensa;
  }

  @Override
  public List<Recompensa> obtenerRecompensas() {
    return this.repositorioRecompensas.obtenerTodas();
  }

  @Override
  public void verificarRecompensas(Usuario usuario) {
    List<Recompensa> recompensasDisponibles =
      this.repositorioRecompensas.obtenerPorNivel(usuario.getNivelUsuario());
    for (Recompensa rec : recompensasDisponibles) {
      if (!repositorioUsuarioRecompensa.existeRecompensaUsuario(rec, usuario)) {
        UsuarioRecompensa usuarioRecompensa = new UsuarioRecompensa();
        usuarioRecompensa.setUsuario(usuario);
        usuarioRecompensa.setRecompensa(rec);
        repositorioUsuarioRecompensa.guardar(usuarioRecompensa);
      }
    }
  }

  @Override
  public List<UsuarioRecompensa> obtenerBaul(Usuario usuario) {
    return this.repositorioUsuarioRecompensa.obtenerPorUsuario(usuario.getId());
  }

  @Override
  public void marcarComoUtilizada(Integer idUsuarioRecompensa) {
    this.repositorioUsuarioRecompensa.utilizar(idUsuarioRecompensa);
  }
}
