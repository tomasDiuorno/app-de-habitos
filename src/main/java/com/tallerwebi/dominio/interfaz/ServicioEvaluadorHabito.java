package com.tallerwebi.dominio.interfaz;

import com.tallerwebi.dominio.entidades.UsuarioHabito;
import com.tallerwebi.presentacion.DTO.EvidenciaDTO;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;

public interface ServicioEvaluadorHabito {
  ResultadoEvaluacionDTO completarHabito(UsuarioHabito usuarioHabito, EvidenciaDTO evidencia);
}
