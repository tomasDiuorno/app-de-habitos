package com.tallerwebi.dominio;

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
  public Usuario consultarUsuario(String email, String password) {//Cambie username por password para que tenga mas sentido
    Usuario usuarioEncontrado = repositorioUsuario.buscarPorEmail(email);//trae solo usuario por mail
    if(usuarioEncontrado != null){//si existe usuario verifica contraseña
      if(BCrypt.checkpw(password, usuarioEncontrado.getPassword())){//checkpw comparar clave plana con hash en BD
        return  usuarioEncontrado;//login exitoso
      }
    }
     return null;//Si no existe mail o contraseña incorrecta, devuelve null(podria lanzar una excepcion)
  }
}
