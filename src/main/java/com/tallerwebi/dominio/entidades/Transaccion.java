package com.tallerwebi.dominio.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer monto;
    private String tipo;       //(logro o recompensa)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "monedero_id")
    private Monedero monedero;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getMonto() { return monto; }
    public void setMonto(Integer monto) { this.monto = monto; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Monedero getMonedero() { return monedero; }
    public void setMonedero(Monedero monedero) { this.monedero = monedero; }
}