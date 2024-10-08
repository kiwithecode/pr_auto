package com.example.vehicleregistration.service;

import com.example.vehicleregistration.model.Vehicle;
import com.example.vehicleregistration.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, JdbcTemplate jdbcTemplate) {
        this.vehicleRepository = vehicleRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicle(UUID id) {
        return vehicleRepository.findById(id);
    }

    public Vehicle updateVehicle(UUID id, Vehicle vehicle) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehicle not found");
        }
        vehicle.setId(id);
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(UUID id) {
        vehicleRepository.deleteById(id);
    }

    public boolean canCirculate(UUID id, LocalDate date) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        return canCirculateByPlaca(vehicle.getPlaca(), date);
    }

    public boolean canCirculateByPlaca(String placa, LocalDate date) {
        int lastDigit = Character.getNumericValue(placa.charAt(placa.length() - 1));
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return true;
        }

        return switch (dayOfWeek) {
            case MONDAY -> !(lastDigit == 1 || lastDigit == 2);
            case TUESDAY -> !(lastDigit == 3 || lastDigit == 4);
            case WEDNESDAY -> !(lastDigit == 5 || lastDigit == 6);
            case THURSDAY -> !(lastDigit == 7 || lastDigit == 8);
            case FRIDAY -> !(lastDigit == 9 || lastDigit == 0);
            default -> true;
        };
    }

    public Map<String, Boolean> getCirculationSchedule(String placa) {
        int lastDigit = Character.getNumericValue(placa.charAt(placa.length() - 1));
        Map<String, Boolean> schedule = new LinkedHashMap<>();

        schedule.put("Lunes", !(lastDigit == 1 || lastDigit == 2));
        schedule.put("Martes", !(lastDigit == 3 || lastDigit == 4));
        schedule.put("Miércoles", !(lastDigit == 5 || lastDigit == 6));
        schedule.put("Jueves", !(lastDigit == 7 || lastDigit == 8));
        schedule.put("Viernes", !(lastDigit == 9 || lastDigit == 0));
        schedule.put("Sábado", true);
        schedule.put("Domingo", true);

        return schedule;
    }

    public Optional<Vehicle> getVehicleByPlaca(String placa) {
        return vehicleRepository.findByPlaca(placa);
    }

    public List<Map<String, Object>> getCirculationScheduleFromSupabase(String placa) {
        String sql = "SELECT * FROM get_circulation_schedule(?)";
        return jdbcTemplate.queryForList(sql, placa);
    }
}