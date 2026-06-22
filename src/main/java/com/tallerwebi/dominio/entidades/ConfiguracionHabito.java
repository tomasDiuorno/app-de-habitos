package com.tallerwebi.dominio.entidades;

import java.time.LocalTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ConfiguracionHabito {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer objetivoNumero;
  private String unidad;
  private LocalTime horaLimite;
  private Integer duracionObjetivo;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getObjetivoNumero() {
    return objetivoNumero;
  }

  public void setObjetivoNumero(Integer objetivoNumero) {
    this.objetivoNumero = objetivoNumero;
  }

  public String getUnidad() {
    return unidad;
  }

  public void setUnidad(String unidad) {
    this.unidad = unidad;
  }

  public LocalTime getHoraLimite() {
    return horaLimite;
  }

  public void setHoraLimite(LocalTime horaLimite) {
    this.horaLimite = horaLimite;
  }

  public Integer getDuracionObjetivo() {
    return duracionObjetivo;
  }

  public void setDuracionObjetivo(Integer duracionObjetivo) {
    this.duracionObjetivo = duracionObjetivo;
  }
}
