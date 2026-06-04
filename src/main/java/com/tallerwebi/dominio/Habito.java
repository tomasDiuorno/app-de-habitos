package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Habito {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String titulo;
  private String descripcion;
  private String frecuencia;
  private Integer duracionEstimada;

  private Integer progresoActual;

  @ManyToOne
  @JoinColumn(name = "categoria_id")
  private Categoria categoria;

  @OneToMany(mappedBy = "habito")
  private List<UsuarioHabito> usuarioHabitos = new ArrayList<>();

  @OneToMany(mappedBy = "habito", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ItemChecklist> cantidadDeChecklist = new ArrayList<>();

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public Categoria getCategoria() {
    return categoria;
  }

  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }

  public List<UsuarioHabito> getUsuarioHabitos() {
    return usuarioHabitos;
  }

  public void setUsuarioHabitos(List<UsuarioHabito> usuarioHabitos) {
    this.usuarioHabitos = usuarioHabitos;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getFrecuencia() {
    return frecuencia;
  }

  public void setFrecuencia(String frecuencia) {
    this.frecuencia = frecuencia;
  }

  public Integer getDuracionEstimada() {
    return duracionEstimada;
  }

  public void setDuracionEstimada(Integer duracionEstimada) {
    this.duracionEstimada = duracionEstimada;
  }

  public List<ItemChecklist> getCantidadDeChecklist() {
    return cantidadDeChecklist;
  }

  public void setCantidadDeChecklist(List<ItemChecklist> cantidadDeChecklist) {
    this.cantidadDeChecklist = cantidadDeChecklist;
  }

  public Integer getProgresoActual() {
    return progresoActual;
  }

  public void setProgresoActual(Integer progresoActual) {
    this.progresoActual = progresoActual;
  }

  public void agregarItemChecklist(ItemChecklist item) {
    this.cantidadDeChecklist.add(item);
    item.setHabito(this);
  }

  public void eliminarItemChecklist(ItemChecklist item) {
    this.cantidadDeChecklist.remove(item);
    item.setHabito(null);
  }
}
