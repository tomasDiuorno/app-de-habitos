package com.tallerwebi.dominio;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioRecompensas")
@Transactional
public class ServicioRecompensasImp implements ServicioRecompensas {

  private RepositorioRecompensas repositorioRecompensas;

  @Autowired
  public ServicioRecompensasImp(RepositorioRecompensas repositorioRecompensas) {
    this.repositorioRecompensas = repositorioRecompensas;
  }

  @Override
  public List<Recompensa> obtenerRecompensas() {
    return this.repositorioRecompensas.obtenerTodas();
  }
}
