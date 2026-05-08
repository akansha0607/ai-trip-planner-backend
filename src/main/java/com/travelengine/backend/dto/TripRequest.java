package com.travelengine.backend.dto;

import lombok.Data;
import java.util.List;
@Data
public class TripRequest {
    private String destination;
    private int durationDays;
    private String budget; // e.g., "Economy", "Standard", "Luxury"
    private List<String> preferences; // e.g., "Adventure", "Food", "Culture"
    private String additionalConstraints; // e.g., "Vegetarian only", "Wheelchair accessible"
}

