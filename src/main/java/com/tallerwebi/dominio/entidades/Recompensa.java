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

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getUrlImg() {
    return urlImg;
  }

  public void setUrlImg(String urlImg) {
    this.urlImg = urlImg;
  }

  public Integer getNivelRequerido() {
    return nivelRequerido;
  }

  public void setNivelRequerido(Integer nivelRequerido) {
    this.nivelRequerido = nivelRequerido;
  }

  public RarezaEnum getRareza() {
    return rareza;
  }

  public void setRareza(RarezaEnum rareza) {
    this.rareza = rareza;
  }
}
