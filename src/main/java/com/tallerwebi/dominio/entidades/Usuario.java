package com.tallerwebi.dominio.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@SuppressWarnings("PMD.TooManyFields")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;
  private String username;
  private String email;
  private String password;
  private String rol;
  private String gender;
  private Integer nivelUsuario = 0;
  private Boolean activo = false;

  @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
  private List<UsuarioHabito> usuarioHabitos = new ArrayList<>();

  @OneToMany(mappedBy = "usuario")
  private List<UsuarioRecompensa> recompensas = new ArrayList<>();

  @OneToMany(mappedBy = "usuario")
  private List<UsuarioBonificacion> bonificaciones = new ArrayList<>();

  @OneToMany(mappedBy = "autor")
  private List<Publicacion> publicaciones = new ArrayList<>();

  @OneToMany(mappedBy = "autor")
  private List<Comentario> comentarios = new ArrayList<>();
}
