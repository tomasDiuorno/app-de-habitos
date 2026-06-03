package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
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
    ServicioLogro servicioLogro
  ) {
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
  public Habito buscarHabitoPorId(Integer id) {
    return this.repositorioHabito.buscarPorId(id);
  }

  @Override
  public Habito obtenerHabito(DatosRegistroHabito datos) {
    Categoria categoria = repositorioCategoria.obtenerCategoriaPorId(datos.getCategoriaId());
    return this.crearHabito(datos, categoria);
  }
}
