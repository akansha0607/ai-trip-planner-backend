package com.travelengine.backend.controller;

import com.travelengine.backend.dto.TripRequest;
import com.travelengine.backend.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://ai-trip-frontend-628807958232.asia-south1.run.app"
})
public class TripPlannerController {

    private final TripService tripService;

    @PostMapping(value = "/plan/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamTripPlan(@RequestBody TripRequest request) {
        return tripService.planTripStream(request);
    }
}

