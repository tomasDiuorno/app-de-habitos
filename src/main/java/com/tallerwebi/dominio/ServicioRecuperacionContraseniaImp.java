package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.DatosRecuperacionContrasenia;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioRecuperacionContrasenia")
@Transactional
public class ServicioRecuperacionContraseniaImp implements ServicioRecuperacionContrasenia {

  private RepositorioUsuario repositorioUsuario;

  @Autowired
  public ServicioRecuperacionContraseniaImp(RepositorioUsuario repositorioUsuario) {
    this.repositorioUsuario = repositorioUsuario;
  }

  @Override
  public void recuperarContrasenia(DatosRecuperacionContrasenia datosRecuperacionContrasenia) {
    if (!verificarSiLasContraseniasSonIguales(datosRecuperacionContrasenia)) {
      throw new RuntimeException("Las contraseñas ingresadas no coinciden");
    }

    Usuario usuarioEncontradoPorEmail = repositorioUsuario.buscarPorEmail(
      datosRecuperacionContrasenia.getEmail()
    );

    if (usuarioEncontradoPorEmail != null) {
      usuarioEncontradoPorEmail.setPassword(datosRecuperacionContrasenia.getContrasenia1());
    } else {
      throw new RuntimeException("El email ingresado no existe");
    }
  }

  private Boolean verificarSiLasContraseniasSonIguales(DatosRecuperacionContrasenia datos) {
    if (datos.getContrasenia1() == null || datos.getContrasenia2() == null) {
      return false;
    }

    return datos.getContrasenia1().equals(datos.getContrasenia2());
  }
}
