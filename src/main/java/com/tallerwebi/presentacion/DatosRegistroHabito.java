package com.tallerwebi.presentacion;

public class DatosRegistroHabito {

  private String titulo;
  private String descripcion;
  private Integer categoriaId;
  private String frecuencia;
  private Integer duracionEstimada;

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Integer getCategoriaId() {
    return categoriaId;
  }

  public void setCategoriaId(Integer categoriaId) {
    this.categoriaId = categoriaId;
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
}
