package com.tallerwebi.infraestructura.IA;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.interfaz.ServicioIA;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServicioGeminiImpl implements ServicioIA {

  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;
  private final String apiKey;
  private static final String ATRIBUTO_TEXT = "text";
  private static final String ATRIBUTO_PARTS = "parts";
  private static final String ATRIBUTO_CONTENTS = "contents";
  private static final String SYSTEM_INSTRUCTION =
    "Sos un asistente experto en creación de hábitos. " +
    "Tu trabajo es transformar objetivos generales " +
    "en hábitos concretos. " +
    "Reglas: " +
    "- El hábito debe ser medible. " +
    "- Debe tener frecuencia. " +
    "- Debe ser simple. " +
    "- No agregues explicaciones. " +
    "Respondé únicamente JSON: " +
    "{ " +
    "\"nombre\":\"\", " +
    "\"descripcion\":\"\", " +
    "\"frecuencia\":\"\", " +
    "\"categoria\":\"\" " +
    "}";

  // Modelos disponibles:
  //
  // Nivel Gratuito (Limitado por cuotas, mejora la IA con tus datos):
  // - gemini-3-flash: Recomendado, mejor equilibrio actual.
  // - gemini-2.5-flash: Estable y rápido.
  // - gemini-2.5-flash-lite: Máxima velocidad, mayores límites de peticiones.
  //
  // Nivel De Pago (Pay-as-you-go, datos privados):
  // - gemini-3.1-pro: Razonamiento complejo y tareas avanzadas.
  // - gemini-2.5-pro: Tareas de análisis y razonamiento avanzado.
  //
  // Métodos disponibles (usar después de los dos puntos en la URL):
  // - generateContent: Generación síncrona (espera la respuesta completa)
  // - streamGenerateContent: Generación en tiempo real (streaming, ideal para UI)
  // - countTokens: Verifica el costo/uso de tokens antes de procesar
  // - embedContent: Convierte texto en vectores (para búsqueda semántica)
  private final String URL;

  @Autowired
  public ServicioGeminiImpl(RestTemplate restTemplate, Dotenv dotenv, ObjectMapper mapper) {
    this.restTemplate = restTemplate;
    this.apiKey = dotenv.get("GEMINI_API_KEY");
    this.URL = dotenv.get("GEMINI_URL");
    this.mapper = mapper;
  }

  @Override
  public String preguntar(String mensaje) {
    try {
      return ejecutarConContexto(mensaje, ServicioGeminiImpl.SYSTEM_INSTRUCTION);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error de comunicacion con Gemini", e);
    }
  }

  @Override
  public String evaluarImagen(String imagen, String titulo, String descripcion, String mimeType) {
    try {
      String contexto = String.format(
        "Eres un experto en evaluación de hábitos. " +
        "Tu tarea es evaluar si la imagen proporcionada cumple con el hábito descrito. " +
        "El título del hábito es: %s. " +
        "La descripción del hábito es: %s. " +
        "Responde con 'SI' o 'NO', y agrega una breve explicación de no más de 20 caracteres.",
        titulo,
        descripcion
      );
      return ejecutarConContextoImagen("Evaluar la imagen adjunta.", contexto, imagen, mimeType);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error de comunicacion con Gemini", e);
    }
  }

  private String ejecutarConContexto(String mensaje, String contexto)
    throws JsonProcessingException {
    Map<String, Object> body = crearBody(contexto, List.of(crearTextPart(mensaje)));
    return ejecutarPeticion(body);
  }

  private String ejecutarConContextoImagen(
    String mensaje,
    String contexto,
    String imagenBase64,
    String mimeType
  ) throws JsonProcessingException {
    Map<String, Object> body = crearBody(
      contexto,
      List.of(crearTextPart(mensaje), crearImagePart(imagenBase64, mimeType))
    );
    return ejecutarPeticion(body);
  }

  private String ejecutarPeticion(Map<String, Object> body) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("X-goog-api-key", this.apiKey);

    String requestBody = mapper.writeValueAsString(body);
    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
    String response = restTemplate.postForObject(URL, request, String.class);
    return extraerRespuesta(response);
  }

  private Map<String, Object> crearTextPart(String texto) {
    Map<String, Object> textPart = new HashMap<>();
    textPart.put(ATRIBUTO_TEXT, texto);
    return textPart;
  }

  private Map<String, Object> crearImagePart(String imagenBase64, String mimeType) {
    Map<String, Object> imagePart = new HashMap<>();
    Map<String, Object> inlineData = new HashMap<>();
    inlineData.put("mime_type", mimeType);
    inlineData.put("data", imagenBase64);
    imagePart.put("inline_data", inlineData);
    return imagePart;
  }

  private Map<String, Object> crearBody(String contexto, List<Object> parts) {
    Map<String, Object> body = new HashMap<>();
    Map<String, Object> systemInstructionPart = new HashMap<>();
    systemInstructionPart.put(ATRIBUTO_PARTS, List.of(Map.of(ATRIBUTO_TEXT, contexto)));
    body.put("system_instruction", systemInstructionPart);
    Map<String, Object> contents = new HashMap<>();
    contents.put(ATRIBUTO_PARTS, parts);
    body.put(ATRIBUTO_CONTENTS, List.of(contents));
    return body;
  }

  private String extraerRespuesta(String json) throws JsonProcessingException {
    JsonNode root = mapper.readTree(json);
    return root
      .path("candidates")
      .get(0)
      .path("content")
      .path("parts")
      .get(0)
      .path("text")
      .asText();
  }
}
