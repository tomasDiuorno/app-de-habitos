package com.tallerwebi.dominio.entidades;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UsuarioBonificacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  @ManyToOne
  @JoinColumn(name = "bonificacion_id")
  private Bonificacion bonificacion;

  private LocalDateTime fechaActivacion;
  private LocalDateTime fechaExpiracion;
  private Boolean activa = true;

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

  public Bonificacion getBonificacion() {
    return bonificacion;
  }

  public void setBonificacion(Bonificacion bonificacion) {
    this.bonificacion = bonificacion;
  }

  public LocalDateTime getFechaActivacion() {
    return fechaActivacion;
  }

  public void setFechaActivacion(LocalDateTime fechaActivacion) {
    this.fechaActivacion = fechaActivacion;
  }

  public LocalDateTime getFechaExpiracion() {
    return fechaExpiracion;
  }

  public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
    this.fechaExpiracion = fechaExpiracion;
  }

  public Boolean getActiva() {
    return activa;
  }

  public void setActiva(Boolean activa) {
    this.activa = activa;
  }
}
