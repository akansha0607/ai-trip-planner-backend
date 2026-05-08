package com.travelengine.backend.service;
import com.travelengine.backend.dto.TripRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
@Service
@RequiredArgsConstructor
public class TripService {
    private final GeminiService geminiService;
    // Calls Gemini API and streams response chunks
    public Flux<String> planTripStream(TripRequest request) {
        return geminiService.generateItineraryStream(request);
    }
}