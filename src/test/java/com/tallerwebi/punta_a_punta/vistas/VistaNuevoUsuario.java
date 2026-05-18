package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaNuevoUsuario extends VistaWeb {

  public VistaNuevoUsuario(Page page) {
    super(page);
  }

  public void escribirNombre(String nombre) {
    this.escribirEnElElemento("#name", nombre);
  }

  public void escribirApellido(String apellido) {
    this.escribirEnElElemento("#surname", apellido);
  }

  public void escribirUsername(String username) {
    this.escribirEnElElemento("#username", username);
  }

  public void escribirGenero(String genero) {
    this.escribirEnElElemento("#gender", genero);
  }

  public void escribirEMAIL(String email) {
    this.escribirEnElElemento("#email", email);
  }

  public void escribirClave(String clave) {
    this.escribirEnElElemento("#password", clave);
  }

  public void escribirConfirmarClave(String confirmarClave) {
    this.escribirEnElElemento("#confirmPass", confirmarClave);
  }

  public void darClickEnRegistrarme() {
    this.darClickEnElElemento("#btn-registrarme");
  }

  public String obtenerMensajeDeError() {
    return this.obtenerTextoDelElemento("p.alert.alert-danger");
  }
}
