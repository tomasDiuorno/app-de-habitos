let habitoSugerido = null;
document.getElementById("cerrarModal").addEventListener("click", cerrarModal);
document.getElementById("aceptarHabito").addEventListener("click", () => {
  console.log("Habito aceptado: ", habitoSugerido);
})
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
      abrirModal();
  }).catch(error => {
    console.error(error);
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

function abrirModal() {
  const modal = document.getElementById("modalHabito");
  modal.style.display = "flex";
}

function cerrarModal() {
  document.getElementById("modalHabito").style.display = "none";
}
