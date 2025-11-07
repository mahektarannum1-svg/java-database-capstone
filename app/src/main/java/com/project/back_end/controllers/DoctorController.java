package com.project.back_end.controllers;

import com.project.back_end.model.Doctor;
import com.project.back_end.model.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * DoctorController handles all endpoints related to Doctor operations —
 * such as managing doctors, validating logins, and checking availability.
 */
@RestController
@RequestMapping("${api.path}" + "doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final Service service;

    // ✅ Constructor injection
    @Autowired
    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    // ---------------------------------------------------------------------
    // 🟩 1. Get Doctor Availability
    // ---------------------------------------------------------------------
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        // Validate token
        if (!service.validateToken(token, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token."));
        }

        try {
            return doctorService.getDoctorAvailability(doctorId, date);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching doctor availability."));
        }
    }

    // ---------------------------------------------------------------------
    // 🟦 2. Get List of Doctors
    // ---------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {
        try {
            return doctorService.getDoctors();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching doctor list."));
        }
    }

    // ---------------------------------------------------------------------
    // 🟨 3. Add New Doctor
    // ---------------------------------------------------------------------
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(
            @PathVariable String token,
            @RequestBody Doctor doctor) {

        // Only admins can add doctors
        if (!service.validateToken(token, "admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: invalid or expired token."));
        }

        try {
            return doctorService.saveDoctor(doctor);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Doctor already exists."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Some internal error occurred."));
        }
    }

    // ---------------------------------------------------------------------
    // 🟪 4. Doctor Login
    // ---------------------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        try {
            return doctorService.validateDoctor(login);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error validating login credentials."));
        }
    }

    // ---------------------------------------------------------------------
    // 🟧 5. Update Doctor Details
    // ---------------------------------------------------------------------
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @PathVariable String token,
            @RequestBody Doctor doctor) {

        // Only admins can update doctors
        if (!service.validateToken(token, "admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: invalid or expired token."));
        }

        try {
            return doctorService.updateDoctor(doctor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Doctor not found."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Some internal error occurred."));
        }
    }

    // ---------------------------------------------------------------------
    // 🟥 6. Delete Doctor
    // ---------------------------------------------------------------------
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable Long id,
            @PathVariable String token) {

        // Only admins can delete doctors
        if (!service.validateToken(token, "admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: invalid or expired token."));
        }

        try {
            return doctorService.deleteDoctor(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Doctor not found with ID: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Some internal error occurred."));
        }
    }

    // ---------------------------------------------------------------------
    // 🟫 7. Filter Doctors
    // ---------------------------------------------------------------------
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filterDoctors(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        try {
            return service.filterDoctor(name, time, speciality);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error filtering doctors."));
        }
    }
}
