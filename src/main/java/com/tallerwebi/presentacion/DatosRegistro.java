package com.tallerwebi.presentacion;

import java.util.List;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DatosRegistro {

  @NotBlank(message = "Nombre incompleto")
  private String name;

  @NotBlank(message = "Nombre de usuario incompleto")
  @Size(min = 3, max = 20, message = "El usuario debe tener entre 3 y 20 caracteres")
  private String username;

  @NotBlank(message = "Debe elegir un genero")
  private String gender;

  @NotBlank(message = "Debe ingresar un correo")
  @Email(message = "Formato de email invalido")
  private String email;

  @NotBlank(message = "Debe completar la contraseña")
  @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
    message = "La contrasenia debe tener al menos 8 caracteres, una mayuscula, una minuscula, un numero y un simbolo"
  )
  private String password;

  @NotBlank(message = "Debe completar la confirmacion")
  private String confirmPassword;

  private List<Integer> habitosSeleccionados;

  @AssertTrue(message = "Las contrasenias no coinciden")
  public boolean isPasswordConfirmada() {
    if (this.password == null || this.confirmPassword == null) {
      return false;
    }
    return password.equals(confirmPassword);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public List<Integer> getHabitosSeleccionados() {
    return habitosSeleccionados;
  }

  public void setHabitosSeleccionados(List<Integer> habitosSeleccionados) {
    this.habitosSeleccionados = habitosSeleccionados;
  }
}
