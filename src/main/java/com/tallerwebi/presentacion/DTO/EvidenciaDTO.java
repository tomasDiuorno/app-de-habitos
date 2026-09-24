package com.tallerwebi.presentacion.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EvidenciaDTO {

  private String texto;
  private MultipartFile imagen;
}
