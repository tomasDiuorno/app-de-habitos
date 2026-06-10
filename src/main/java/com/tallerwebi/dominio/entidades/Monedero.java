package com.tallerwebi.dominio.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Monedero {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer saldo = 0;

  @OneToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  @OneToMany(mappedBy = "monedero", cascade = CascadeType.ALL)
  private List<Transaccion> transacciones = new ArrayList<>();

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getSaldo() {
    return saldo;
  }

  public void setSaldo(Integer saldo) {
    this.saldo = saldo;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public List<Transaccion> getTransacciones() {
    return transacciones;
  }

  public void setTransacciones(List<Transaccion> transacciones) {
    this.transacciones = transacciones;
  }
}
