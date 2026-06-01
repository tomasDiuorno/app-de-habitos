package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioUsuarioLogro {
    void guardar(UsuarioLogro usuarioLogro);
    List<UsuarioLogro> buscarPorUsuario(Usuario usuario);
    boolean existeLogroParaUsuario(Usuario usuario, Logro logro);
}