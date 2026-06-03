console.log("Cargando funcionalidades del popup de hábitos...");

const modal = document.getElementById("habitModal");
const openButtons = document.querySelectorAll(".open-modal-button");
const modalContent = document.querySelector(".habit-modal-content");

// Calcula y actualiza la barra de progreso visualmente
function actualizarProgresoVisual() {
  const checkboxes = document.querySelectorAll('.checklist-checkbox');
  if (checkboxes.length === 0) {
    const progressBar = document.getElementById('progressBar');
    const progressText = document.getElementById('progressText');
    if (progressBar && progressText) {
        progressBar.style.width = '0%';
        progressText.innerText = '0%';
    }
    return;
  }

  const total = checkboxes.length;
  const completados = Array.from(checkboxes).filter(cb => cb.checked).length;
  const porcentaje = Math.round((completados / total) * 100);

  const progressBar = document.getElementById('progressBar');
  const progressText = document.getElementById('progressText');

  if (progressBar && progressText) {
    progressBar.style.width = porcentaje + '%';
    progressText.innerText = porcentaje + '%';
    
    if (porcentaje === 100) {
      progressBar.style.backgroundColor = '#61bd4f'; // Verde si está completo
    } else {
      progressBar.style.backgroundColor = '#5ba4cf'; // Azul por defecto
    }
  }
}

// Función para generar el HTML de un ítem individual
function crearHtmlItem(idItem, idHabito, descripcion, completado) {
    const isChecked = completado ? 'checked' : '';
    const textDecoration = completado ? 'line-through' : 'none';
    const textColor = completado ? '#888' : '#000';

    return `
        <div class="checklist-item-row" id="item-row-${idItem}" style="display: flex; align-items: center; justify-content: space-between; padding: 5px 0; border-bottom: 1px solid #eee;">
            <label style="display: flex; align-items: center; cursor: pointer; font-size: 14px; flex-grow: 1; margin: 0;">
              <input type="checkbox" class="checklist-checkbox" data-id="${idItem}" data-habito-id="${idHabito}" ${isChecked} 
                     style="margin-right: 10px; width: 16px; height: 16px; cursor: pointer;">
              <span id="text-item-${idItem}" style="text-decoration: ${textDecoration}; color: ${textColor};">${descripcion}</span>
            </label>
            <div style="display: flex; gap: 5px;">
                <button class="btn-editar-item" data-id="${idItem}" data-habito-id="${idHabito}" style="background: none; border: none; cursor: pointer; font-size: 12px; color: #f2a600;">✏️ Editar</button>
                <button class="btn-eliminar-item" data-id="${idItem}" data-habito-id="${idHabito}" style="background: none; border: none; cursor: pointer; font-size: 12px; color: #eb5a46;">❌</button>
            </div>
        </div>
    `;
}

