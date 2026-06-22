package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.componentes.EvaluadorHabito;
import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.RegistroHabito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.entidades.UsuarioHabito;
import com.tallerwebi.dominio.factory.EvaluadorHabitosFactory;
import com.tallerwebi.dominio.interfaz.RepositorioRegistroHabito;
import com.tallerwebi.dominio.interfaz.ServicioEvaluadorHabito;
import com.tallerwebi.presentacion.DTO.ResultadoEvaluacionDTO;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioEvaluadorHabito")
@Transactional
public class ServicioEvaluadorHabitoImp implements ServicioEvaluadorHabito {

  EvaluadorHabitosFactory factory;
  RepositorioRegistroHabito registroHabitoRepositorio;

  @Autowired
  public ServicioEvaluadorHabitoImp(
    EvaluadorHabitosFactory factory,
    RepositorioRegistroHabito registroHabitoRepositorio
  ) {
    this.factory = factory;
    this.registroHabitoRepositorio = registroHabitoRepositorio;
  }

  @Override
  public void completarHabito(UsuarioHabito usuarioHabito, String evidencia) {
    Habito habito = usuarioHabito.getHabito();
    Usuario usuario = usuarioHabito.getUsuario();
    EvaluadorHabito evaluador = factory.obtener(habito.getTipoHabito());

    ResultadoEvaluacionDTO resultado = evaluador.evaluar(habito, evidencia);
    RegistroHabito registro = new RegistroHabito();

    registro.setUsuario(usuario);
    registro.setHabito(habito);
    registro.setCompletado(resultado.getCumplido());

    registroHabitoRepositorio.guardar(registro);
  }
}
