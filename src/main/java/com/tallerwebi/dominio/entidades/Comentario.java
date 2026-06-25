package com.tallerwebi.dominio.entidades;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
}
