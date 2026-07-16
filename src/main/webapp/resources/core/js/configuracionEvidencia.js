const ConfiguracionEvidencia = (() => {
  const contenedorEvidenciaTexto = document.getElementById("evidenciaTexto");
  const textArea = document.getElementById("evidenciaTextArea");

  const contenedorEvidenciaImagen = document.getElementById("evidenciaImagen");
  const inputImagen = document.getElementById("evidenciaImagenInput");


  function mostrarTextArea() {
    contenedorEvidenciaTexto.style.display = "block";
    contenedorEvidenciaImagen.style.display = "none";
  };

  function mostrarImagen() {
    contenedorEvidenciaTexto.style.display = "none";
    contenedorEvidenciaImagen.style.display = "block";
  };

  function limpiarFormulario() {
    textArea.value = "";
    inputImagen.value = "";
  };

  const estrategias = {
    HORARIO() {
      configurarTextArea("Ej: 22:30");
    },
    CANTIDAD() {
      configurarTextArea("Ej: 10");
    },
    CHECK() {
      limpiarFormulario();
      mostrarImagen();
    }
  };

  function configurar(tipoHabito) {
    const estrategia = estrategias[tipoHabito];
    if (!estrategia) {
      console.error(`No se encontró una estrategia para el tipo de hábito: ${tipoHabito}`);
      return;
    }
    estrategia();
  };

  function configurarTextArea(placeholder) {
    limpiarFormulario();
    mostrarTextArea();
    textArea.placeholder = placeholder;
  };

  return{
    configurar
  };
})();