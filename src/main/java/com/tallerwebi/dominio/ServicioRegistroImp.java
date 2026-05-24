package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosRegistro;
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
  public void registrar(DatosRegistro datos) throws UsuarioExistente {
    Usuario usuarioEncontrado = repositorioUsuario.buscarPorEmail(datos.getEmail());
    if (usuarioEncontrado != null) {
      throw new UsuarioExistente();
    }
    Usuario usuario = crearUsuario(datos);
    String hash = BCrypt.hashpw(datos.getPassword(), BCrypt.gensalt()); //Genera Hash a partir de la contraseña
    usuario.setPassword(hash); //Reemplaza contraseña por hash seguro
    repositorioUsuario.guardar(usuario); //Guardamos usuario. la bd recibe hash.
  }

  private Usuario crearUsuario(DatosRegistro datos) {
    Usuario usuario = new Usuario();
    usuario.setName(datos.getName());
    usuario.setSurname(datos.getSurname());
    usuario.setEmail(datos.getEmail());
    usuario.setGender(datos.getGender());
    usuario.setUsername(datos.getUsername());
    return usuario;
  }

  @Override
  public void registrarHabitos(DatosRegistro datos) {}
}
