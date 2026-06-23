const botonesCompletar = document.querySelectorAll(".completar-habito-button");
const modal = document.getElementById("modalCompletar");
const inputHabitoId = document.getElementById("habitoId");
const nombreHabito = document.getElementById("nombreHabitoModal");
const cerrar = document.getElementById("cerrarModal");

botonesCompletar.forEach(boton => {
  boton.addEventListener("click", () => {
    const id = boton.dataset.id;
    const nombre = boton.dataset.nombre;
      inputHabitoId.value = id;
      nombreHabito.innerText = nombre;
      modal.classList.add("active");
  });
});

cerrar.addEventListener("click", () => {
  modal.classList.remove("active");
});

window.addEventListener("click", (event) => {
  if (event.target === modal) {
    modal.classList.remove("active");
  }
});