package com.tallerwebi.dominio.excepcion;

public class FormatoEmailInvalido extends Exception {

  private static final long serialVersionUID = 1L;

   public FormatoEmailInvalido(String mensaje){
        super(mensaje);
    }

}
