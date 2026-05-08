package com.travelengine.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String role; // ROLE_USER, ROLE_ADMIN
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Trip> trips;
}
