package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * The Doctor class represents healthcare providers in the Clinic Management System.
 * It includes personal details, medical specialization, years of experience, clinic information,
 * and availability for appointments. This entity is crucial for mapping with appointments
 * and managing doctor data securely.
 */

@Entity // Marks this class as a JPA entity (represents a database table)
@Table(name = "doctors") // Maps this entity to the 'doctors' table in the database
public class Doctor {

    // 1️⃣ ID FIELD
    /**
     * Unique identifier for each doctor.
     * Primary key that auto-increments with each new record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2️⃣ NAME FIELD
    /**
     * Doctor's full name.
     * Must be between 3 and 100 characters and cannot be null.
     */
    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    // 3️⃣ SPECIALTY FIELD
    /**
     * Medical specialty of the doctor (e.g., Cardiologist, Dermatologist).
     * Must be between 3 and 50 characters and cannot be null.
     */
    @NotNull(message = "Specialty cannot be null")
    @Size(min = 3, max = 50, message = "Specialty must be between 3 and 50 characters")
    private String specialty;

    // 4️⃣ YEARS OF EXPERIENCE FIELD
    /**
     * Number of years the doctor has been practicing.
     * Must be a non-negative integer.
     */
    @Min(value = 0, message = "Years of experience must be non-negative")
    private int yearsOfExperience;

    // 5️⃣ CLINIC ADDRESS FIELD
    /**
     * The address of the clinic or hospital where the doctor practices.
     * Can include street, city, and state details.
     */
    @NotNull(message = "Clinic address cannot be null")
    @Size(min = 5, max = 200, message = "Clinic address must be between 5 and 200 characters")
    private String clinicAddress;

    // 6️⃣ RATING FIELD
    /**
     * Average patient rating for the doctor (on a scale of 1.0 to 5.0).
     * Can be updated based on patient feedback.
     */
    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private Double rating;

    // 7️⃣ EMAIL FIELD
    /**
     * Doctor's email address.
     * Must be a valid email format and cannot be null.
     */
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    // 8️⃣ PASSWORD FIELD
    /**
     * Doctor's password for authentication.
     * Must be at least 6 characters long.
     * Hidden from JSON responses for security reasons.
     */
    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Prevents password exposure in API responses
    private String password;

    // 9️⃣ PHONE FIELD
    /**
     * Doctor's contact number.
     * Must contain exactly 10 digits.
     */
    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;

    // 🔟 AVAILABLE TIMES FIELD
    /**
     * List of time slots when the doctor is available.
     * Example: ["09:00-10:00", "10:00-11:00"]
     * Stored as a separate collection in the database.
     */
    @ElementCollection
    private List<String> availableTimes;

    // -------------------- Constructors --------------------

    /** Default constructor — required by JPA. */
    public Doctor() {}

    /** Parameterized constructor for creating Doctor objects easily. */
    public Doctor(Long id, String name, String specialty, int yearsOfExperience, String clinicAddress,
                  Double rating, String email, String password, String phone, List<String> availableTimes) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.yearsOfExperience = yearsOfExperience;
        this.clinicAddress = clinicAddress;
        this.rating = rating;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.availableTimes = availableTimes;
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

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Password is write-only — not included in JSON responses
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

    /**
     * Returns the list of time slots when the doctor is available.
     */
    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    /**
     * Sets the list of time slots when the doctor is available.
     */
    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }
}
