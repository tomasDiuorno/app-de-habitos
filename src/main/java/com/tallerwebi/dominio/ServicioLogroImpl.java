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

  @Autowired
  public ServicioLogroImpl(
    RepositorioLogro repositorioLogro,
    RepositorioUsuarioLogro repositorioUsuarioLogro
  ) {
    this.repositorioLogro = repositorioLogro;
    this.repositorioUsuarioLogro = repositorioUsuarioLogro;
  }

  @Override
  public void verificarYAsignarLogros(Usuario usuario) {
    int cantidadHabitos = usuario.getUsuarioHabito().size();

    verificarLogro("Primer Paso", 1, cantidadHabitos, usuario);
    verificarLogro("Constante", 3, cantidadHabitos, usuario);
    verificarLogro("Experto", 4, cantidadHabitos, usuario);
  }

  private void verificarLogro(
    String nombreLogro,
    int cantidadRequerida,
    int cantidadActual,
    Usuario usuario
  ) {
    if (cantidadActual >= cantidadRequerida) {
      Logro logro = repositorioLogro.buscarPorNombre(nombreLogro);
      if (logro != null && !repositorioUsuarioLogro.existeLogroParaUsuario(usuario, logro)) {
        UsuarioLogro usuarioLogro = new UsuarioLogro();
        usuarioLogro.setUsuario(usuario);
        usuarioLogro.setLogro(logro);
        repositorioUsuarioLogro.guardar(usuarioLogro);
      }
    }
  }

  @Override
  public List<UsuarioLogro> obtenerLogrosDeUsuario(Usuario usuario) {
    return repositorioUsuarioLogro.buscarPorUsuario(usuario);
  }
}
