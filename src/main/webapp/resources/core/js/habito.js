const botones = document.querySelectorAll(".completar-habito-button");

botones.forEach(boton => {
  boton.addEventListener("click", () => {
    const idHabito = boton.dataset.habitoId;
  });
});