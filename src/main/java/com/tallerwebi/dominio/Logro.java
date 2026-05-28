package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Logro {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String nombre;

  private String descripcion;

  private String icono;

  @OneToMany(mappedBy = "logro")
  private List<UsuarioLogro> usuarioLogros = new ArrayList<>();

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

  public String getIcono() {
    return icono;
  }

  public void setIcono(String icono) {
    this.icono = icono;
  }

  public List<UsuarioLogro> getUsuarioLogros() {
    return usuarioLogros;
  }

  public void setUsuarioLogros(List<UsuarioLogro> usuarioLogros) {
    this.usuarioLogros = usuarioLogros;
  }
}