package com.project.back_end.models;

// Importing required libraries for database mapping, validation, and JSON control
import jakarta.persistence.*;                     // For @Entity, @Id, @GeneratedValue, etc.
import jakarta.validation.constraints.*;          // For validation annotations like @NotNull, @Email, @Size, etc.
import com.fasterxml.jackson.annotation.JsonProperty; // To hide sensitive data like password from JSON responses

/**
 * The Patient class represents users who book appointments and receive treatment.
 * It includes personal details like name, email, phone number, and address.
 * This entity forms a core part of the Clinic Management System, linking to appointments and prescriptions.
 */

@Entity // Marks this class as a JPA entity (maps to a table in the database)
@Table(name = "patients") // Defines the table name in the database
public class Patient {

    // 1️⃣ ID FIELD
    /**
     * Unique identifier for each patient.
     * Auto-generated primary key for the 'patients' table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2️⃣ NAME FIELD
    /**
     * Patient's full name.
     * Must be between 3 and 100 characters and cannot be null.
     */
    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    // 3️⃣ EMAIL FIELD
    /**
     * Patient's email address.
     * Must be valid and unique for each patient.
     */
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    // 4️⃣ PASSWORD FIELD
    /**
     * Password for patient authentication.
     * Must be at least 6 characters long.
     * Hidden from JSON responses for security.
     */
    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // 5️⃣ PHONE FIELD
    /**
     * Patient's phone number.
     * Must be exactly 10 digits and not null.
     */
    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phone;

    // 6️⃣ ADDRESS FIELD
    /**
     * Patient's address.
     * Required and limited to 255 characters.
     */
    @NotNull(message = "Address cannot be null")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    // -------------------- Constructors --------------------

    /** Default constructor — required by JPA. */
    public Patient() {}

    /** Parameterized constructor for easy object creation. */
    public Patient(Long id, String name, String email, String password, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
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

    // Password is write-only (will not be exposed in JSON)
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
}
