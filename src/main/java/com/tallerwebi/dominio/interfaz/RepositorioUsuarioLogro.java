package com.tallerwebi.dominio.interfaz;

import java.util.List;

import com.tallerwebi.dominio.entidades.Logro;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioLogro;

public interface RepositorioUsuarioLogro {
  void guardar(UsuarioLogro usuarioLogro);
  List<UsuarioLogro> buscarPorUsuario(Usuario usuario);
  boolean existeLogroParaUsuario(Usuario usuario, Logro logro);
}
