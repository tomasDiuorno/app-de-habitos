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
        progressText.innerText = "0%";
        return;
    }

    const total = checkboxes.length;
    const completados = Array.from(checkboxes).filter((checkbox) => checkbox.checked).length;
    const porcentaje = Math.round((completados / total) * 100);

    progressBar.style.width = porcentaje + "%";
    progressText.innerText = porcentaje + "%";
}

function escaparHtml(texto) {
    const div = document.createElement("div");
    div.innerText = texto;
    return div.innerHTML;
}

function crearHtmlItem(idItem, idHabito, descripcion, completado) {
    const isChecked = completado ? "checked" : "";
    const textDecoration = completado ? "line-through" : "none";
    const textColor = completado ? "#888" : "var(--text-main)";

    return `
        <div id="item-row-${idItem}" class="checklist-item">
            <label>
                <input
                    type="checkbox"
                    class="habit-check checklist-checkbox"
                    data-id="${idItem}"
                    data-habito-id="${idHabito}"
                    ${isChecked}
                >

                <span
                    id="text-item-${idItem}"
                    style="text-decoration: ${textDecoration}; color: ${textColor};"
                >
                    ${escaparHtml(descripcion)}
                </span>
            </label>

            <div class="checklist-actions">
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
                <h3>Progreso</h3>

                <div class="progress-bar-container">
                    <div id="progressBar" class="progress-bar"></div>
                </div>

                <p id="progressText">0%</p>
            </div>

            <div class="checklist-section">
                <h3>Checklist diario</h3>

                <div id="checklistItemsContainer">
        `;

        if (habito.checklist && habito.checklist.length > 0) {
            habito.checklist.forEach((item) => {
                const texto = item.descripcion || item.tarea;
                const estado = item.estadoChecklist !== undefined ? item.estadoChecklist : item.completado;

                checklistHTML += crearHtmlItem(item.id, habitoId, texto, estado);
            });
        }

        checklistHTML += `
                </div>

                <div class="agregar-checklist">
                    <input
                        type="text"
                        id="inputNuevaTarea"
                        placeholder="Agregá una tarea"
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

            <h2>${escaparHtml(habito.titulo)}</h2>

            <p>${escaparHtml(habito.descripcion)}</p>

            <p>Frecuencia: ${escaparHtml(habito.frecuencia)}</p>

            <p>Duración estimada: ${habito.duracionEstimada} minutos</p>

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
    const botonCerrar = document.getElementById("closeModal");

    container.addEventListener("click", async (event) => {
        if (event.target.classList.contains("checklist-checkbox")) {
            await marcarODesmarcarChecklist(event.target);
        }

        if (event.target.classList.contains("btn-eliminar-item")) {
            await eliminarChecklist(event.target);
        }

        if (event.target.classList.contains("btn-editar-item")) {
            await editarChecklist(event.target);
        }
    });

    botonAgregar.addEventListener("click", async () => {
        await agregarChecklist(button, habitoId);
    });

    botonCerrar.addEventListener("click", () => {
        modal.classList.remove("active");
    });
}

async function marcarODesmarcarChecklist(checkbox) {
    const idItem = checkbox.getAttribute("data-id");
    const idHabito = checkbox.getAttribute("data-habito-id");
    const spanTexto = document.getElementById(`text-item-${idItem}`);

    spanTexto.style.textDecoration = checkbox.checked ? "line-through" : "none";
    spanTexto.style.color = checkbox.checked ? "#888" : "var(--text-main)";

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
            actualizarProgresoVisual();
        } else {
            alert(data.mensaje);
        }

    } catch (error) {
        console.error("Error al eliminar checklist:", error);
    }
}

async function editarChecklist(boton) {
    const idItem = boton.getAttribute("data-id");
    const idHabito = boton.getAttribute("data-habito-id");
    const spanTexto = document.getElementById(`text-item-${idItem}`);
    const textoActual = spanTexto.innerText.trim();

    const nuevoTexto = prompt("Editá la tarea:", textoActual);

    if (!nuevoTexto || nuevoTexto.trim() === "" || nuevoTexto.trim() === textoActual) {
        return;
    }

    const formData = new URLSearchParams();
    formData.append("nuevaDescripcion", nuevoTexto.trim());

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
            spanTexto.innerText = nuevoTexto.trim();
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