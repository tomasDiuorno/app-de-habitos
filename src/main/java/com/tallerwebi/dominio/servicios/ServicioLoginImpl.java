package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.interfaz.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.ServicioLogin;
import javax.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

  private RepositorioUsuario repositorioUsuario;

  @Autowired
  public ServicioLoginImpl(RepositorioUsuario repositorioUsuario) {
    this.repositorioUsuario = repositorioUsuario;
  }

  @Override
  public Usuario consultarUsuario(String emailorusername, String password) {
    Usuario usuarioEncontrado = repositorioUsuario.buscarPorEmailOrUsername(emailorusername); //Buscamos por email o username
    if (usuarioEncontrado != null && BCrypt.checkpw(password, usuarioEncontrado.getPassword())) { //Comparamos password ingresada con hash de bd
      return usuarioEncontrado;
    }
    return null;
  }
}
