package com.tallerwebi.presentacion.DTO;

import com.tallerwebi.dominio.enums.TipoHabitoEnum;
import java.time.LocalTime;

public class RegistroHabitoDTO {

  private String titulo;

  private String descripcion;

  private String frecuencia;

  private Integer categoriaId;

  private TipoHabitoEnum tipoHabito;

  private Integer objetivoNumerico;

  private String unidadObjetivo;

  private LocalTime horaLimite;

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

  public TipoHabitoEnum getTipoHabito() {
    return tipoHabito;
  }

  public void setTipoHabito(TipoHabitoEnum tipoHabito) {
    this.tipoHabito = tipoHabito;
  }

  public Integer getObjetivoNumerico() {
    return objetivoNumerico;
  }

  public void setObjetivoNumerico(Integer objetivoNumerico) {
    this.objetivoNumerico = objetivoNumerico;
  }

  public String getUnidadObjetivo() {
    return unidadObjetivo;
  }

  public void setUnidadObjetivo(String unidadObjetivo) {
    this.unidadObjetivo = unidadObjetivo;
  }

  public LocalTime getHoraLimite() {
    return horaLimite;
  }

  public void setHoraLimite(LocalTime horaLimite) {
    this.horaLimite = horaLimite;
  }
}
