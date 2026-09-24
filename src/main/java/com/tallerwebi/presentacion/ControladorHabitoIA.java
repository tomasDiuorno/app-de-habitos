package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.enums.TipoHabitoEnum;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.dominio.interfaz.ServicioCategoria;
import com.tallerwebi.dominio.interfaz.ServicioHabito;
import com.tallerwebi.dominio.interfaz.ServicioLogro;
import com.tallerwebi.dominio.servicios.ServicioHabitoIA;
import com.tallerwebi.presentacion.DTO.HabitoObjetivoDTO;
import com.tallerwebi.presentacion.DTO.HabitoSugeridoDTO;
import com.tallerwebi.presentacion.DTO.RegistroHabitoDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/habitos")
public class ControladorHabitoIA {

  private static final String ATRIBUTO_USUARIO = "usuario";
  private static final String CLAVE_MENSAJE = "mensaje";
  private static final String CLAVE_ERROR = "error";
  private static final String CLAVE_MOSTRAR_LOGRO = "mostrarLogro";
  private static final String CLAVE_TITULO_LOGRO = "tituloLogro";
  private static final String CLAVE_DESCRIPCION_LOGRO = "descripcionLogro";

  private static final int CERO_HABITOS = 0;
  private static final int UN_HABITO = 1;
  private static final int DOS_HABITOS = 2;
  private static final int TRES_HABITOS = 3;
  private static final int CUATRO_HABITOS = 4;

  private final ServicioHabitoIA servicioHabitoIA;
  private final ServicioHabito servicioHabito;
  private final ServicioCategoria servicioCategoria;
  private final ServicioLogro servicioLogro;

  @Autowired
  public ControladorHabitoIA(
    ServicioHabitoIA servicioHabitoIA,
    ServicioHabito servicioHabito,
    ServicioCategoria servicioCategoria,
    ServicioLogro servicioLogro
  ) {
    this.servicioHabitoIA = servicioHabitoIA;
    this.servicioHabito = servicioHabito;
    this.servicioCategoria = servicioCategoria;
    this.servicioLogro = servicioLogro;
  }

  @PostMapping("/recomendar")
  public ResponseEntity<?> preguntar(@RequestBody HabitoObjetivoDTO objetivo) {
    HabitoSugeridoDTO sugerencia;
    try {
      sugerencia = servicioHabitoIA.recomendar(objetivo.getObjetivo());
      return ResponseEntity.ok(sugerencia);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(e.getMessage());
    }
  }

  @PostMapping("/crear-desde-sugerencia")
  public ResponseEntity<?> crearDesdeSugerencia(
    @RequestBody HabitoSugeridoDTO sugerido,
    HttpServletRequest request
  ) {
    Usuario usuario = this.obtenerUsuario(request);
    if (usuario == null) {
      return ResponseEntity.status(401).body(mapaConError("Debe iniciar sesión"));
    }

    RegistroHabitoDTO datos = this.mapearASugeridoARegistro(sugerido);
    int cantidadHabitosAntes = usuario.getUsuarioHabitos().size();

    try {
      Habito habito = this.servicioHabito.obtenerHabito(datos);
      this.servicioHabito.agregarHabitoParaUsuario(habito, usuario);

      int cantidadHabitosDespues = cantidadHabitosAntes + 1;
      this.servicioLogro.verificarYAsignarLogros(usuario, cantidadHabitosDespues);

      Map<String, Object> cuerpo = new HashMap<>();
      cuerpo.put(CLAVE_MENSAJE, "¡Hábito creado con éxito!");
      this.cargarLogroDesbloqueado(cuerpo, cantidadHabitosAntes, cantidadHabitosDespues);

      return ResponseEntity.ok(cuerpo);
    } catch (HabitoExistenteExeption excepcion) {
      return ResponseEntity.status(409).body(mapaConError("Ya existe un hábito con ese nombre"));
    } catch (LimiteHabitosAlcanzadoException excepcion) {
      return ResponseEntity
        .status(400)
        .body(mapaConError("No podés tener más de 4 hábitos activos"));
    } catch (Exception excepcion) {
      return ResponseEntity.status(500).body(mapaConError("Hubo un problema al crear el hábito"));
    }
  }

  private void cargarLogroDesbloqueado(
    Map<String, Object> cuerpo,
    int cantidadHabitosAntes,
    int cantidadHabitosDespues
  ) {
    cuerpo.put(CLAVE_MOSTRAR_LOGRO, false);

    if (cantidadHabitosAntes == CERO_HABITOS && cantidadHabitosDespues == UN_HABITO) {
      this.cargarDatosDelLogro(
          cuerpo,
          "Primer hábito creado",
          "Creaste tu primer hábito. Tu rutina acaba de empezar."
        );
    }

    if (cantidadHabitosAntes == DOS_HABITOS && cantidadHabitosDespues == TRES_HABITOS) {
      this.cargarDatosDelLogro(
          cuerpo,
          "Constante",
          "Ya tenés 3 hábitos activos. Estás construyendo una rutina."
        );
    }

    if (cantidadHabitosAntes == TRES_HABITOS && cantidadHabitosDespues == CUATRO_HABITOS) {
      this.cargarDatosDelLogro(cuerpo, "Experto", "Llegaste al máximo de 4 hábitos activos.");
    }
  }

  private void cargarDatosDelLogro(
    Map<String, Object> cuerpo,
    String tituloLogro,
    String descripcionLogro
  ) {
    cuerpo.put(CLAVE_MOSTRAR_LOGRO, true);
    cuerpo.put(CLAVE_TITULO_LOGRO, tituloLogro);
    cuerpo.put(CLAVE_DESCRIPCION_LOGRO, descripcionLogro);
  }

  private RegistroHabitoDTO mapearASugeridoARegistro(HabitoSugeridoDTO sugerido) {
    RegistroHabitoDTO datos = new RegistroHabitoDTO();
    datos.setTitulo(sugerido.getNombre());
    datos.setDescripcion(sugerido.getDescripcion());
    datos.setFrecuencia(sugerido.getFrecuencia());
    datos.setCategoriaId(this.buscarCategoriaId(sugerido.getCategoria()));
    datos.setTipoHabito(TipoHabitoEnum.CHECK);
    datos.setCompartirEnForo(false);
    return datos;
  }

  private Integer buscarCategoriaId(String nombreCategoriaSugerida) {
    List<Categoria> categorias = this.servicioCategoria.obtenerCategorias();

    if (categorias == null || categorias.isEmpty()) {
      return null;
    }

    if (nombreCategoriaSugerida != null) {
      for (Categoria categoria : categorias) {
        if (categoria.getNombre().equalsIgnoreCase(nombreCategoriaSugerida.trim())) {
          return categoria.getId();
        }
      }
    }

    return categorias.get(0).getId();
  }

  private Usuario obtenerUsuario(HttpServletRequest request) {
    return (Usuario) request.getSession().getAttribute(ATRIBUTO_USUARIO);
  }

  private Map<String, String> mapaConError(String mensaje) {
    Map<String, String> cuerpo = new HashMap<>();
    cuerpo.put(CLAVE_ERROR, mensaje);
    return cuerpo;
  }
}
