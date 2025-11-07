package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * PrescriptionController
 *
 * Handles all operations related to prescriptions in the system.
 * Allows doctors to create new prescriptions and retrieve them by appointment ID.
 *
 * Base URL: ${api.path}prescription
 */
@RestController
@RequestMapping("${api.path}" + "prescription")
public class PrescriptionController {

    // --------------------------------------------------------------------
    // Dependencies
    // --------------------------------------------------------------------
    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private Service service;

    // --------------------------------------------------------------------
    // 1. Save Prescription
    // --------------------------------------------------------------------
    @PostMapping("/{token}")
    public ResponseEntity<?> savePrescription(
            @PathVariable String token,
            @RequestBody Prescription prescription) {

        try {
            // Validate token for doctor role
            String validationResult = service.validateToken(token, "doctor");
            if (!validationResult.equals("valid")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", validationResult));
            }

            // Save prescription
            boolean saved = prescriptionService.savePrescription(prescription);

            if (saved) {
                // Optionally update appointment status after prescription is created
                appointmentService.markAsPrescribed(prescription.getAppointmentId());
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("message", "Prescription saved successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Failed to save prescription"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Internal server error"));
        }
    }

    // --------------------------------------------------------------------
    // 2. Get Prescription by Appointment ID
    // --------------------------------------------------------------------
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(
            @PathVariable int appointmentId,
            @PathVariable String token) {

        try {
            // Validate token for doctor role
            String validationResult = service.validateToken(token, "doctor");
            if (!validationResult.equals("valid")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", validationResult));
            }

            // Fetch prescription by appointment ID
            Prescription prescription = prescriptionService.getPrescription(appointmentId);

            if (prescription == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No prescription found for this appointment"));
            }

            return ResponseEntity.ok(prescription);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error retrieving prescription"));
        }
    }
}
