package com.tallerwebi.dominio;

public interface ServicioLogin {
  Usuario consultarUsuario(String email, String password);
}
