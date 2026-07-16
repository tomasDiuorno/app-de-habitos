const popup = document.getElementById("resultadoHabitoPopup");

if(popup) {
  mostrarResultado();
};

function mostrarResultado(){
  configurarContenido();
  popup.style.display = "flex";
  setTimeout(cerrarPopup, 4000);
}

function configurarContenido() {
  const exito = popup.dataset.exito === "true";
  const icono = document.getElementById("resultadoIcono");
  const titulo = document.getElementById("resultadoTitulo");
  const mensaje = document.getElementById("resultadoMensaje");

  mensaje.innerHTML = popup.dataset.mensaje;
  if (exito) {
    configurarExito(icono, titulo);
    return;
  }
  configurarError(icono, titulo);
}

function configurarExito(icono, titulo) {
  popup.classList.add("popup-exito");
  icono.innerHTML = "&#10004;";
  titulo.innerHTML = "¡Habito completado con éxito!";
}

function configurarError(icono, titulo) {
  popup.classList.add("popup-error");
  icono.innerHTML = "&#10006;";
  titulo.innerHTML = "¡Error al completar el habito!";
}

function cerrarPopup() {
  popup.style.display = "none";
}