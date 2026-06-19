console.log("Cargando funcionalidades del popup de hábitos...");

const modal = document.getElementById("habitModal");
const openButtons = document.querySelectorAll(".open-modal-button");
const modalContent = document.querySelector(".habit-modal-content");

function actualizarProgresoVisual() {
  const checkboxes = document.querySelectorAll(".checklist-checkbox");
  const progressBar = document.getElementById("progressBar");
  const progressText = document.getElementById("progressText");

  if (!progressBar || !progressText) {
    return;
  }

  if (checkboxes.length === 0) {
    progressBar.style.width = "0%";
    progressText.innerText = "0% completado";
    return;
  }

  const total = checkboxes.length;
  const completados = Array.from(checkboxes).filter((checkbox) => checkbox.checked).length;
  const porcentaje = Math.round((completados / total) * 100);

  progressBar.style.width = porcentaje + "%";
  progressText.innerText = porcentaje + "% completado";
}

function escaparHtml(texto) {
  const div = document.createElement("div");
  div.innerText = texto;
  return div.innerHTML;
}

function crearHtmlItem(idItem, idHabito, descripcion, completado) {
  const isChecked = completado ? "checked" : "";
  const itemCompletado = completado ? "checklist-item-completado" : "";

  return `
    <div id="item-row-${idItem}" class="checklist-item ${itemCompletado}">
      <div class="checklist-left">
        <input
          type="checkbox"
          class="habit-check checklist-checkbox"
          data-id="${idItem}"
          data-habito-id="${idHabito}"
          ${isChecked}
        >

        <span
          id="text-item-${idItem}"
          class="checklist-text"
        >
          ${escaparHtml(descripcion)}
        </span>

        <input
          id="edit-input-${idItem}"
          class="checklist-edit-input"
          type="text"
          value="${escaparHtml(descripcion)}"
        >
      </div>

      <div id="normal-actions-${idItem}" class="checklist-actions">
        <button
          type="button"
          class="btn-editar-item"
          data-id="${idItem}"
          data-habito-id="${idHabito}"
        >
          Editar
        </button>

        <button
          type="button"
          class="btn-eliminar-item"
          data-id="${idItem}"
          data-habito-id="${idHabito}"
        >
          Eliminar
        </button>
      </div>

      <div id="edit-actions-${idItem}" class="checklist-actions checklist-edit-actions">
        <button
          type="button"
          class="btn-guardar-item"
          data-id="${idItem}"
          data-habito-id="${idHabito}"
        >
          Guardar
        </button>

        <button
          type="button"
          class="btn-cancelar-edicion"
          data-id="${idItem}"
        >
          Cancelar
        </button>
      </div>
    </div>
  `;
}

async function cargarHabitoEnModal(button) {
  const habitoId = button.dataset.id;

  try {
    const response = await fetch(`/spring/habito/${habitoId}`);

    if (!response.ok) {
      throw new Error("No se pudo obtener el hábito");
    }

    const habito = await response.json();

    let checklistHTML = `
      <div class="progress-section">
        <div class="popup-section-header">
          <h3>Progreso</h3>
          <span class="popup-chip">Checklist diario</span>
        </div>

        <div class="progress-bar-container">
          <div id="progressBar" class="progress-bar"></div>
        </div>

        <p id="progressText">0% completado</p>
      </div>

      <div class="checklist-section">
        <div class="popup-section-header">
          <h3>Tareas</h3>
        </div>

        <div id="checklistItemsContainer">
    `;

    if (habito.checklist && habito.checklist.length > 0) {
      habito.checklist.forEach((item) => {
        const texto = item.descripcion || item.tarea;
        const estado = item.estadoChecklist !== undefined ? item.estadoChecklist : item.completado;

        checklistHTML += crearHtmlItem(item.id, habitoId, texto, estado);
      });
    } else {
      checklistHTML += `
        <p id="emptyChecklistMessage" class="empty-checklist-message">
          Todavía no agregaste tareas para este hábito.
        </p>
      `;
    }

    checklistHTML += `
        </div>

        <div class="agregar-checklist">
          <input
            type="text"
            id="inputNuevaTarea"
            placeholder="Escribí una nueva tarea"
          >

          <button
            type="button"
            id="btnAgregarTarea"
          >
            Añadir
          </button>
        </div>
      </div>
    `;

    modalContent.innerHTML = `
      <button type="button" id="closeModal" class="habit-modal-close">X</button>

      <p class="habit-modal-kicker">Progreso del hábito</p>

      <h2>${escaparHtml(habito.titulo)}</h2>

      <p class="habit-modal-description">${escaparHtml(habito.descripcion)}</p>

      ${checklistHTML}
    `;

    modal.classList.add("active");
    actualizarProgresoVisual();
    configurarEventosDelModal(button, habitoId);
  } catch (error) {
    console.error("Error al procesar el hábito:", error);
  }
}

