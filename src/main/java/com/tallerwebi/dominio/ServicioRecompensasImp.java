package com.tallerwebi.dominio;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioRecompensas")
@Transactional
public class ServicioRecompensasImp implements ServicioRecompensas {

  private RepositorioRecompensas repositorioRecompensas;
  private RepositorioUsuarioRecompensa repositorioUsuarioRecompensa;

  @Autowired
  public ServicioRecompensasImp(
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
