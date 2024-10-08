package com.example.vehicleregistration.controller;

import com.example.vehicleregistration.model.Vehicle;
import com.example.vehicleregistration.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.createVehicle(vehicle));
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable UUID id) {
        return vehicleService.getVehicle(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable UUID id, @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable UUID id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/can-circulate")
    public ResponseEntity<Boolean> canCirculate(
            @PathVariable UUID id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(vehicleService.canCirculate(id, date));
    }

    @GetMapping("/can-circulate-by-placa")
    public ResponseEntity<Boolean> canCirculateByPlaca(
            @RequestParam String placa,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(vehicleService.canCirculateByPlaca(placa, date));
    }

    @GetMapping("/circulation-schedule")
    public ResponseEntity<Map<String, Boolean>> getCirculationSchedule(@RequestParam String placa) {
        return ResponseEntity.ok(vehicleService.getCirculationSchedule(placa));
    }

    @GetMapping("/by-placa/{placa}")
    public ResponseEntity<Vehicle> getVehicleByPlaca(@PathVariable String placa) {
        return vehicleService.getVehicleByPlaca(placa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/circulation-schedule-supabase/{placa}")
    public ResponseEntity<List<Map<String, Object>>> getCirculationScheduleFromSupabase(@PathVariable String placa) {
        return ResponseEntity.ok(vehicleService.getCirculationScheduleFromSupabase(placa));
    }
}