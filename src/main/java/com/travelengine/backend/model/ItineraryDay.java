package com.travelengine.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
@Data
@Entity
public class ItineraryDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int dayNumber;
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;
    @OneToMany(mappedBy = "itineraryDay", cascade = CascadeType.ALL)
    private List<Activity> activities;
}

