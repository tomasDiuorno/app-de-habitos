package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tallerwebi.dominio.ServicioHabito;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class ControladorHabitosTest {

  private Usuario usuarioMock;
  private ServicioHabito servicioHabitoMock;

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    usuarioMock = mock(Usuario.class);
    servicioHabitoMock = mock(ServicioHabito.class);
    when(usuarioMock.getEmail()).thenReturn("test@mail.com");
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void deberiaRetornarLaPaginaHabitosCuandoNavegoAHabitos() throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/habitos").sessionAttr("usuario", usuarioMock))
        .andExpect(status().isOk())
        .andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assert modelAndView != null;
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("habitos"));
  }

  @Test
  public void deberiaRetornarLaPaginaCrearHabitoCuandoQuieroCrearUnHaibto() throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/crear-habito").sessionAttr("usuario", usuarioMock))
        .andExpect(status().isOk())
        .andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assert modelAndView != null;
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("crear-habito"));
  }

  @Test
  public void deberiaRetornarAlLoginCuandoNoHayUnUsuarioLogueado() throws Exception {
    MvcResult result =
      this.mockMvc.perform(get("/habitos")).andExpect(status().is3xxRedirection()).andReturn();

    ModelAndView modelAndView = result.getModelAndView();
    assert modelAndView != null;
    assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
  }
}
