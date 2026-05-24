package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioHabito")
@Transactional
public class ServicioHabitoImp implements ServicioHabito {

  private RepositorioHabito repositorioHabito;

  @Autowired
  public ServicioHabitoImp(RepositorioHabito repositorioHabito) {
    this.repositorioHabito = repositorioHabito;
  }

  @Override
  public List<Habito> obtenerHabitosIniciales() {
    return repositorioHabito.obtenerHabitosIniciales();
  }

  @Override
  public void agregarHabito(Habito habito) throws HabitoExistenteExeption {
    if (this.repositorioHabito.buscarPorTitulo(habito.getTitulo()) != null) {
      throw new HabitoExistenteExeption();
    }
    this.repositorioHabito.guardar(habito);
  }

  @Override
  public Habito buscarHabito(String titulo) {
    return this.repositorioHabito.buscarPorTitulo(titulo);
  }
}
