package com.tallerwebi.dominio.entidades;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class RegistroHabito {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  private Usuario usuario;

  @ManyToOne
  private Habito habito;

  private LocalDate fecha;

  private Boolean completado;

  private String evidencia;

  private Integer xpGanada;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Habito getHabito() {
    return habito;
  }

  public void setHabito(Habito habito) {
    this.habito = habito;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public void setFecha(LocalDate fecha) {
    this.fecha = fecha;
  }

  public Boolean getCompletado() {
    return completado;
  }

  public void setCompletado(Boolean completado) {
    this.completado = completado;
  }

  public String getEvidencia() {
    return evidencia;
  }

  public void setEvidencia(String evidencia) {
    this.evidencia = evidencia;
  }

  public Integer getXpGanada() {
    return xpGanada;
  }

  public void setXpGanada(Integer xpGanada) {
    this.xpGanada = xpGanada;
  }
}
