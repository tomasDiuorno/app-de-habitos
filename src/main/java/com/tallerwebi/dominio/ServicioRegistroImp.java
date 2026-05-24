package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CamposObligatorios;
import com.tallerwebi.dominio.excepcion.FormatoEmailInvalido;
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

    String hash = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt()); // Genera Hash a partir de la contraseña
    usuario.setPassword(hash); // Reemplaza contraseña por hash seguro

    repositorioUsuario.guardar(usuario); // Guardamos usuario. la bd recibe hash.
  }

  @Override
  public void validarCamposObligatorios(Usuario usuario) throws CamposObligatorios {
    
    if (usuario.getName() == null  || usuario.getName().isEmpty() &&
        usuario.getEmail() == null || usuario.getEmail().isEmpty() &&
        usuario.getSurname() == null || usuario.getSurname().isEmpty() &&
        usuario.getUsername() == null || usuario.getUsername().isEmpty() &&
        usuario.getPassword() == null || usuario.getPassword().isEmpty() &&
        usuario.getConfirmPass() == null || usuario.getConfirmPass().isEmpty()) {

          throw new CamposObligatorios();
    }
  }

  @Override
  public void validarEmail(Usuario usuario) throws FormatoEmailInvalido {

    String expresionRegular = "^([a-zA-Z0-9._%-]+)@([a-zA-Z0-9.-]+).([a-zA-Z]{2,6})$";

    if(!(usuario.getEmail().matches(expresionRegular))){
      throw new FormatoEmailInvalido();
    }
  }

}

