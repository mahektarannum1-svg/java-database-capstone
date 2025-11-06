package com.project.back_end.models;

// Importing required libraries for database mapping, validation, and JSON control
import jakarta.persistence.*;                     
import jakarta.validation.constraints.*;          
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate; // For dateOfBirth field

/**
 * The Patient class represents users who book appointments and receive treatment.
 * It includes personal details like name, contact information, and optional insurance details.
 * This entity forms a key part of the Clinic Management System.
 */

@Entity // Marks this class as a JPA entity (maps to a table in the database)
@Table(name = "patients") // Defines the table name in the database
public class Patient {

    // 1️⃣ ID FIELD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2️⃣ NAME FIELD
    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    // 3️⃣ EMAIL FIELD
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    // 4️⃣ PASSWORD FIELD
    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // 5️⃣ PHONE FIELD
    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phone;

    // 6️⃣ ADDRESS FIELD
    @NotNull(message = "Address cannot be null")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    // 7️⃣ DATE OF BIRTH FIELD
    /**
     * Represents the patient's date of birth.
     * Must be a valid date in the past.
     */
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // 8️⃣ EMERGENCY CONTACT FIELD
    /**
     * Emergency contact number of the patient.
     * Must be exactly 10 digits.
     */
    @Pattern(regexp = "\\d{10}", message = "Emergency contact must be exactly 10 digits")
    private String emergencyContact;

    // 9️⃣ INSURANCE PROVIDER FIELD
    /**
     * Name of the patient's insurance provider.
     * Optional field, limited to 100 characters.
     */
    @Size(max = 100, message = "Insurance provider name cannot exceed 100 characters")
    private String insuranceProvider;

    // -------------------- Constructors --------------------

    /** Default constructor — required by JPA. */
    public Patient() {}

    /** Parameterized constructor for easy object creation. */
    public Patient(Long id, String name, String email, String password, String phone,
                   String address, LocalDate dateOfBirth, String emergencyContact, String insuranceProvider) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.emergencyContact = emergencyContact;
        this.insuranceProvider = insuranceProvider;
    }

    // -------------------- Getters and Setters --------------------

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }
    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }
}
