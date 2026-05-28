package com.tallerwebi.dominio;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioLogro")
@Transactional
public class ServicioLogroImpl implements ServicioLogro {

  private RepositorioLogro repositorioLogro;
  private RepositorioUsuarioLogro repositorioUsuarioLogro;

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

    if (cantidadHabitos >= 1) {
      desbloquearLogro(usuario, "Primer hábito creado");
    }

    if (cantidadHabitos >= 3) {
      desbloquearLogro(usuario, "3 hábitos creados");
    }

    if (cantidadHabitos >= 10) {
      desbloquearLogro(usuario, "10 hábitos creados");
    }
  }

  private void desbloquearLogro(
    Usuario usuario,
    String nombreLogro
  ) {

    Logro logro = repositorioLogro.buscarPorNombre(nombreLogro);

    UsuarioLogro usuarioLogroExistente =
      repositorioUsuarioLogro.buscarPorUsuarioYLogro(
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