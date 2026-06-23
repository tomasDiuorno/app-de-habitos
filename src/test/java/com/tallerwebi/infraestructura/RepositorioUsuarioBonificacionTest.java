package com.tallerwebi.infraestructura;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioBonificacion;
import com.tallerwebi.dominio.interfaz.RepositorioUsuarioBonificacion;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateInfraestructuraTestConfig.class })
public class RepositorioUsuarioBonificacionTest {

    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioUsuarioBonificacion repositorioUsuarioBonificacion;

    @BeforeEach
    public void init() {
        repositorioUsuarioBonificacion = new RepositorioUsuarioBonificacionImp(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaGuardarUnaBonificacionParaUnUsuario() {
        Usuario usuario = dadoQueTengoUnUsuario();
        dadoQueExisteElUsuario(usuario);
        UsuarioBonificacion bonificacion = dadoQueTengoUnaBonificacion(usuario);
        cuandoGuardoLaBonificacion(bonificacion);
        UsuarioBonificacion obtenida = buscarBonificacionPorUsuario(usuario.getId());

        assertThat(obtenida.getActiva(), is(true));
        assertThat(obtenida.getUsuario(), is(usuario));
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaEncontrarBonificacionActivaDeUsuario() {
        Usuario usuario = dadoQueTengoUnUsuario();
        dadoQueExisteElUsuario(usuario);
        UsuarioBonificacion bonificacion = dadoQueTengoUnaBonificacion(usuario);
        dadoQueExisteLaBonificacion(bonificacion);
        UsuarioBonificacion obtenida = repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(usuario.getId());

        assertThat(obtenida.getUsuario(), is(usuario));
    }

    @Test
    @Transactional
    @Rollback
    public void noDeberiaEncontrarBonificacionCuandoEstaVencida() {
        Usuario usuario = dadoQueTengoUnUsuario();
        dadoQueExisteElUsuario(usuario);
        UsuarioBonificacion bonificacion = new UsuarioBonificacion();

        bonificacion.setUsuario(usuario);
        bonificacion.setActiva(true);
        bonificacion.setFechaExpiracion(LocalDateTime.now().minusDays(1));
        dadoQueExisteLaBonificacion(bonificacion);

        UsuarioBonificacion obtenida = repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(usuario.getId());

        assertThat(obtenida,is(nullValue()));
    }

    @Test
    @Transactional
    @Rollback
    public void deberiaModificarUnaBonificacionExistente() {
        Usuario usuario = dadoQueTengoUnUsuario();
        dadoQueExisteElUsuario(usuario);
        UsuarioBonificacion bonificacion = dadoQueTengoUnaBonificacion(usuario);
        dadoQueExisteLaBonificacion(bonificacion);

        bonificacion.setActiva(false);
        repositorioUsuarioBonificacion.modificar(bonificacion);

        UsuarioBonificacion obtenida = obtenerBonificacionPorId(bonificacion.getId());

        assertThat(obtenida.getActiva(), is(false));
    }

    private Usuario dadoQueTengoUnUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setUsername("usuarioTest");
        return usuario;
    }

    private void dadoQueExisteElUsuario(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
    }

    private UsuarioBonificacion dadoQueTengoUnaBonificacion(Usuario usuario) {
        UsuarioBonificacion bonificacion = new UsuarioBonificacion();
        bonificacion.setUsuario(usuario);
        bonificacion.setActiva(true);
        bonificacion.setFechaExpiracion(LocalDateTime.now().plusDays(10));
        return bonificacion;
    }

    private void dadoQueExisteLaBonificacion(UsuarioBonificacion bonificacion) {
        sessionFactory.getCurrentSession().save(bonificacion);
    }

    private void cuandoGuardoLaBonificacion(UsuarioBonificacion bonificacion) {
        repositorioUsuarioBonificacion.guardar(bonificacion);
    }

    private UsuarioBonificacion buscarBonificacionPorUsuario(Integer usuarioId) {
        return repositorioUsuarioBonificacion.buscarActivaPorUsuarioId(usuarioId);
    }

    private UsuarioBonificacion obtenerBonificacionPorId(Integer id) {
        String hql = "FROM UsuarioBonificacion ub WHERE ub.id = :id";

        return sessionFactory.getCurrentSession().createQuery(hql,UsuarioBonificacion.class)
                .setParameter("id",id)
                .getSingleResult();
    }

}
