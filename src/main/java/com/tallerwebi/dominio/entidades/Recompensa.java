package com.tallerwebi.dominio.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Recompensa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String nombre;
  private String descripcion;
  private String urlImg;
  private Integer nivelRequerido;

  @Enumerated(EnumType.STRING)
  private RarezaEnum rareza;

  @OneToMany(mappedBy = "recompensa")
  private List<UsuarioRecompensa> usuario = new ArrayList<>();
}
