package com.tallerwebi.dominio;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class ServicioRecompensaTest {

  private ServicioRecompensas servicioRecompensas;
  private RepositorioRecompensas repositorioRecompensasMock;
  private RepositorioUsuarioRecompensa repositorioUsuarioRecompensaMock;

  @BeforeEach
  public void init() {
    this.repositorioRecompensasMock = mock(RepositorioRecompensas.class);
    this.repositorioUsuarioRecompensaMock = mock(RepositorioUsuarioRecompensa.class);
    this.servicioRecompensas =
      new ServicioRecompensasImp(
        this.repositorioRecompensasMock,
        this.repositorioUsuarioRecompensaMock
      );
  }

  @Test
  @Transactional
  @Rollback
  public void deberiaDevolverLasRecompensasDisponiblesSegunElNivelDelUsuario() {
    Usuario usuario = new Usuario();
    usuario.setNivelUsuario(10);
    Recompensa recompensa = new Recompensa();
    recompensa.setNivelRequerido(5);

    when(repositorioRecompensasMock.obtenerPorNivel(10)).thenReturn(List.of(recompensa));
    when(repositorioUsuarioRecompensaMock.existeRecompensaUsuario(recompensa, usuario))
      .thenReturn(false);

    this.servicioRecompensas.verificarRecompensas(usuario);

    verify(repositorioUsuarioRecompensaMock).guardar(any(UsuarioRecompensa.class));
  }

  @Test
  @Transactional
  @Rollback
  public void noDeberiaGuardarUnaRecompensaSiElUsuarioYaLaTiene() {
    Usuario u = new Usuario();
    u.setNivelUsuario(10);
    Recompensa r = new Recompensa();
    r.setNivelRequerido(5);

    when(repositorioRecompensasMock.obtenerPorNivel(10)).thenReturn(List.of(r));
    when(repositorioUsuarioRecompensaMock.existeRecompensaUsuario(r, u)).thenReturn(true);

    this.servicioRecompensas.verificarRecompensas(u);

    verify(repositorioUsuarioRecompensaMock, times(0)).guardar(any(UsuarioRecompensa.class));
  }
}
