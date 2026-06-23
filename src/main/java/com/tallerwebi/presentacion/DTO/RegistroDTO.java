package com.tallerwebi.presentacion.DTO;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroDTO {

  @NotBlank(message = "El nombre es obligatorio")
  private String name;

  @NotBlank(message = "El nombre de usuario es obligatorio")
  @NotBlank(message = "Nombre de usuario incompleto")
  @Size(min = 3, max = 20, message = "El usuario debe tener entre 3 y 20 caracteres")
  private String username;

  @NotBlank(message = "El correo electrónico es obligatorio")
  @Email(message = "El formato de correo electrónico no es válido")
  private String email;

  @NotBlank(message = "La contraseña es obligatoria")
  @Pattern(
    regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
    message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número"
  )
  private String password;

  @NotBlank(message = "La elección del genero es obligatorio")
  private String gender;

  @NotBlank(message = "La confirmación de contrasenia es obligatorio")
  @Pattern(
    regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
    message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número"
  )
  private String confirmPassword;

  private List<Integer> habitosSeleccionados;

  public Boolean isPasswordConfirmada() {
    return this.password != null && this.password.equals(this.confirmPassword);
  }
}
