package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Habito;
import java.util.List;

public class DatosRegistro {

  private String name;
  private String surname;
  private String username;
  private String gender;
  private String email;
  private String password;
  private String confirmPassword;
  private List<Habito> habitosSeleccionados;

  public String getName() {
    return name;
  }
  public String getSurname() {
    return surname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public String getUsername() {
    return username;
  }

  public String getGender() {
    return gender;
  }
  public List<Habito> getHabitosSeleccionados() {
    return habitosSeleccionados;
  }
  public void setHabitosSeleccionados(List<Habito> habitosSeleccionados) {
    this.habitosSeleccionados = habitosSeleccionados;
  }
}
