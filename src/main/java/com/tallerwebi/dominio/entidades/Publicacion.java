package com.tallerwebi.dominio.entidades;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Publicacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String titulo;
  private String contenido;
  private LocalDateTime fechaCreacion;

  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Usuario autor;

  @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL)
  private List<Comentario> comentarios = new ArrayList<>();

  public void agregarComentario(Comentario comentario) {
    comentarios.add(comentario);
    comentario.setPublicacion(this);
  }
}
