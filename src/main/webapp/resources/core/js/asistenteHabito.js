document.getElementById("btnGenerarHabito").addEventListener("click", generarHabito);

function generarHabito() {
  const objetivo = document.getElementById("objetivoHabito").value;
  fetch("/spring/api/habitos/recomendar", {method: "POST", headers: {"Content-Type": "application/json"},
    body: JSON.stringify({
      objetivo: objetivo
    })
  }).then(response => {
    if (!response.ok) {
      throw new Error("Error generando hábito");
    }
  return response.json();
  }).then(habito => {
    mostrarHabito(habito);
  }).catch(error => {
    console.error(error);
  });
}

function mostrarHabito(habito) {
  const contenedor = document.getElementById("resultadoIA");
  contenedor.innerHTML = `
<h4>✨ Hábito sugerido</h4>
<p>
<b>Nombre:</b>
${habito.nombre}
</p>
<p>
<b>Descripción:</b>
${habito.descripcion}
</p>
<p>
<b>Frecuencia:</b>
${habito.frecuencia}
</p>
<p>
<b>Categoría:</b>
${habito.categoria}
</p>
<button>
Crear hábito
</button>
`;
}