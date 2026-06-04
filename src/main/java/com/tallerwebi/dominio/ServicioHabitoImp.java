package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ChecklistInsuficienteExeption;
import com.tallerwebi.dominio.excepcion.DescripcionChecklistInvalidaException;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.HabitoNoEncontradoException;
import com.tallerwebi.dominio.excepcion.ItemChecklistNoEncontradoException;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.presentacion.DatosRegistroHabito;
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
  private ServicioLogro servicioLogro;

  @Autowired
  public ServicioHabitoImp(
      RepositorioHabito repositorioHabito,
      RepositorioUsuarioHabito repositorioUsuarioHabito,
      RepositorioCategoria repositorioCategoria,
      ServicioLogro servicioLogro) {
    this.repositorioHabito = repositorioHabito;
    this.repositorioUsuarioHabito = repositorioUsuarioHabito;
    this.repositorioCategoria = repositorioCategoria;
    this.servicioLogro = servicioLogro;
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
    UsuarioHabito usuarioHabito = this.crearRelacion(habito, usuario);
    this.agregarHabito(habito);
    this.repositorioUsuarioHabito.guardar(usuarioHabito);
    usuario.getUsuarioHabito().add(usuarioHabito);

    if (usuario.getId() != null) {
      this.servicioLogro.verificarYAsignarLogros(usuario);
    }
  }

  private UsuarioHabito crearRelacion(Habito habito, Usuario usuario) {
    UsuarioHabito usuarioHabito = new UsuarioHabito();
    usuarioHabito.setUsuario(usuario);
    usuarioHabito.setHabito(habito);
    usuarioHabito.setActivo(true);
    return usuarioHabito;
  }

  private Habito crearHabito(DatosRegistroHabito datos, Categoria categoria) {
    Habito habito = new Habito();
    habito.setTitulo(datos.getTitulo());
    habito.setDescripcion(datos.getDescripcion());
    habito.setDuracionEstimada(datos.getDuracionEstimada());
    habito.setCategoria(categoria);
    habito.setFrecuencia(datos.getFrecuencia());
    return habito;
  }

  @Override
  public Habito buscarHabito(String titulo) {
    return this.repositorioHabito.buscarPorTitulo(titulo);
  }

  @Override
  public void actualizarProgresoActualHabito(Habito habito) throws ChecklistInsuficienteExeption {
    Integer cantidadDeChecklist = habito.getCantidadDeChecklist().size();

    if (cantidadDeChecklist == 0) {
      habito.setProgresoActual(0);
      return;
    }

    Long checklistCompletados = habito
        .getCantidadDeChecklist()
        .stream()
        .filter(item -> Boolean.TRUE.equals(item.getEstadoChecklist()))
        .count();
    // guarda cuantos checklists estan completados y los cuenta.

    Integer porcentajeFinal = (int) ((checklistCompletados * 100) / cantidadDeChecklist);
    habito.setProgresoActual(porcentajeFinal);
  }

  @Override
  public void agregarItemChecklistAlHabito(ItemChecklist item, Integer idHabito)
      throws ChecklistInsuficienteExeption, HabitoNoEncontradoException {
    Habito habitoEncontrado = this.buscarHabitoPorId(idHabito);

    habitoEncontrado.agregarItemChecklist(item);
    this.actualizarProgresoActualHabito(habitoEncontrado);
    this.repositorioHabito.modificar(habitoEncontrado);
  }

  @Override
  public void eliminarItemChecklistDelHabito(Integer idHabito, Integer idItem)
      throws HabitoNoEncontradoException, ItemChecklistNoEncontradoException, ChecklistInsuficienteExeption {

    Habito habitoEncontrado = this.buscarHabitoPorId(idHabito);

    ItemChecklist itemAEliminar = this.buscarItemChecklistEnHabito(habitoEncontrado, idItem);

    habitoEncontrado.eliminarItemChecklist(itemAEliminar);

    this.actualizarProgresoActualHabito(habitoEncontrado);

    this.repositorioHabito.modificar(habitoEncontrado);
  }

  @Override
  public Habito buscarHabitoPorId(Integer id) throws HabitoNoEncontradoException {
    Habito habito = this.repositorioHabito.buscarPorId(id);

    if (habito != null) {
      habito.getCantidadDeChecklist().size();
    } else {
      throw new HabitoNoEncontradoException();
    }

    return habito;
  }

  @Override
  public Habito obtenerHabito(DatosRegistroHabito datos) {
    Categoria categoria = repositorioCategoria.obtenerCategoriaPorId(datos.getCategoriaId());
    return this.crearHabito(datos, categoria);
  }

  @Override
  public void actualizarEstadoItemChecklist(Integer itemId, Integer habitoId)
      throws ChecklistInsuficienteExeption, HabitoNoEncontradoException, ItemChecklistNoEncontradoException {
    Habito habito = this.buscarHabitoPorId(habitoId);

    ItemChecklist item = this.buscarItemChecklistEnHabito(habito, itemId);

    item.setEstadoChecklist(!item.getEstadoChecklist());

    this.actualizarProgresoActualHabito(habito);
    this.repositorioHabito.modificar(habito);
  }

  @Override
  public void editarDescripcionItemChecklist(Integer idItem, Integer idHabito, String nuevaDescripcion)
      throws HabitoNoEncontradoException, ItemChecklistNoEncontradoException, DescripcionChecklistInvalidaException {

    if (nuevaDescripcion == null || nuevaDescripcion.trim().isEmpty()) {
      throw new DescripcionChecklistInvalidaException();
    }

    Habito habito = this.buscarHabitoPorId(idHabito);

    ItemChecklist item = this.buscarItemChecklistEnHabito(habito, idItem);

    item.setDescripcion(nuevaDescripcion.trim());

    this.repositorioHabito.modificar(habito);
  }

  private ItemChecklist buscarItemChecklistEnHabito(Habito habito, Integer idItem)
      throws ItemChecklistNoEncontradoException {
    ItemChecklist item = habito
        .getCantidadDeChecklist()
        .stream()
        .filter(i -> i.getId().equals(idItem))
        .findFirst()
        .orElseThrow(ItemChecklistNoEncontradoException::new); // recorre los items, se querda con el primer item que
                                                               // coincide. Si encontro uno que esta vacio lanza una
                                                               // excepcion
    return item;
  }

}
