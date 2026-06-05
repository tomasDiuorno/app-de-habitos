package com.tallerwebi.dominio.entidades;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class HistorialHabito {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  private Usuario usuario;

  @ManyToOne
  private Habito habito;

  private LocalDate fechaCompletado;

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

  public LocalDate getFechaCompletado() {
    return fechaCompletado;
  }

  public void setFechaCompletado(LocalDate fechaCompletado) {
    this.fechaCompletado = fechaCompletado;
  }
}
