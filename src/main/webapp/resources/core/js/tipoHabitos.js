document.addEventListener("DOMContentLoaded", () => {
  const selectTipo = document.getElementById("tipoHabito");
  const bloques = ["bloqueHorario", "bloqueCantidad", "bloqueDuracion"];
  function ocultarTodos() {
    bloques.forEach(id => {
      document.getElementById(id).style.display = "none";
    });
  }

  selectTipo.addEventListener("change", () => {
    ocultarTodos();
    const tipo = selectTipo.value;
    if (tipo === "HORARIO") {
      document.getElementById("bloqueHorario").style.display = "block";
    }
    if (tipo === "CANTIDAD") {
      document.getElementById("bloqueCantidad").style.display = "block";
    }
    if (tipo === "DURACION") {
      document.getElementById("bloqueDuracion").style.display = "block";
    }
  });
});