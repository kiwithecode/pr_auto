package com.example.vehicleregistration.repository;

import com.example.vehicleregistration.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, UUID> {
}