package com.tallerwebi.dominio.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Bonificacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String nombre;
  private String descripcion;
  private Integer porcentaje;
  private Integer precioMonedas;
  private Integer duracionEnDias;
  private Boolean disponible = true;

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

  public Integer getPorcentaje() {
    return porcentaje;
  }

  public void setPorcentaje(Integer porcentaje) {
    this.porcentaje = porcentaje;
  }

  public Integer getPrecioMonedas() {
    return precioMonedas;
  }

  public void setPrecioMonedas(Integer precioMonedas) {
    this.precioMonedas = precioMonedas;
  }

  public Integer getDuracionEnDias() {
    return duracionEnDias;
  }

  public void setDuracionEnDias(Integer duracionEnDias) {
    this.duracionEnDias = duracionEnDias;
  }

  public Boolean getDisponible() {
    return disponible;
  }

  public void setDisponible(Boolean disponible) {
    this.disponible = disponible;
  }
}
