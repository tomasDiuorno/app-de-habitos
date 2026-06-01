package com.tallerwebi.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Logro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String descripcion;
    private String condicion; // ej: "PRIMER_HABITO", "CINCO_HABITOS"

    @OneToMany(mappedBy = "logro")
    private List<UsuarioLogro> usuarioLogros = new ArrayList<>();

    public Integer getId() {
         return id; }

    public void setId(Integer id) { 
        this.id = id; }

    public String getNombre() { 
        return nombre; }

    public void setNombre(String nombre) { 
        this.nombre = nombre; }

    public String getDescripcion() { 
        return descripcion; }

    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; }

    public String getCondicion() { 
        return condicion; }

    public void setCondicion(String condicion) {
         this.condicion = condicion; }

    public List<UsuarioLogro> getUsuarioLogros() {
         return usuarioLogros; }

    public void setUsuarioLogros(List<UsuarioLogro> usuarioLogros) {
        this.usuarioLogros = usuarioLogros;}
}