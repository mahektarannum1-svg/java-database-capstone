package com.project.back_end.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * AppointmentDTO
 * 
 * This Data Transfer Object (DTO) is used to send structured appointment data
 * between the backend and the frontend. It decouples internal entity models
 * from the exposed API response.
 */
public class AppointmentDTO {

    // Core Fields
    private Long id;                     // Unique identifier for the appointment
    private Long doctorId;               // Doctor ID
    private String doctorName;           // Doctor's full name
    private Long patientId;              // Patient ID
    private String patientName;          // Patient's full name
    private String patientEmail;         // Patient's email
    private String patientPhone;         // Patient's contact number
    private String patientAddress;       // Patient's residential address
    private LocalDateTime appointmentTime; // Full date & time of appointment
    private int status;                  // Appointment status (e.g., 0 = scheduled, 1 = completed)

    // Derived Fields
    private LocalDate appointmentDate;   // Extracted date from appointmentTime
    private LocalTime appointmentTimeOnly; // Extracted time from appointmentTime
    private LocalDateTime endTime;       // appointmentTime + 1 hour

    /**
     * Constructor to initialize the AppointmentDTO.
     * Automatically computes date, time, and endTime fields.
     */
    public AppointmentDTO(
            Long id,
            Long doctorId,
            String doctorName,
            Long patientId,
            String patientName,
            String patientEmail,
            String patientPhone,
            String patientAddress,
            LocalDateTime appointmentTime,
            int status
    ) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhone = patientPhone;
        this.patientAddress = patientAddress;
        this.appointmentTime = appointmentTime;
        this.status = status;

        // Compute derived fields
        if (appointmentTime != null) {
            this.appointmentDate = appointmentTime.toLocalDate();
            this.appointmentTimeOnly = appointmentTime.toLocalTime();
            this.endTime = appointmentTime.plusHours(1);
        }
    }

    // ✅ Getters
    public Long getId() {
        return id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTimeOnly() {
        return appointmentTimeOnly;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
