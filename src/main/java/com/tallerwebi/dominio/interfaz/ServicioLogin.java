package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.Usuario;

public interface ServicioLogin {
  Usuario consultarUsuario(String emailorusername, String password);
}
