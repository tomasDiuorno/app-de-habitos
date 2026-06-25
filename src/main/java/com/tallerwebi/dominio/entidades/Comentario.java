package com.tallerwebi.dominio.entidades;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
<<<<<<< HEAD
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
=======

>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
@Entity
public class Comentario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String contenido;
  private LocalDateTime fechaCreacion;

  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Usuario autor;

  @ManyToOne
  @JoinColumn(name = "publicacion_id")
  private Publicacion publicacion;
<<<<<<< HEAD
=======

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getContenido() {
    return contenido;
  }

  public void setContenido(String contenido) {
    this.contenido = contenido;
  }

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public Usuario getAutor() {
    return autor;
  }

  public void setAutor(Usuario autor) {
    this.autor = autor;
  }

  public Publicacion getPublicacion() {
    return publicacion;
  }

  public void setPublicacion(Publicacion publicacion) {
    this.publicacion = publicacion;
  }
>>>>>>> f20d2fa78752f27cde6ba7ccf2dbc3e735c6e51a
}
