package com.tallerwebi.presentacion;

import org.springframework.web.servlet.ModelAndView;

public final class CargadorLogroDesbloqueado {

  public static final String ATRIBUTO_MOSTRAR_LOGRO = "mostrarLogro";
  public static final String ATRIBUTO_TITULO_LOGRO = "tituloLogro";
  public static final String ATRIBUTO_DESCRIPCION_LOGRO = "descripcionLogro";

  private static final int CERO_HABITOS = 0;
  private static final int UN_HABITO = 1;
  private static final int DOS_HABITOS = 2;
  private static final int TRES_HABITOS = 3;
  private static final int CUATRO_HABITOS = 4;

  private CargadorLogroDesbloqueado() {}

  public static void cargarLogroDesbloqueado(
    ModelAndView modelAndView,
    int cantidadHabitosAntes,
    int cantidadHabitosDespues
  ) {
    if (cantidadHabitosAntes == CERO_HABITOS && cantidadHabitosDespues == UN_HABITO) {
      cargarDatosDelLogro(
        modelAndView,
        "Primer hábito creado",
        "Creaste tu primer hábito. Tu rutina acaba de empezar."
      );
    }

    if (cantidadHabitosAntes == DOS_HABITOS && cantidadHabitosDespues == TRES_HABITOS) {
      cargarDatosDelLogro(
        modelAndView,
        "Constante",
        "Ya tenés 3 hábitos activos. Estás construyendo una rutina."
      );
    }

    if (cantidadHabitosAntes == TRES_HABITOS && cantidadHabitosDespues == CUATRO_HABITOS) {
      cargarDatosDelLogro(modelAndView, "Experto", "Llegaste al máximo de 4 hábitos activos.");
    }
  }

  private static void cargarDatosDelLogro(
    ModelAndView modelAndView,
    String tituloLogro,
    String descripcionLogro
  ) {
    modelAndView.addObject(ATRIBUTO_MOSTRAR_LOGRO, true);
    modelAndView.addObject(ATRIBUTO_TITULO_LOGRO, tituloLogro);
    modelAndView.addObject(ATRIBUTO_DESCRIPCION_LOGRO, descripcionLogro);
  }
}
