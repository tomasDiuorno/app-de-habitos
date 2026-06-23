package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioHabito;
import com.tallerwebi.dominio.interfaz.RepositorioUsuarioHabito;
import com.tallerwebi.dominio.interfaz.ServicioUsuarioHabito;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioUsuarioHabito")
@Transactional
public class ServicioUsuarioHabitoImp implements ServicioUsuarioHabito {

  private RepositorioUsuarioHabito repositorioUsuarioHabito;

  @Autowired
  public ServicioUsuarioHabitoImp(RepositorioUsuarioHabito repositorioUsuarioHabito) {
    this.repositorioUsuarioHabito = repositorioUsuarioHabito;
  }

  @Override
  public UsuarioHabito obtenerPorUsuarioYHabito(Usuario usuario, Habito habito) {
    return this.repositorioUsuarioHabito.obtenerPorIds(usuario.getId(), habito.getId());
  }
}
