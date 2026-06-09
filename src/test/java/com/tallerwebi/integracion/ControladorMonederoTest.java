package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tallerwebi.dominio.entidades.Usuario;
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
public class ControladorMonederoTest {

    private Usuario usuario;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        usuario =  new Usuario();
        usuario.setEmail("test@mail.com");
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void deberiaRetornarLaVistaTransaccionesCuandoHayUsuarioEnSesion() throws Exception {
        MvcResult result =
            this.mockMvc.perform(get("/transacciones").sessionAttr("usuario", usuario))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();

        assert modelAndView != null;
        assertThat(modelAndView.getViewName(), is(equalToIgnoringCase("transacciones")));
    }

    @Test
    public void deberiaRedirigirALoginSiNoHayUsuarioEnSesion() throws Exception {
        MvcResult result =
            this.mockMvc.perform(get("/transacciones"))
                .andExpect(status().isFound())
                .andExpect(status().is3xxRedirection())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();

        assert modelAndView != null;
        assertThat(modelAndView.getViewName(), is(equalToIgnoringCase("redirect:/login")));
    }

    @Test
    public void deberiaExponerElSaldoEnElModeloCuandoHayUsuarioEnSesion() throws Exception {
        MvcResult result =
            this.mockMvc.perform(get("/transacciones").sessionAttr("usuario", usuario))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();

        assert modelAndView != null;
        assertThat(modelAndView.getModel().get("saldo"), is(notNullValue()));
    }

    @Test
    public void deberiaExponerLasTransaccionesEnElModeloCuandoHayUsuarioEnSesion() throws Exception {
        MvcResult result =
            this.mockMvc.perform(get("/transacciones").sessionAttr("usuario", usuario))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();

        assert modelAndView != null;
        assertThat(modelAndView.getModel().get("transacciones"), is(notNullValue()));
    }
}