package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Logro;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioLogro;
import java.util.List;

public interface RepositorioUsuarioLogro {
  void guardar(UsuarioLogro usuarioLogro);
  List<UsuarioLogro> buscarPorUsuario(Usuario usuario);
  boolean existeLogroParaUsuario(Usuario usuario, Logro logro);
}
