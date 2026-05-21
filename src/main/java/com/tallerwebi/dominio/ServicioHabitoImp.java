package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service("servicioHabitos")
@Transactional
public class ServicioHabitoImp implements ServicioHabitos {

  @Override
  public List<Habito> obtenerHabitosIniciales() {
    List<Habito> habitos = new ArrayList<>();
    habitos.add(crearHabito("Meditar", "Bienestar"));
    habitos.add(crearHabito("Leer un libro", "Cultura"));
    habitos.add(crearHabito("Hacer ejercicio", "Salud"));
    return habitos;
  }

  private Habito crearHabito(String titulo, String categoria) {
    Habito habito = new Habito();
    habito.setTitulo(titulo);
    habito.setCategoria(categoria);
    return habito;
  }
}
