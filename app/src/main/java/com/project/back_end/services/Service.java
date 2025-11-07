package com.project.back_end.services;

import com.project.back_end.model.*;
import com.project.back_end.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Central Service class that manages authentication, validation,
 * and coordination between Admin, Doctor, and Patient entities.
 */
@Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    // ✅ Constructor injection for dependencies
    @Autowired
    public Service(TokenService tokenService,
                   AdminRepository adminRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository,
                   DoctorService doctorService,
                   PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // ---------------------------------------------------------------------
    // 1️⃣ Validate Token
    // ---------------------------------------------------------------------
    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = new HashMap<>();

        try {
            if (!tokenService.validateToken(token, user)) {
                response.put("message", "Invalid or expired token");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            response.put("message", "Token is valid");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error validating token");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------------------------------------------------------------
    // 2️⃣ Validate Admin Login
    // ---------------------------------------------------------------------
    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();

        try {
            Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());

            if (admin == null || !admin.getPassword().equals(receivedAdmin.getPassword())) {
                response.put("message", "Invalid username or password");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            String token = tokenService.generateToken(admin.getUsername());
            response.put("token", token);
            response.put("message", "Login successful");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error during admin validation");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------------------------------------------------------------
    // 3️⃣ Filter Doctors by Name, Specialty, and Time
    // ---------------------------------------------------------------------
    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Doctor> doctors = doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
            response.put("doctors", doctors);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Error filtering doctors");
        }

        return response;
    }

    // ---------------------------------------------------------------------
    // 4️⃣ Validate Appointment Availability
    // ---------------------------------------------------------------------
    public int validateAppointment(Appointment appointment) {
        try {
            Optional<Doctor> doctorOpt = doctorRepository.findById(appointment.getDoctorId());

            if (doctorOpt.isEmpty()) {
                return -1; // Doctor doesn't exist
            }

            Doctor doctor = doctorOpt.get();
            List<Map<String, String>> availableSlots = doctorService.getDoctorAvailability(doctor.getId());

            for (Map<String, String> slot : availableSlots) {
                if (slot.get("startTime").equals(appointment.getStartTime())) {
                    return 1; // Valid time
                }
            }
            return 0; // Time unavailable

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ---------------------------------------------------------------------
    // 5️⃣ Validate Patient Registration (No duplicates)
    // ---------------------------------------------------------------------
    public boolean validatePatient(Patient patient) {
        try {
            Patient existingPatient = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
            return existingPatient == null; // true if patient does not exist
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------------------------
    // 6️⃣ Validate Patient Login
    // ---------------------------------------------------------------------
    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();

        try {
            Patient patient = patientRepository.findByEmail(login.getEmail());

            if (patient == null || !patient.getPassword().equals(login.getPassword())) {
                response.put("message", "Invalid email or password");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            String token = tokenService.generateToken(patient.getEmail());
            response.put("token", token);
            response.put("message", "Login successful");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error during patient login validation");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------------------------------------------------------------
    // 7️⃣ Filter Patient Appointments
    // ---------------------------------------------------------------------
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = tokenService.extractEmail(token); // Extract email from token
            List<Appointment> filteredAppointments = new ArrayList<>();

            if (condition != null && name != null) {
                filteredAppointments = patientService.filterByDoctorAndCondition(email, name, condition);
            } else if (condition != null) {
                filteredAppointments = patientService.filterByCondition(email, condition);
            } else if (name != null) {
                filteredAppointments = patientService.filterByDoctor(email, name);
            } else {
                filteredAppointments = patientService.getAllAppointments(email);
            }

            response.put("appointments", filteredAppointments);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error filtering patient data");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
