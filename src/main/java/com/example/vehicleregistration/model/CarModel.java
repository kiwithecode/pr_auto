package com.example.vehicleregistration.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "car_models")
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    // Getters and setters
}