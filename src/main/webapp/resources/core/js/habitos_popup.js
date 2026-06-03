console.log("comprobando que funque el js");

const modal = document.getElementById("habitModal");
const openButtons = document.querySelectorAll(".open-modal-button");
const modalContent = document.querySelector(".habit-modal-content");

function actualizarProgreso() {
  const checkboxes = document.querySelectorAll('.checklist-checkbox');
  if (checkboxes.length === 0) return;

  const total = checkboxes.length;
  const completados = Array.from(checkboxes).filter(cb => cb.checked).length;
  const porcentaje = Math.round((completados / total) * 100);

  const progressBar = document.getElementById('progressBar');
  const progressText = document.getElementById('progressText');

  if (progressBar && progressText) {
    progressBar.style.width = porcentaje + '%';
    progressText.innerText = porcentaje + '%';
    
    if (porcentaje === 100) {
      progressBar.style.backgroundColor = '#61bd4f';
    } else {
      progressBar.style.backgroundColor = '#5ba4cf';
    }
  }
}

openButtons.forEach((button) => {
  button.addEventListener("click", async (event) => {
    event.preventDefault();
    const habitoId = button.dataset.id;

    try {
      const response = await fetch(`/spring/habito/${habitoId}`);
      const texto = await response.text();
      const habito = JSON.parse(texto);

      // Creamos la estructura base del checklist y una caja contenedora para los ítems
      let checklistHTML = `
        <div style="margin-top: 20px;">
          <h3 style="margin-bottom: 10px; font-size: 16px;">Checklist</h3>
          <div style="display: flex; align-items: center; margin-bottom: 15px;">
            <span id="progressText" style="font-size: 12px; margin-right: 10px; min-width: 35px;">0%</span>
            <div style="background-color: #e9ecef; border-radius: 4px; flex-grow: 1; height: 8px; overflow: hidden;">
              <div id="progressBar" style="background-color: #5ba4cf; width: 0%; height: 100%; transition: width 0.3s ease, background-color 0.3s ease;"></div>
            </div>
          </div>
          
          <div id="checklistItemsContainer" style="display: flex; flex-direction: column; gap: 8px;">
      `;

      if (habito.checklist && habito.checklist.length > 0) {
        habito.checklist.forEach(item => {
          const isChecked = item.completado ? 'checked' : '';
          checklistHTML += `
            <label style="display: flex; align-items: center; cursor: pointer; font-size: 14px;">
              <input type="checkbox" class="checklist-checkbox" data-id="${item.id}" ${isChecked} 
                     style="margin-right: 10px; width: 16px; height: 16px; cursor: pointer;">
              <span>${item.tarea}</span>
            </label>
          `;
        });
      }
      
      checklistHTML += `</div>`; // Cierre de #checklistItemsContainer

      // Añadimos el formulario estilo Trello para sumar nuevas tareas al vuelo
      checklistHTML += `
        <div style="display: flex; gap: 8px; margin-top: 15px;">
          <input type="text" id="inputNuevaTarea" placeholder="Añadir un elemento" 
                 style="flex-grow: 1; padding: 6px; border: 1px solid #ccc; border-radius: 4px; font-size: 14px;">
          <button id="btnAgregarTarea" style="background-color: #5ba4cf; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; font-size: 14px; font-weight: bold;">Añadir</button>
        </div>
      </div>`;

      modalContent.innerHTML = `
        <button class="habit-modal-close" id="closeModal">X</button>
        <h2>${habito.titulo}</h2>
        <p>${habito.descripcion}</p>
        <p><strong>Frecuencia:</strong> ${habito.frecuencia}</p>
        <p><strong>Duración estimada:</strong> ${habito.duracionEstimada} minutos</p>
        ${checklistHTML}
      `;

      modal.classList.add("active");
      actualizarProgreso();

      // Asignamos eventos 'change' a los checkboxes ya existentes
      document.querySelectorAll('.checklist-checkbox').forEach(cb => {
        cb.addEventListener('change', actualizarProgreso);
      });

      // Lógica para el botón de añadir tareas
      const btnAgregar = document.getElementById("btnAgregarTarea");
      const inputNuevaTarea = document.getElementById("inputNuevaTarea");
      const itemsContainer = document.getElementById("checklistItemsContainer");

      btnAgregar.addEventListener("click", async () => {
        const textoTarea = inputNuevaTarea.value.trim();
        if (textoTarea === "") return;

        try {
          // Enviamos la petición POST al nuevo endpoint del controlador
          const res = await fetch(`/spring/habito/${habitoId}/agregar-tarea?nombreTarea=${encodeURIComponent(textoTarea)}`, {
            method: 'POST'
          });
          
          if (res.ok) {
            const nuevoItem = await res.json();
            
            // Creamos dinámicamente el nuevo elemento label en el DOM
            const nuevoLabel = document.createElement("label");
            nuevoLabel.style = "display: flex; align-items: center; cursor: pointer; font-size: 14px;";
            nuevoLabel.innerHTML = `
              <input type="checkbox" class="checklist-checkbox" data-id="${nuevoItem.id}" 
                     style="margin-right: 10px; width: 16px; height: 16px; cursor: pointer;">
              <span>${nuevoItem.tarea}</span>
            `;

            // Le asociamos el recálculo de la barra de progreso
            nuevoLabel.querySelector('.checklist-checkbox').addEventListener('change', actualizarProgreso);
            
            // Lo metemos dentro del contenedor visual
            itemsContainer.appendChild(nuevoLabel);
            
            // Limpiamos el campo de texto y recalculamos el porcentaje total
            inputNuevaTarea.value = "";
            actualizarProgreso();
          }
        } catch (err) {
          console.error("Error al guardar el nuevo ítem del checklist:", err);
        }
      });

      document.getElementById("closeModal").addEventListener("click", () => {
        modal.classList.remove("active");
      });

    } catch (error) {
      console.error("Error al obtener hábito:", error);
    }
  });
});

window.addEventListener("click", (event) => {
  if (event.target === modal) {
    modal.classList.remove("active");
  }
});