function configurarEventosDelModal(button, habitoId) {
  const container = document.getElementById("checklistItemsContainer");
  const botonAgregar = document.getElementById("btnAgregarTarea");
  const inputNuevaTarea = document.getElementById("inputNuevaTarea");
  const botonCerrar = document.getElementById("closeModal");

  container.addEventListener("click", async (event) => {
    if (event.target.classList.contains("checklist-checkbox")) {
      await marcarODesmarcarChecklist(event.target);
    }

    if (event.target.classList.contains("btn-eliminar-item")) {
      await eliminarChecklist(event.target);
    }

    if (event.target.classList.contains("btn-editar-item")) {
      activarModoEdicion(event.target);
    }

    if (event.target.classList.contains("btn-cancelar-edicion")) {
      cancelarEdicion(event.target);
    }

    if (event.target.classList.contains("btn-guardar-item")) {
      await guardarEdicion(event.target);
    }
  });

  container.addEventListener("keydown", async (event) => {
    if (!event.target.classList.contains("checklist-edit-input")) {
      return;
    }

    const idItem = event.target.id.replace("edit-input-", "");

    if (event.key === "Enter") {
      event.preventDefault();

      const botonGuardar = document.querySelector(`.btn-guardar-item[data-id="${idItem}"]`);
      await guardarEdicion(botonGuardar);
    }

    if (event.key === "Escape") {
      event.preventDefault();

      const botonCancelar = document.querySelector(`.btn-cancelar-edicion[data-id="${idItem}"]`);
      cancelarEdicion(botonCancelar);
    }
  });

  botonAgregar.addEventListener("click", async () => {
    await agregarChecklist(button, habitoId);
  });

  inputNuevaTarea.addEventListener("keydown", async (event) => {
    if (event.key === "Enter") {
      event.preventDefault();
      await agregarChecklist(button, habitoId);
    }
  });

  botonCerrar.addEventListener("click", () => {
    modal.classList.remove("active");
  });
}

async function marcarODesmarcarChecklist(checkbox) {
  const idItem = checkbox.getAttribute("data-id");
  const idHabito = checkbox.getAttribute("data-habito-id");
  const row = document.getElementById(`item-row-${idItem}`);

  if (checkbox.checked) {
    row.classList.add("checklist-item-completado");
  } else {
    row.classList.remove("checklist-item-completado");
  }

  actualizarProgresoVisual();

  try {
    const response = await fetch(`/spring/habito/${idHabito}/toggle-checklist/${idItem}`, {
      method: "POST"
    });

    const data = await response.json();

    if (data.status === "error") {
      alert(data.mensaje);
    }
  } catch (error) {
    console.error("Error al marcar checklist:", error);
  }
}

async function eliminarChecklist(boton) {
  const idItem = boton.getAttribute("data-id");
  const idHabito = boton.getAttribute("data-habito-id");

  const confirmar = confirm("¿Seguro que querés eliminar esta tarea?");

  if (!confirmar) {
    return;
  }

  try {
    const response = await fetch(`/spring/habito/${idHabito}/eliminar-checklist/${idItem}`, {
      method: "POST"
    });

    const data = await response.json();

    if (data.status === "success") {
      document.getElementById(`item-row-${idItem}`).remove();

      const emptyMessage = document.getElementById("emptyChecklistMessage");
      const itemsRestantes = document.querySelectorAll(".checklist-item");

      if (itemsRestantes.length === 0 && !emptyMessage) {
        document.getElementById("checklistItemsContainer").innerHTML = `
          <p id="emptyChecklistMessage" class="empty-checklist-message">
            Todavía no agregaste tareas para este hábito.
          </p>
        `;
      }

      actualizarProgresoVisual();
    } else {
      alert(data.mensaje);
    }
  } catch (error) {
    console.error("Error al eliminar checklist:", error);
  }
}

function activarModoEdicion(boton) {
  const idItem = boton.getAttribute("data-id");

  const row = document.getElementById(`item-row-${idItem}`);
  const input = document.getElementById(`edit-input-${idItem}`);
  const texto = document.getElementById(`text-item-${idItem}`);

  input.value = texto.innerText.trim();

  row.classList.add("editing");
  input.focus();
  input.select();
}

function cancelarEdicion(boton) {
  const idItem = boton.getAttribute("data-id");
  const row = document.getElementById(`item-row-${idItem}`);

  row.classList.remove("editing");
}

async function guardarEdicion(boton) {
  const idItem = boton.getAttribute("data-id");
  const idHabito = boton.getAttribute("data-habito-id");

  const row = document.getElementById(`item-row-${idItem}`);
  const spanTexto = document.getElementById(`text-item-${idItem}`);
  const input = document.getElementById(`edit-input-${idItem}`);

  const textoActual = spanTexto.innerText.trim();
  const nuevoTexto = input.value.trim();

  if (nuevoTexto === "" || nuevoTexto === textoActual) {
    row.classList.remove("editing");
    return;
  }

  const formData = new URLSearchParams();
  formData.append("nuevaDescripcion", nuevoTexto);

  try {
    const response = await fetch(`/spring/habito/${idHabito}/editar-checklist/${idItem}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: formData.toString()
    });

    const data = await response.json();

    if (data.status === "success") {
      spanTexto.innerText = nuevoTexto;
      row.classList.remove("editing");
    } else {
      alert(data.mensaje);
    }
  } catch (error) {
    console.error("Error al editar checklist:", error);
  }
}

async function agregarChecklist(button, habitoId) {
  const input = document.getElementById("inputNuevaTarea");
  const descripcion = input.value.trim();

  if (descripcion === "") {
    return;
  }

  const formData = new URLSearchParams();
  formData.append("descripcion", descripcion);

  try {
    const response = await fetch(`/spring/habito/${habitoId}/agregar-checklist`, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: formData.toString()
    });

    const data = await response.json();

    if (data.status === "success") {
      modal.classList.remove("active");
      await cargarHabitoEnModal(button);
    } else {
      alert(data.mensaje);
    }
  } catch (error) {
    console.error("Error al agregar checklist:", error);
  }
}

openButtons.forEach((button) => {
  button.addEventListener("click", async (event) => {
    event.preventDefault();
    await cargarHabitoEnModal(button);
  });
});

window.addEventListener("click", (event) => {
  if (event.target === modal) {
    modal.classList.remove("active");
  }
});