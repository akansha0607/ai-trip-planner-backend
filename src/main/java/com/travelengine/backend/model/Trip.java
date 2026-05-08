package com.travelengine.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
@Data
@Entity
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destination;
    private int durationDays;
    private String budget;
    private String status; // PLANNING, UPCOMING, COMPLETED
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<ItineraryDay> itineraryDays;
}

