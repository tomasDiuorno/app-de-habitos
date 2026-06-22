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

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public Categoria getCategoria() {
    return categoria;
  }

  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }

  public List<UsuarioHabito> getUsuarioHabitos() {
    return usuarioHabitos;
  }

  public void setUsuarioHabitos(List<UsuarioHabito> usuarioHabitos) {
    this.usuarioHabitos = usuarioHabitos;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getFrecuencia() {
    return frecuencia;
  }

  public void setFrecuencia(String frecuencia) {
    this.frecuencia = frecuencia;
  }

  public TipoHabitoEnum getTipoHabito() {
    return tipoHabito;
  }

  public void setTipoHabito(TipoHabitoEnum tipoHabito) {
    this.tipoHabito = tipoHabito;
  }

  public Integer getXpBase() {
    return xpBase;
  }

  public void setXpBase(Integer xpBase) {
    this.xpBase = xpBase;
  }

  public ConfiguracionHabito getConfiguracion() {
    return configuracion;
  }

  public void setConfiguracion(ConfiguracionHabito configuracion) {
    this.configuracion = configuracion;
  }
}
