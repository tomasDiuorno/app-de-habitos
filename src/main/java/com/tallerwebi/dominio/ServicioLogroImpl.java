package com.tallerwebi.dominio;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioLogro")
@Transactional
public class ServicioLogroImpl implements ServicioLogro {

  private RepositorioLogro repositorioLogro;
  private RepositorioUsuarioLogro repositorioUsuarioLogro;

  private static final int PRIMER_LOGRO = 1;
  private static final int TRES_HABITOS = 3;
  private static final int DIEZ_HABITOS = 4;

  @Autowired
  public ServicioLogroImpl(
    RepositorioLogro repositorioLogro,
    RepositorioUsuarioLogro repositorioUsuarioLogro
  ) {
    this.repositorioLogro = repositorioLogro;
    this.repositorioUsuarioLogro = repositorioUsuarioLogro;
  }

  @Override
  public void verificarLogros(Usuario usuario) {
    int cantidadHabitos = usuario.getUsuarioHabito().size();

    if (cantidadHabitos >= PRIMER_LOGRO) {
      desbloquearLogro(usuario, "Primer hábito creado");
    }

    if (cantidadHabitos >= TRES_HABITOS) {
      desbloquearLogro(usuario, "3 hábitos creados");
    }

    if (cantidadHabitos >= DIEZ_HABITOS) {
      desbloquearLogro(usuario, "4 hábitos creados");
    }
  }

  @Override
  public List<UsuarioLogro> obtenerLogrosDeUsuario(Usuario usuario) {
    return repositorioUsuarioLogro.buscarPorUsuario(usuario.getId());
  }

  private void desbloquearLogro(Usuario usuario, String nombreLogro) {
    Logro logro = repositorioLogro.buscarPorNombre(nombreLogro);
    if (logro == null) {
      throw new RuntimeException("NO SE ENCONTRO EL LOGRO: " + nombreLogro);
    }

    UsuarioLogro usuarioLogroExistente = repositorioUsuarioLogro.buscarPorUsuarioYLogro(
      usuario,
      logro
    );

    if (usuarioLogroExistente == null) {
      UsuarioLogro usuarioLogro = new UsuarioLogro();

      usuarioLogro.setUsuario(usuario);
      usuarioLogro.setLogro(logro);
      usuarioLogro.setDesbloqueado(true);

      repositorioUsuarioLogro.guardar(usuarioLogro);
    }
  }
}
