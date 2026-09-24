let habitoSugerido = null;

document.getElementById("cerrarModalHabito").addEventListener("click", cerrarModal);
document.getElementById("cancelarHabito").addEventListener("click", cerrarModal);
document.getElementById("aceptarHabito").addEventListener("click", crearHabitoDesdeSugerencia);
document.getElementById("generarHabito").addEventListener("click", generarHabito);

function generarHabito() {
  const objetivo = document.getElementById("objetivo").value;
  fetch("/spring/api/habitos/recomendar", {
    method: "POST",
    headers: { "Content-type": "application/json" },
    body: JSON.stringify({
      objetivo: objetivo
    })
  }).then(response => {
    if (!response.ok) {
      throw new Error("Error generando recomendacion");
    }
    return response.json();
  }).then(habito => {
    habitoSugerido = habito;
    mostrarSugerencia(habito);
    limpiarMensaje();
    abrirModal();
  }).catch(error => {
    console.error(error);
  });
}

function crearHabitoDesdeSugerencia() {
  if (!habitoSugerido) {
    return;
  }

  const botonCrear = document.getElementById("aceptarHabito");
  const botonCancelar = document.getElementById("cancelarHabito");
  botonCrear.disabled = true;
  botonCancelar.disabled = true;

  fetch("/spring/api/habitos/crear-desde-sugerencia", {
    method: "POST",
    headers: { "Content-type": "application/json" },
    body: JSON.stringify(habitoSugerido)
  }).then(response => {
    return response.json().then(cuerpo => ({ ok: response.ok, cuerpo: cuerpo }));
  }).then(resultado => {
    if (!resultado.ok) {
      throw new Error(resultado.cuerpo.error || "No se pudo crear el hábito");
    }

    habitoSugerido = null;

    if (resultado.cuerpo.mostrarLogro) {
      cerrarModal();
      mostrarLogro(resultado.cuerpo.tituloLogro, resultado.cuerpo.descripcionLogro);
      setTimeout(() => {
        window.location.href = "/spring/habitos";
      }, 3500);
    } else {
      mostrarMensaje(resultado.cuerpo.mensaje || "¡Hábito creado con éxito!", "exito");
      setTimeout(() => {
        cerrarModal();
        window.location.href = "/spring/habitos";
      }, 1500);
    }
  }).catch(error => {
    mostrarMensaje(error.message, "error");
    botonCrear.disabled = false;
    botonCancelar.disabled = false;
  });
}

function mostrarSugerencia(habito) {
  const contenedor = document.getElementById("sugerencia");
  contenedor.innerHTML = `
  <h3>Sugerencia IA</h3>
  <p>Nombre: <b>${habito.nombre}</b></p>
  <p>Descripcion: <b>${habito.descripcion}</b></p>
  <p>Frecuencia: <b>${habito.frecuencia}</b></p>
  <p>Categoria: <b>${habito.categoria}</b></p>`;
}

function mostrarMensaje(texto, tipo) {
  const mensaje = document.getElementById("mensajeModal");
  mensaje.textContent = texto;
  mensaje.className = "mensaje-modal " + tipo;
}

function limpiarMensaje() {
  const mensaje = document.getElementById("mensajeModal");
  mensaje.textContent = "";
  mensaje.className = "mensaje-modal";
}

function abrirModal() {
  const modal = document.getElementById("modalHabito");
  modal.style.display = "flex";
}

function cerrarModal() {
  document.getElementById("modalHabito").style.display = "none";
  document.getElementById("aceptarHabito").disabled = false;
  document.getElementById("cancelarHabito").disabled = false;
  limpiarMensaje();
}

function mostrarLogro(titulo, descripcion) {
  const popup = document.getElementById("logroPopupIA");
  document.getElementById("tituloLogroIA").textContent = titulo;
  document.getElementById("descripcionLogroIA").textContent = descripcion;
  popup.style.display = "";
}