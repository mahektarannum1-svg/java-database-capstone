package com.project.back_end.services;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.model.Admin;
import com.project.back_end.model.Doctor;
import com.project.back_end.model.Patient;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * TokenService handles JWT generation, extraction, and validation
 * for secure authentication of Admins, Doctors, and Patients.
 */
@Component
public class TokenService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    // ✅ Secret key from application.properties (jwt.secret)
    @Value("${jwt.secret}")
    private String jwtSecret;

    // ✅ Constructor Injection for Repositories
    public TokenService(AdminRepository adminRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // ---------------------------------------------------------------------
    // 1️⃣ Get Signing Key
    // ---------------------------------------------------------------------
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ---------------------------------------------------------------------
    // 2️⃣ Generate Token
    // ---------------------------------------------------------------------
    public String generateToken(String identifier) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000); // 7 days validity

        return Jwts.builder()
                .setSubject(identifier) // subject = user's email or username
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    // ---------------------------------------------------------------------
    // 3️⃣ Extract Identifier (Email or Username)
    // ---------------------------------------------------------------------
    public String extractIdentifier(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            // Invalid or expired token
            return null;
        }
    }

    // ---------------------------------------------------------------------
    // 4️⃣ Validate Token
    // ---------------------------------------------------------------------
    public boolean validateToken(String token, String userType) {
        try {
            String identifier = extractIdentifier(token);
            if (identifier == null) return false;

            switch (userType.toLowerCase()) {
                case "admin":
                    Admin admin = adminRepository.findByUsername(identifier);
                    return admin != null;

                case "doctor":
                    Doctor doctor = doctorRepository.findByEmail(identifier);
                    return doctor != null;

                case "patient":
                    Patient patient = patientRepository.findByEmail(identifier);
                    return patient != null;

                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
