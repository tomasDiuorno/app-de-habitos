package com.tallerwebi.dominio.entidades;

import com.tallerwebi.dominio.enums.TipoHabitoEnum;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Habito {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String titulo;
  private String descripcion;
  private String frecuencia;
  private Integer xpBase;

  @Enumerated(EnumType.STRING)
  private TipoHabitoEnum tipoHabito;

  @OneToOne(cascade = CascadeType.ALL)
  private ConfiguracionHabito configuracion;

  @ManyToOne
  @JoinColumn(name = "categoria_id")
  private Categoria categoria;

  @OneToMany(mappedBy = "habito")
  private List<UsuarioHabito> usuarioHabitos = new ArrayList<>();
}
