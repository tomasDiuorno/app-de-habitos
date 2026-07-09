package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Habito;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.HabitoExistenteExeption;
import com.tallerwebi.dominio.excepcion.LimiteHabitosAlcanzadoException;
import com.tallerwebi.dominio.excepcion.UsuarioYaUnidoAHabitoException;
import com.tallerwebi.presentacion.DTO.RegistroHabitoDTO;

public interface ServicioHabitoCompartido {
  Habito crearHabitoGrupal(RegistroHabitoDTO datos, Usuario creador)
    throws HabitoExistenteExeption, LimiteHabitosAlcanzadoException;

  void unirseAHabitoGrupal(Habito habito, Usuario usuario)
    throws LimiteHabitosAlcanzadoException, UsuarioYaUnidoAHabitoException;
}
