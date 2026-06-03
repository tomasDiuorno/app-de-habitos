console.log("comprobando que funque el js");

const modal = document.getElementById("habitModal");

const openButtons = document.querySelectorAll(".open-modal-button");

const modalContent = document.querySelector(".habit-modal-content");

openButtons.forEach((button) => {

  button.addEventListener("click", async (event) => {

    event.preventDefault();

    const habitoId = button.dataset.id;

    try {

      const response = await fetch(`/spring/habito/${habitoId}`);

      const texto = await response.text();

      console.log(texto);

      const habito = JSON.parse(texto);

      modalContent.innerHTML = `
        <button class="habit-modal-close" id="closeModal">
          X
        </button>

        <h2>${habito.titulo}</h2>

        <p>
          ${habito.descripcion}
        </p>

        <p>
          <strong>Frecuencia:</strong> ${habito.frecuencia}
        </p>

        <p>
          <strong>Duración estimada:</strong>
          ${habito.duracionEstimada} minutos
        </p>
      `;

      modal.classList.add("active");

      document
        .getElementById("closeModal")
        .addEventListener("click", () => {

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