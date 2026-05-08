package com.travelengine.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class TripDto {
    private String destination;
    private int durationDays;
    private String budget;
    private List<String> preferences;
    private String constraints;
    private String itinerary; // full generated itinerary text
    private LocalDateTime createdAt;
    private Long userId; // optional linking to User
}
