package com.project.back_end.services;

import com.project.back_end.model.Appointment;
import com.project.back_end.model.Doctor;
import com.project.back_end.model.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    /**
     * Book a new appointment.
     * @param appointment Appointment object to book
     * @return 1 if success, 0 if failed
     */
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Update an existing appointment.
     * @param appointment Appointment object with updated details
     * @return ResponseEntity with a message indicating success or failure
     */
    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> existing = appointmentRepository.findById(appointment.getId());
        if (existing.isEmpty()) {
            response.put("message", "Appointment not found!");
            return ResponseEntity.badRequest().body(response);
        }

        // Optional: Add validation logic (e.g., doctor availability)
        Appointment existingAppointment = existing.get();
        existingAppointment.setAppointmentTime(appointment.getAppointmentTime());
        existingAppointment.setStatus(appointment.getStatus());
        existingAppointment.setDescription(appointment.getDescription());
        existingAppointment.setDoctorId(appointment.getDoctorId());
        existingAppointment.setPatientId(appointment.getPatientId());

        appointmentRepository.save(existingAppointment);

        response.put("message", "Appointment updated successfully!");
        return ResponseEntity.ok(response);
    }

    /**
     * Cancel an existing appointment.
     * @param id ID of appointment to cancel
     * @param token Authorization token to verify the patient
     * @return ResponseEntity with message
     */
    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isEmpty()) {
            response.put("message", "Appointment not found!");
            return ResponseEntity.badRequest().body(response);
        }

        // Verify that the token corresponds to the same patient
        String email = tokenService.extractEmail(token);
        Patient patient = patientRepository.findByEmail(email);
        if (patient == null || patient.getId() != appointment.get().getPatientId()) {
            response.put("message", "Unauthorized cancellation attempt!");
            return ResponseEntity.status(403).body(response);
        }

        appointmentRepository.delete(appointment.get());
        response.put("message", "Appointment cancelled successfully!");
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve list of appointments for a doctor on a specific date, optionally filtered by patient name.
     * @param pname Patient name to filter by
     * @param date Appointment date
     * @param token Authorization token
     * @return Map containing appointments list
     */
    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
        Map<String, Object> result = new HashMap<>();

        String email = tokenService.extractEmail(token);
        Doctor doctor = doctorRepository.findByEmail(email);

        if (doctor == null) {
            result.put("message", "Doctor not found!");
            return result;
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctor.getId(), startOfDay, endOfDay);

        if (pname != null && !pname.isEmpty()) {
            appointments = appointments.stream()
                    .filter(a -> {
                        Optional<Patient> p = patientRepository.findById(a.getPatientId());
                        return p.isPresent() && p.get().getName().toLowerCase().contains(pname.toLowerCase());
                    })
                    .toList();
        }

        result.put("appointments", appointments);
        return result;
    }

    /**
     * Change the status of an appointment.
     * @param id Appointment ID
     * @param status New status string
     * @return ResponseEntity with message
     */
    @Transactional
    public ResponseEntity<Map<String, String>> changeStatus(long id, String status) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isEmpty()) {
            response.put("message", "Appointment not found!");
            return ResponseEntity.badRequest().body(response);
        }

        Appointment existingAppointment = appointment.get();
        existingAppointment.setStatus(status);
        appointmentRepository.save(existingAppointment);

        response.put("message", "Appointment status updated successfully!");
        return ResponseEntity.ok(response);
    }
}
