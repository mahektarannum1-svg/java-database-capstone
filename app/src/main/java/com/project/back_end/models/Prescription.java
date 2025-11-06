package com.project.back_end.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * The Prescription class represents a MongoDB document that stores 
 * details of prescriptions given to patients during appointments.
 * It includes patient details, appointment reference, prescribed medication, 
 * dosage, and optional doctor notes.
 */

@Document(collection = "prescriptions") // Maps this class to the 'prescriptions' collection in MongoDB
public class Prescription {

    /**
     * Unique ID for each prescription (MongoDB automatically generates this value).
     */
    @Id
    private String id;

    /**
     * Name of the patient receiving the prescription.
     * Must be between 3 and 100 characters long.
     */
    @NotNull(message = "Patient name cannot be null")
    @Size(min = 3, max = 100, message = "Patient name must be between 3 and 100 characters")
    private String patientName;

    /**
     * ID of the associated appointment where this prescription was issued.
     * This field links the prescription to a specific appointment record.
     */
    @NotNull(message = "Appointment ID cannot be null")
    private Long appointmentId;

    /**
     * Name of the prescribed medication.
     * Must be between 3 and 100 characters long.
     */
    @NotNull(message = "Medication name cannot be null")
    @Size(min = 3, max = 100, message = "Medication name must be between 3 and 100 characters")
    private String medication;

    /**
     * Dosage details for the prescribed medication.
     * Must be between 3 and 20 characters long.
     */
    @NotNull(message = "Dosage cannot be null")
    @Size(min = 3, max = 20, message = "Dosage must be between 3 and 20 characters")
    private String dosage;

    /**
     * Optional notes provided by the doctor.
     * Maximum of 200 characters allowed.
     */
    @Size(max = 200, message = "Doctor notes cannot exceed 200 characters")
    private String doctorNotes;

    // -------------------- Constructors --------------------

    /** Default constructor (required by Spring and MongoDB). */
    public Prescription() {}

    /**
     * Parameterized constructor for easier object creation.
     */
    public Prescription(String patientName, Long appointmentId, String medication, String dosage, String doctorNotes) {
        this.patientName = patientName;
        this.appointmentId = appointmentId;
        this.medication = medication;
        this.dosage = dosage;
        this.doctorNotes = doctorNotes;
    }

    // -------------------- Getters and Setters --------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }
}
