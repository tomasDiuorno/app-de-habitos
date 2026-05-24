package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CamposObligatorios;
import com.tallerwebi.dominio.excepcion.ContraseniasNoCoincidenException;
import com.tallerwebi.dominio.excepcion.FormatoEmailInvalido;
import com.tallerwebi.dominio.excepcion.PasswordInvalido;
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
    String hash = BCrypt.hashpw(datos.getPassword(), BCrypt.gensalt()); // Genera Hash a partir de la contraseña
    usuario.setPassword(hash); // Reemplaza contraseña por hash seguro

    repositorioUsuario.guardar(usuario); // Guardamos usuario. la bd recibe hash.
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
  public void validarCamposObligatorios(DatosRegistro datos) throws CamposObligatorios {
    if (faltanDatosPersonales(datos) || faltanCredenciales(datos)) {
      throw new CamposObligatorios("Los campos obligatorios no pueden estar vacios");
    }
  }

  private boolean faltanDatosPersonales(DatosRegistro datos) {
    return (
      (datos.getName() == null || datos.getName().isEmpty()) ||
      (datos.getSurname() == null || datos.getSurname().isEmpty()) ||
      (datos.getGender() == null || datos.getGender().isEmpty()) ||
      (datos.getHabitosSeleccionados() == null || datos.getHabitosSeleccionados().isEmpty())
    );
  }

  private boolean faltanCredenciales(DatosRegistro datos) {
    return (
      (datos.getEmail() == null || datos.getEmail().isEmpty()) ||
      (datos.getPassword() == null || datos.getPassword().isEmpty()) ||
      (datos.getConfirmPassword() == null || datos.getConfirmPassword().isEmpty()) ||
        (datos.getUsername() == null || datos.getUsername().isEmpty())
    );
  }

  @Override
  public void validarCreedenciales(DatosRegistro datos)
    throws FormatoEmailInvalido, PasswordInvalido {
    String expresionRegularEmail = "^([a-zA-Z0-9._%-]+)@([a-zA-Z0-9.-]+).([a-zA-Z]{2,6})$";

    if (!datos.getEmail().matches(expresionRegularEmail)) {
      throw new FormatoEmailInvalido("El formato de correo electrónico no es válido");
    }
    String expresionRegularPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
    if (!datos.getPassword().matches(expresionRegularPassword)) {
      throw new PasswordInvalido(
        "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número"
      );
    }
  }

  @Override
  public void registrarHabitos(DatosRegistro datos) {}

  @Override
  public void registrar(Usuario usuario) throws UsuarioExistente {
    throw new UnsupportedOperationException("Unimplemented method 'registrar'");
  }

@Override
 public void validarSiLasContraseniasSonIguales(DatosRegistro datos) throws ContraseniasNoCoincidenException {
    if (!datos.getPassword().equals(datos.getConfirmPassword())) {
    throw new ContraseniasNoCoincidenException("Las contraseñas deben ser iguales");
  
}
}
}
