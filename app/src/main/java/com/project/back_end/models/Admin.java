package com.project.back_end.models;

// Importing required libraries for JPA (database mapping), validation, and JSON control
import jakarta.persistence.*;                     // For @Entity, @Id, @GeneratedValue, etc.
import jakarta.validation.constraints.NotNull;    // For field validation
import com.fasterxml.jackson.annotation.JsonProperty; // For controlling JSON visibility

/**
 * The Admin class represents the administrator of the Clinic Management System.
 * Admins have access to manage the backend portal, including users, appointments, and system configurations.
 * This entity maps to the 'admins' table in the database.
 */

@Entity // Marks this class as a JPA entity that maps to a database table
@Table(name = "admins") // Specifies the database table name as 'admins'
public class Admin {

    /**
     * Unique ID for each admin.
     * Marked as the primary key and auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates unique ID values
    private Long id;

    /**
     * Username of the admin.
     * This field cannot be null and is used for authentication.
     */
    @NotNull(message = "username cannot be null")
    private String username;

    /**
     * Password of the admin.
     * This field cannot be null and is marked as WRITE_ONLY to hide it in JSON responses.
     */
    @NotNull(message = "password cannot be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Password will not appear in API responses
    private String password;

    /**
     * Default constructor — required by JPA.
     * It allows Spring Boot to instantiate the entity automatically.
     */
    public Admin() {}

    /**
     * Parameterized constructor for creating Admin objects easily.
     */
    public Admin(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // -------------------- Getters and Setters --------------------

    /** Returns the admin ID. */
    public Long getId() {
        return id;
    }

    /** Sets the admin ID. */
    public void setId(Long id) {
        this.id = id;
    }

    /** Returns the admin username. */
    public String getUsername() {
        return username;
    }

    /** Sets the admin username. */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the admin password.
     * Although it’s accessible within the backend, it will never be returned in JSON output.
     */
    public String getPassword() {
        return password;
    }

    /** Sets the admin password. */
    public void setPassword(String password) {
        this.password = password;
    }
}
