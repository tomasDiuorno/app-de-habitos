package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import javax.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioRegistro")
@Transactional
public class ServicioRegistroImp implements ServicioRegistro {

  private RepositorioUsuario repositorioUsuario;

  @Autowired
  public ServicioRegistroImp(RepositorioUsuario repositorioUsuario) {
    this.repositorioUsuario = repositorioUsuario;
  }

  @Override
  public void registrar(Usuario usuario) throws UsuarioExistente {
    Usuario usuarioEncontrado = repositorioUsuario.buscarPorEmail(usuario.getEmail());
    if (usuarioEncontrado != null) {
      throw new UsuarioExistente();
    }

    String hash = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt()); //Genera Hash a partir de la contraseña
    usuario.setPassword(hash); //Reemplaza contraseña por hash seguro

    repositorioUsuario.guardar(usuario); //Guardamos usuario. la bd recibe hash.
  }
}
