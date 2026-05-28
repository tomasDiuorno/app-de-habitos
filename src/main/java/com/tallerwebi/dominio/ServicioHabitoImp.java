package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ChecklistInsuficienteExeption;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioHabito")
@Transactional
public class ServicioHabitoImp implements ServicioHabito {

  private static final int CANTIDAD_MAXIMA_HABITOS = 4;

  private RepositorioHabito repositorioHabito;
  private RepositorioUsuarioHabito repositorioUsuarioHabito;
  private RepositorioCategoria repositorioCategoria;

  @Autowired
  public ServicioHabitoImp(
      RepositorioHabito repositorioHabito,
      RepositorioUsuarioHabito repositorioUsuarioHabito,
      RepositorioCategoria repositorioCategoria) {
    this.repositorioHabito = repositorioHabito;
    this.repositorioUsuarioHabito = repositorioUsuarioHabito;
    this.repositorioCategoria = repositorioCategoria;
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
  public void agregarHabitoParaUsuario(Habito habito, Usuario usuario)
      throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException {
    if (usuario.getUsuarioHabito().size() >= CANTIDAD_MAXIMA_HABITOS) {
      throw new LimiteHabitosAlcanzadoException();
    }

    this.repositorioCategoria.guardar(habito.getCategoria());
    this.agregarHabito(habito);

    UsuarioHabito usuarioHabito = new UsuarioHabito();
    usuarioHabito.setUsuario(usuario);
    usuarioHabito.setHabito(habito);
    usuarioHabito.setActivo(true);

    this.repositorioUsuarioHabito.guardar(usuarioHabito);
    usuario.getUsuarioHabito().add(usuarioHabito);
  }

  @Override
  public Habito buscarHabito(String titulo) {
    return this.repositorioHabito.buscarPorTitulo(titulo);
  }


@Override
public void actualizarProgresoActualHabito(Habito habito) throws ChecklistInsuficienteExeption {

    Integer cantidadDeChecklist = habito.getCantidadDeChecklist().size();
    Long checklistCompletados = 0L;

    if (cantidadDeChecklist == 0) {
        habito.setProgresoActual(0);
        throw new ChecklistInsuficienteExeption("Agrega una checklist personalizada");
    }
    
    for (ItemChecklist item : habito.getCantidadDeChecklist()) {
        if(Boolean.TRUE.equals(item.getItemCompletado())){ //item.getItemCompletado() == true. Es mejor usar esa expresion por si trae algun null
          checklistCompletados++;
        }
    }

     //long checklistCompletados = habito.getCantidadDeChecklist().stream().filter(item -> Boolean.TRUE.equals(item.getItemCompletado())).count();

    Integer porcentajeFinal = (int) ((checklistCompletados * 100) / cantidadDeChecklist);

    habito.setProgresoActual(porcentajeFinal);
}

  @Override
  public void agregarItemChecklistAlHabito(ItemChecklist item, Integer idHabito) throws ChecklistInsuficienteExeption {
    
    Habito habitoEncontrado = this.repositorioHabito.buscarHabitoPorId(idHabito);

    habitoEncontrado.getCantidadDeChecklist().add(item);
    this.actualizarProgresoActualHabito(habitoEncontrado);
    this.repositorioHabito.modificar(habitoEncontrado);

  }

  @Override
  public void eliminarItemChecklistDelHabito(ItemChecklist item, Integer idHabito) throws ChecklistInsuficienteExeption {

    Habito habitoEncontrado = this.repositorioHabito.buscarHabitoPorId(idHabito);

    habitoEncontrado.eliminarItemChecklist(item);
    this.actualizarProgresoActualHabito(habitoEncontrado);
    this.repositorioHabito.modificar(habitoEncontrado);
    
  }
  

}
