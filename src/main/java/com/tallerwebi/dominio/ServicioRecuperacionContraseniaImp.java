package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ContraseniasNoCoincidenException;
import com.tallerwebi.dominio.excepcion.EmailInexistenteException;
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
  public void recuperarContrasenia(DatosRecuperacionContrasenia datosRecuperacionContrasenia)
    throws ContraseniasNoCoincidenException, EmailInexistenteException {
    if (!verificarSiLasContraseniasSonIguales(datosRecuperacionContrasenia)) {
      throw new ContraseniasNoCoincidenException("Error, las contrasenias no coinciden");
    }

    Usuario usuarioEncontradoPorEmail = repositorioUsuario.buscarPorEmailOrUsername(
      datosRecuperacionContrasenia.getEmail()
    );

    if (usuarioEncontradoPorEmail != null) {
      usuarioEncontradoPorEmail.setPassword(datosRecuperacionContrasenia.getContrasenia1());
    } else {
      throw new EmailInexistenteException("El email ingresado no existe");
    }
  }

  private Boolean verificarSiLasContraseniasSonIguales(DatosRecuperacionContrasenia datos) {
    if (datos.getContrasenia1() == null || datos.getContrasenia2() == null) {
      return false;
    }

    return datos.getContrasenia1().equals(datos.getContrasenia2());
  }
}
