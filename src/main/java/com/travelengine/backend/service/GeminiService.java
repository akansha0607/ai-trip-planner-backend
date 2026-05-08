package com.travelengine.backend.service;

import com.travelengine.backend.dto.TripRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final WebClient webClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${groq.api.key}")
    private String apiKey;

    /**
     * Calls Gemini’s **generateContent** endpoint.
     * The request body contains a `generationConfig` with `stream:true` so Gemini returns
     * Server‑Sent Events (text/event‑stream). Each line is a JSON object with a `candidates`
     * field that we later extract to a simple `{ "chunk": "…" }` payload.
     */
    public Flux<String> generateItineraryStream(TripRequest request) {

        String prompt = buildPrompt(request);

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.1-8b-instant",
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "temperature", 0.7
        );

        String response = webClient.post()
                .uri("https://api.groq.com/openai/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response2 -> response2.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Groq API Error: " + body))
                )
                .bodyToMono(String.class)
                .block();
        String content = extractContent(response);
        return Flux.just(content);
    }

    private String extractContent(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            return root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();
        } catch (Exception e) {
            return response;
        }
    }

    /** Build a human‑readable prompt from the incoming DTO. */
    private String buildPrompt(TripRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("Create a detailed travel itinerary.\n")
                .append("Destination: ").append(request.getDestination()).append("\n")
                .append("Duration (days): ").append(request.getDurationDays()).append("\n")
                .append("Budget: ").append(request.getBudget()).append("\n");
        if (request.getPreferences() != null && !request.getPreferences().isEmpty()) {
            sb.append("Preferences: ").append(String.join(", ", request.getPreferences())).append("\n");
        }
        if (request.getAdditionalConstraints() != null && !request.getAdditionalConstraints().isBlank()) {
            sb.append("Constraints: ").append(request.getAdditionalConstraints()).append("\n");
        }
        sb.append("Provide a day‑wise plan with activities, meals, and optional travel tips. Use concise bullet points.");
        return sb.toString();
    }

    /** Extract just the `text` field from the raw SSE JSON line. */
    private String extractChunk(String raw) {
        // Remove SSE prefix if present
        String cleaned = raw.replaceFirst("^data: ", "").trim();
        // Very tolerant: find the first occurrence of "text":"…"
        int startIdx = cleaned.indexOf("\"content\":\"");
        if (startIdx == -1) return cleaned;
        startIdx += 8;
        int endIdx = cleaned.indexOf('"', startIdx);
        if (endIdx == -1) return cleaned;
        return cleaned.substring(startIdx, endIdx);
    }
}
