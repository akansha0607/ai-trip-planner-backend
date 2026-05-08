package com.travelengine.backend.model;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String time;
    private String description;
    private String location;
    private String costEstimate;
    @ManyToOne
    @JoinColumn(name = "itinerary_day_id")
    private ItineraryDay itineraryDay;
}
