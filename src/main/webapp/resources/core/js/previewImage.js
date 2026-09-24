const inputImagen = document.getElementById("evidenciaImagenInput");
const preview = document.getElementById("previewImagen");

inputImagen.addEventListener("change", e => {
  const archivo = e.target.files[0];
  if(!archivo){
    preview.style.display = "none";
    return;
  }
  preview.src = URL.createObjectURL(archivo);
  preview.style.display = "block";
});
