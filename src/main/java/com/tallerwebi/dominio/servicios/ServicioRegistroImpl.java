package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioHabito;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.interfaz.RepositorioHabito;
import com.tallerwebi.dominio.interfaz.RepositorioUsuario;
import com.tallerwebi.dominio.interfaz.RepositorioUsuarioHabito;
import com.tallerwebi.dominio.interfaz.ServicioRegistro;
import com.tallerwebi.presentacion.DTO.RegistroDTO;

import java.util.List;
import javax.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioRegistro")
@Transactional
public class ServicioRegistroImpl implements ServicioRegistro {

  private RepositorioUsuario repositorioUsuario;
  private RepositorioHabito repositorioHabito;
  private RepositorioUsuarioHabito repositorioUsuarioHabito;

  @Autowired
  public ServicioRegistroImpl(
    RepositorioUsuario repositorioUsuario,
    RepositorioHabito repositorioHabito,
    RepositorioUsuarioHabito repositorioUsuarioHabito
  ) {
    this.repositorioUsuario = repositorioUsuario;
    this.repositorioHabito = repositorioHabito;
    this.repositorioUsuarioHabito = repositorioUsuarioHabito;
  }

  @Override
  public void registrar(RegistroDTO datos) throws UsuarioExistente {
    Usuario usuarioEncontrado = repositorioUsuario.buscarPorEmailOrUsername(datos.getEmail());
    if (usuarioEncontrado != null) {
      throw new UsuarioExistente();
    }
    Usuario usuario = crearUsuario(datos);
    String hash = BCrypt.hashpw(datos.getPassword(), BCrypt.gensalt()); //Genera Hash a partir de la contraseña
    usuario.setPassword(hash); //Reemplaza contraseña por hash seguro
    repositorioUsuario.guardar(usuario); //Guardamos usuario. la bd recibe hash.

    if (datos.getHabitosSeleccionados() == null || datos.getHabitosSeleccionados().isEmpty()) {
      return;
    }

    List<Habito> habitos = repositorioHabito.buscarPorIds(datos.getHabitosSeleccionados());
    for (Habito habito : habitos) {
      UsuarioHabito usuarioHabito = new UsuarioHabito();
      usuarioHabito.setUsuario(usuario);
      usuarioHabito.setHabito(habito);
      usuarioHabito.setActivo(true);

      repositorioUsuarioHabito.guardar(usuarioHabito);
    }
  }

  private Usuario crearUsuario(RegistroDTO datos) {
    Usuario usuario = new Usuario();
    usuario.setName(datos.getName());
    usuario.setEmail(datos.getEmail());
    usuario.setGender(datos.getGender());
    usuario.setUsername(datos.getUsername());
    return usuario;
  }
}
