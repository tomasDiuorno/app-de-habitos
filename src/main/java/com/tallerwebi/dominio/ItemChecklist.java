package com.tallerwebi.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ItemChecklist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String descripcion;
  private Boolean itemCompletado;

  @ManyToOne
  @JoinColumn(name = "habito_id")
  private Habito habito;

  public ItemChecklist() {
    this.itemCompletado = false;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Habito getHabito() {
    return habito;
  }

  public void setHabito(Habito habito) {
    this.habito = habito;
  }

  public Boolean getItemCompletado() {
    return itemCompletado;
  }

  public void setItemCompletado(Boolean itemCompletado) {
    this.itemCompletado = itemCompletado;
  }
}
