const botonesCompletar = document.querySelectorAll(".completar-habito-button");
const modal = document.getElementById("modalCompletar");
const inputHabitoId = document.getElementById("habitoId");
const nombreHabito = document.getElementById("nombreHabitoModal");
const cerrar = document.getElementById("cerrarModal");

const form = document.querySelector("#modalCompletar form");

form.addEventListener("submit", () => {
    console.log(inputHabitoId.value);
});

botonesCompletar.forEach(boton => {
  boton.addEventListener("click", () => {
    const id = boton.dataset.id;
    const nombre = boton.dataset.nombre;
    inputHabitoId.value = id;
    nombreHabito.innerText = nombre;
    console.log(id);
    console.log(boton.dataset.tipo);
    configurarEvidencia(boton.dataset.tipo);
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