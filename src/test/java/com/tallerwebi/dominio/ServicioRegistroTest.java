package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.excepcion.CamposObligatorios;
import com.tallerwebi.dominio.excepcion.FormatoEmailInvalido;
import com.tallerwebi.dominio.excepcion.PasswordInvalido;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosRegistro;

import ch.qos.logback.core.util.DatePatternToRegexUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioRegistroTest {

  private ServicioRegistro servicioRegistro;
  private RepositorioUsuario repositorioUsuarioMock;

  @BeforeEach
  public void init() {
    this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
    this.servicioRegistro = new ServicioRegistroImp(this.repositorioUsuarioMock);
  }

  @Test
  public void registrarUsuarioSiNoExisteDeberiaGuardarlo() throws UsuarioExistente {
    // preparacion
    DatosRegistro datos = new DatosRegistro();
    datos.setEmail("nuevo@test.com");
    when(this.repositorioUsuarioMock.buscarPorEmail(datos.getEmail())).thenReturn(null);

    // ejecucion
    this.servicioRegistro.registrar(datos);

    // validacion
    verify(this.repositorioUsuarioMock, times(1)).guardar(any(Usuario.class));
  }

  @Test
  public void registrarUsuarioSiExisteDeberiaLanzarExcepcion() {
    // preparacion
    DatosRegistro datos = new DatosRegistro();
    datos.setEmail("existe@test.com");
    when(this.repositorioUsuarioMock.buscarPorEmail(datos.getEmail())).thenReturn(new Usuario());

    // ejecucion y validacion
    assertThrows(UsuarioExistente.class, () -> this.servicioRegistro.registrar(datos));
    verify(this.repositorioUsuarioMock, times(0)).guardar(any(Usuario.class));
  }

  @Test
  public void validarUnUsuarioConCamposVaciosDeberiaLanzarUnaExcepcion() {

   DatosRegistro datos = new DatosRegistro();

    datos.setEmail("");
    datos.setPassword("");
    datos.setName("");
    datos.setSurname("");
    datos.setGender("");
    datos.setUsername("");

    //Cuando se ejecute el metodo validar con los datos dados, lanzá esta excepcion
   assertThrows(CamposObligatorios.class, () -> this.servicioRegistro.validarCamposObligatorios(datos));
   
  }

   @Test
  public void validarUnUsuarioConCamposNulosDeberiaLanzarUnaExcepcion() {

   DatosRegistro datos = new DatosRegistro();

    datos.setEmail(null);
    datos.setPassword(null);
    datos.setName(null);
    datos.setSurname(null);
    datos.setGender(null);
    datos.setUsername(null);

    //Cuando se ejecute el metodo validar con los datos dados, lanzá esta excepcion
   assertThrows(CamposObligatorios.class, () -> this.servicioRegistro.validarCamposObligatorios(datos));
   
  }

  @Test
  public void validarEmailQueNoCumpleConElFormatoValidoDeberiaLanzarUnaExcepcion(){

    DatosRegistro datos = new DatosRegistro();
     datos.setEmail("@testeando11111111com.");
    datos.setPassword("Lacontrasenia123$");

    assertThrows(FormatoEmailInvalido.class, () -> this.servicioRegistro.validarCreedenciales(datos));

  }

  @Test
  public void validarPasswordQueNoCumpleConElFormatoValidoDeberiaLanzarUnaExcepcion(){

    DatosRegistro datos = new DatosRegistro();
     datos.setEmail("test@gmail.com");
    datos.setPassword("invalid");
    
    assertThrows(PasswordInvalido.class, () -> this.servicioRegistro.validarCreedenciales(datos));

  }


}
