package com.tallerwebi.dominio.interfaz;

public interface ServicioIA {
  String preguntar(String mensaje);
  String evaluarImagen(String imagen, String titulo, String descripcion, String mimeType);
}
