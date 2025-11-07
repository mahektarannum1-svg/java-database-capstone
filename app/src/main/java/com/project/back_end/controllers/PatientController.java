package com.project.back_end.controllers;

import com.project.back_end.entities.Patient;
import com.project.back_end.entities.Login;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * PatientController
 *
 * This REST controller manages all operations related to the Patient entity,
 * including registration, login, fetching patient details, and managing or filtering
 * patient appointments.
 *
 * Base URL: /patient
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    // --------------------------------------------------------------------
    // Dependencies
    // --------------------------------------------------------------------
    @Autowired
    private PatientService patientService;

    @Autowired
    private Service service;

    // --------------------------------------------------------------------
    // 1. Get Patient Details
    // --------------------------------------------------------------------
    @GetMapping("/{token}")
    public ResponseEntity<?> getPatientDetails(@PathVariable String token) {
        try {
            String validationResult = service.validateToken(token, "patient");
            if (!validationResult.equals("valid")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", validationResult));
            }

            Patient patient = patientService.getPatientDetails(token);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Patient not found"));
            }

            return ResponseEntity.ok(patient);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Internal server error"));
        }
    }

    // --------------------------------------------------------------------
    // 2. Create a New Patient (Signup)
    // --------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        try {
            String validation = patientService.validateNewPatient(patient);
            if (validation.equals("exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Patient with email id or phone no already exist"));
            }

            boolean created = patientService.createPatient(patient);
            if (created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("message", "Signup successful"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Internal server error"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Internal server error"));
        }
    }

    // --------------------------------------------------------------------
    // 3. Patient Login
    // --------------------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        try {
            Map<String, Object> response = service.validatePatientLogin(login);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }
    }

    // --------------------------------------------------------------------
    // 4. Get Patient Appointments
    // --------------------------------------------------------------------
    @GetMapping("/{id}/{token}")
    public ResponseEntity<?> getPatientAppointments(@PathVariable int id, @PathVariable String token) {
        try {
            String validationResult = service.validateToken(token, "patient");
            if (!validationResult.equals("valid")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", validationResult));
            }

            var appointments = patientService.getPatientAppointment(id);
            return ResponseEntity.ok(Map.of("appointments", appointments));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error fetching appointments"));
        }
    }

    // --------------------------------------------------------------------
    // 5. Filter Patient Appointments
    // --------------------------------------------------------------------
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterPatientAppointments(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {
        try {
            String validationResult = service.validateToken(token, "patient");
            if (!validationResult.equals("valid")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", validationResult));
            }

            var filteredAppointments = service.filterPatient(condition, name);
            return ResponseEntity.ok(Map.of("filteredAppointments", filteredAppointments));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error filtering appointments"));
        }
    }
}
