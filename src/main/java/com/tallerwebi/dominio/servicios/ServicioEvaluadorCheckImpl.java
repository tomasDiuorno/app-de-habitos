package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.enums.TipoHabitoEnum;
import com.tallerwebi.dominio.interfaz.ServicioEvaluadorTipoHabito;
import com.tallerwebi.dominio.interfaz.ServicioIA;
import com.tallerwebi.presentacion.DTO.EvidenciaDTO;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;
import java.io.IOException;
import java.util.Base64;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("evaluadorCheck")
public class ServicioEvaluadorCheckImpl implements ServicioEvaluadorTipoHabito {

  private final ServicioIA servicioIA;

  @Autowired
  public ServicioEvaluadorCheckImpl(ServicioIA servicioIA) {
    this.servicioIA = servicioIA;
  }

  @Override
  public TipoHabitoEnum getTipoHabito() {
    return TipoHabitoEnum.CHECK;
  }

  @Override
  public ResultadoEvaluacionDTO evaluar(Habito habito, EvidenciaDTO evidencia) {
    MultipartFile imagen = evidencia.getImagen();
    if (imagen == null || imagen.isEmpty()) {
      return new ResultadoEvaluacionDTO(false, "No se proporcionó una imagen para evaluar.");
    }
    String imagenBase64;
    try {
      imagenBase64 = Base64.getEncoder().encodeToString(imagen.getBytes());
      String respuesta = servicioIA.evaluarImagen(
        imagenBase64,
        habito.getTitulo(),
        habito.getDescripcion(),
        imagen.getContentType()
      );
      Boolean cumplio =
        respuesta != null && respuesta.trim().toUpperCase(Locale.ROOT).startsWith("SI");
      return new ResultadoEvaluacionDTO(cumplio, respuesta);
    } catch (IOException e) {
      return new ResultadoEvaluacionDTO(false, "Error al procesar la imagen.");
    }
  }
}
