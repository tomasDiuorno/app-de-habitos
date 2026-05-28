package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class UsuarioLogro {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  @ManyToOne
  @JoinColumn(name = "logro_id")
  private Logro logro;

  private Boolean desbloqueado = false;

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

  public Logro getLogro() {
    return logro;
  }

  public void setLogro(Logro logro) {
    this.logro = logro;
  }

  public Boolean getDesbloqueado() {
    return desbloqueado;
  }

  public void setDesbloqueado(Boolean desbloqueado) {
    this.desbloqueado = desbloqueado;
  }
}