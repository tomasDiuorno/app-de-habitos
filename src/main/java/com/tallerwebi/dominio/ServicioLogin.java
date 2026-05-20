package com.tallerwebi.dominio;

public interface ServicioLogin {
  Usuario consultarUsuario(String emailorusername, String password);
}
