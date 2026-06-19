package com.tallerwebi.dominio.entidades;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class RegistroHabito {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  private Usuario usuario;

  @ManyToOne
  private Habito habito;

  private LocalDate fecha;

  private Boolean completado;

  private String evidenciaTexto;

  private Integer evidenciaNumerica;

  private Integer xpGanada;
}
