package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * The Appointment class represents a scheduled meeting between a Doctor and a Patient.
 * It stores details such as appointment time, reason for visit, notes, and links to Doctor and Patient entities.
 * This is one of the core models of the Clinic Management System.
 */

@Entity // Marks this class as a database entity
@Table(name = "appointments") // Maps this entity to the 'appointments' table in the database
public class Appointment {

    /**
     * Unique ID for each appointment.
     * Marked as the primary key and auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The doctor assigned to this appointment.
     * Represents a many-to-one relationship (many appointments can belong to one doctor).
     * Cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false) // Foreign key to Doctor table
    @NotNull(message = "Doctor cannot be null")
    private Doctor doctor;

    /**
     * The patient who booked this appointment.
     * Represents a many-to-one relationship (many appointments can belong to one patient).
     * Cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false) // Foreign key to Patient table
    @NotNull(message = "Patient cannot be null")
    private Patient patient;

    /**
     * Date and time of the appointment.
     * Must always be in the future to avoid past scheduling.
     */
    @NotNull(message = "Appointment time cannot be null")
    @Future(message = "Appointment time must be in the future")
    private LocalDateTime appointmentTime;

    /**
     * Reason for the visit (e.g., "Regular Checkup", "Fever", "Consultation").
     * Helps doctors prepare in advance.
     */
    @NotNull(message = "Reason for visit cannot be null")
    @Size(min = 3, max = 100, message = "Reason for visit must be between 3 and 100 characters")
    private String reasonForVisit;

    /**
     * Additional notes related to the appointment (e.g., patient’s symptoms or doctor’s comments).
     * Optional field for extra context.
     */
    @Column(length = 500)
    private String notes;

    /**
     * Status of the appointment.
     * 0 = Scheduled
     * 1 = Completed
     * 2 = Cancelled
     */
    @NotNull(message = "Status cannot be null")
    private int status;

    // -------------------- Constructors --------------------

    /** Default constructor — required by JPA. */
    public Appointment() {}

    /** Parameterized constructor for easy object creation. */
    public Appointment(Long id, Doctor doctor, Patient patient, LocalDateTime appointmentTime,
                       String reasonForVisit, String notes, int status) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.reasonForVisit = reasonForVisit;
        this.notes = notes;
        this.status = status;
    }

    // -------------------- Helper Methods --------------------

    /**
     * Returns the end time of the appointment.
     * Each appointment is assumed to last 1 hour.
     * This method is not stored in the database.
     */
    @Transient // Excluded from persistence (not stored in DB)
    public LocalDateTime getEndTime() {
        return appointmentTime.plusHours(1);
    }

    /**
     * Returns only the date part of the appointment.
     * Useful for displaying or filtering by date.
     */
    @Transient
    public LocalDate getAppointmentDate() {
        return appointmentTime.toLocalDate();
    }

    /**
     * Returns only the time portion of the appointment.
     * Useful for UI display or scheduling comparison.
     */
    @Transient
    public LocalTime getAppointmentTimeOnly() {
        return appointmentTime.toLocalTime();
    }

    // -------------------- Getters and Setters --------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
