package com.project.back_end.models;

/**
 * Login DTO
 * 
 * This class is used to receive user login credentials from the frontend.
 * It encapsulates the identifier (email/username) and password
 * used for authentication.
 * 
 * ⚙️ Notes:
 * - Used in @RequestBody of controller methods.
 * - Not persisted to the database.
 */
public class Login {

    // User identifier — could be an email or username
    private String identifier;

    // User password
    private String password;

    // ✅ Default constructor (required for JSON deserialization)
    public Login() {}

    // ✅ Parameterized constructor (optional convenience)
    public Login(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    // ✅ Getters and Setters
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