openButtons.forEach((button) => {
  button.addEventListener("click", async (event) => {
    event.preventDefault();
    const habitoId = button.dataset.id;

    try {
      // Pedimos los datos del hábito 
      const response = await fetch(`/spring/habito/${habitoId}`);
      const habito = await response.json();

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
          // Soporta los nombres viejos del mock o los reales de la base de datos
          const texto = item.descripcion || item.tarea; 
          const estado = item.estadoChecklist !== undefined ? item.estadoChecklist : item.completado;
          checklistHTML += crearHtmlItem(item.id, habitoId, texto, estado);
        });
      }
      
      checklistHTML += `</div>`; 

      // Formulario para agregar nuevos items
      checklistHTML += `
        <div style="display: flex; gap: 8px; margin-top: 15px;">
          <input type="text" id="inputNuevaTarea" placeholder="Añadir un elemento..." 
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
      actualizarProgresoVisual();

      // ==========================================
      // MANEJO DE EVENTOS DENTRO DEL MODAL
      // ==========================================

      const container = document.getElementById("checklistItemsContainer");
      
      container.addEventListener("click", async (e) => {
          
          // --- ACCIÓN: MARCAR/DESMARCAR ---
          if(e.target.classList.contains("checklist-checkbox")) {
              const idItem = e.target.getAttribute("data-id");
              const idHabito = e.target.getAttribute("data-habito-id");
              const isChecked = e.target.checked;
              
              const spanTexto = document.getElementById(`text-item-${idItem}`);
              spanTexto.style.textDecoration = isChecked ? 'line-through' : 'none';
              spanTexto.style.color = isChecked ? '#888' : '#000';
              
              actualizarProgresoVisual();

              fetch(`/spring/progreso/habito/${idHabito}/toggle-checklist/${idItem}`, { method: 'POST' })
                .then(res => res.json())
                .then(data => { if(data.status === 'error') alert(data.mensaje); })
                .catch(err => console.error("Error al marcar", err));
          }

          // --- ACCIÓN: ELIMINAR ---
          if(e.target.classList.contains("btn-eliminar-item")) {
              const idItem = e.target.getAttribute("data-id");
              const idHabito = e.target.getAttribute("data-habito-id");
              
              if(confirm("¿Seguro que querés eliminar esta tarea?")) {
                  fetch(`/spring/progreso/habito/${idHabito}/eliminar-checklist/${idItem}`, { method: 'POST' })
                    .then(res => res.json())
                    .then(data => {
                        if(data.status === 'success') {
                            document.getElementById(`item-row-${idItem}`).remove();
                            actualizarProgresoVisual();
                        } else {
                            alert(data.mensaje);
                        }
                    })
                    .catch(err => console.error("Error al eliminar", err));
              }
          }

          // --- ACCIÓN: EDITAR ---
          if(e.target.classList.contains("btn-editar-item")) {
              const idItem = e.target.getAttribute("data-id");
              const idHabito = e.target.getAttribute("data-habito-id");
              const spanTexto = document.getElementById(`text-item-${idItem}`);
              const textoActual = spanTexto.innerText;
              
              const nuevoTexto = prompt("Editá la tarea:", textoActual);
              if(nuevoTexto && nuevoTexto.trim() !== "" && nuevoTexto !== textoActual) {
                  
                  const formData = new URLSearchParams();
                  formData.append('nuevaDescripcion', nuevoTexto.trim());

                  fetch(`/spring/progreso/habito/${idHabito}/editar-checklist/${idItem}`, { 
                      method: 'POST',
                      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                      body: formData.toString()
                  })
                  .then(res => res.json())
                  .then(data => {
                      if(data.status === 'success') {
                          spanTexto.innerText = nuevoTexto.trim();
                      } else {
                          alert(data.mensaje);
                      }
                  })
                  .catch(err => console.error("Error al editar", err));
              }
          }
      });

      // --- ACCIÓN: AGREGAR ---
      document.getElementById("btnAgregarTarea").addEventListener("click", () => {
          const input = document.getElementById("inputNuevaTarea");
          const descripcion = input.value.trim();
          
          if(descripcion === "") return;

          const formData = new URLSearchParams();
          formData.append('descripcion', descripcion);

          fetch(`/spring/progreso/habito/${habitoId}/agregar-checklist`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
              body: formData.toString()
          })
          .then(res => res.json())
          .then(data => {
              if(data.status === 'success') {
                  // Cerramos y volvemos a abrir el modal automáticamente para que traiga los datos frescos con el ID nuevo
                  document.getElementById("closeModal").click();
                  setTimeout(() => button.click(), 100); 
              } else {
                  alert(data.mensaje);
              }
          })
          .catch(err => console.error("Error al agregar", err));
      });

      document.getElementById("closeModal").addEventListener("click", () => {
        modal.classList.remove("active");
      });

    } catch (error) {
      console.error("Error al procesar el hábito:", error);
    }
  });
});

window.addEventListener("click", (event) => {
  if (event.target === modal) {
    modal.classList.remove("active");
  }
});