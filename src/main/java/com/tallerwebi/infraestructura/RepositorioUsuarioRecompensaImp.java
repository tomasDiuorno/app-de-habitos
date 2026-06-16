package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Recompensa;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioRecompensa;
import com.tallerwebi.dominio.interfaz.RepositorioUsuarioRecompensa;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioUsuarioRecompensa")
@Transactional
public class RepositorioUsuarioRecompensaImp implements RepositorioUsuarioRecompensa {

  private SessionFactory sessionFactory;

  @Autowired
  public RepositorioUsuarioRecompensaImp(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void guardar(UsuarioRecompensa usuarioRecompensa) {
    this.sessionFactory.getCurrentSession().save(usuarioRecompensa);
  }

  @Override
  public List<UsuarioRecompensa> obtenerPorUsuario(Integer idUsuario) {
    String query = "FROM UsuarioRecompensa WHERE usuario.id = :idUsuario AND utilizada = false";
    return this.sessionFactory.getCurrentSession()
      .createQuery(query, UsuarioRecompensa.class)
      .setParameter("idUsuario", idUsuario)
      .getResultList();
  }

  @Override
  public Boolean existeRecompensaUsuario(Recompensa recompensa, Usuario usuario) {
    String query =
      "FROM UsuarioRecompensa WHERE usuario.id = :idUsuario AND recompensa.id = :idRecompensa";
    List<UsuarioRecompensa> resultado =
      this.sessionFactory.getCurrentSession()
        .createQuery(query, UsuarioRecompensa.class)
        .setParameter("idUsuario", usuario.getId())
        .setParameter("idRecompensa", recompensa.getId())
        .getResultList();
    return !resultado.isEmpty();
  }

  @Override
  public void utilizar(Integer idUsuarioRecompensa) {
    String query = "UPDATE UsuarioRecompensa SET utilizada = true WHERE id = :idUsuarioRecompensa";
    this.sessionFactory.getCurrentSession()
      .createQuery(query)
      .setParameter("idUsuarioRecompensa", idUsuarioRecompensa)
      .executeUpdate();
  }

  @Override
  public UsuarioRecompensa obtenerPorId(Integer id) {
    return this.sessionFactory.getCurrentSession().get(UsuarioRecompensa.class, id);
  }
}
