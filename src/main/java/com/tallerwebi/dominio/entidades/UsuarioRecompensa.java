package com.tallerwebi.dominio.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UsuarioRecompensa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  private Usuario usuario;

  @ManyToOne
  private Recompensa recompensa;

  private Boolean utilizada = false;

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

  public Recompensa getRecompensa() {
    return recompensa;
  }

  public void setRecompensa(Recompensa recompensa) {
    this.recompensa = recompensa;
  }

  public Boolean getUtilizada() {
    return utilizada;
  }

  public void setUtilizada(Boolean utilizada) {
    this.utilizada = utilizada;
  }
}
