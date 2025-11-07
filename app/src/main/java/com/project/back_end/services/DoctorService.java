package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.DTO.Login; // if needed for validateDoctor input
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DoctorService
 *
 * Business logic for doctor management: save, update, delete, search and availability.
 *
 * Assumptions made:
 * - Doctor has methods getId(), getEmail(), getName(), getSpecialty(), getAvailability()
 *   where getAvailability() returns List<String> of time-slots (e.g. "09:00", "14:30") or similar.
 * - Appointment has getAppointmentTime() (LocalDateTime) and getDoctorId().
 * - TokenService has methods: generateToken(String identifier, String role) and extractEmail(String token).
 *   If your TokenService API differs, adapt calls accordingly.
 */
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository; // used in some flows (optional)
    private final TokenService tokenService;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         PatientRepository patientRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.tokenService = tokenService;
    }

    // ---------------------------
    // Availability helpers
    // ---------------------------

    /**
     * Returns available time slots for a doctor on a date by removing booked slots.
     *
     * @param doctorId doctor id
     * @param date     date for availability
     * @return list of available time strings (same format as doctor's availability list)
     */
    @Transactional(readOnly = true)
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        Optional<Doctor> dOpt = Optional.ofNullable(doctorRepository.findById(doctorId).orElse(null));
        if (dOpt.isEmpty()) return Collections.emptyList();
        Doctor doctor = dOpt.get();

        // Assume doctor.getAvailability() returns List<String> like ["09:00","10:00","14:00", ...]
        List<String> allSlots = Optional.ofNullable(doctor.getAvailability()).orElse(Collections.emptyList());

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctorId, startOfDay, endOfDay);

        // Convert booked appointment times to "HH:mm" strings for comparison
        Set<String> bookedSlots = appointments.stream()
                .map(Appointment::getAppointmentTime)
                .filter(Objects::nonNull)
                .map(dt -> dt.toLocalTime().withSecond(0).withNano(0))
                .map(LocalTime::toString) // "HH:mm:ss" or "HH:mm" depending on toString; normalize to HH:mm
                .map(s -> {
                    // normalization to HH:mm (strip seconds if present)
                    if (s.length() > 5) return s.substring(0, 5);
                    return s;
                })
                .collect(Collectors.toSet());

        List<String> available = allSlots.stream()
                .filter(slot -> {
                    // normalize slot to HH:mm
                    String norm = slot.length() > 5 ? slot.substring(0, 5) : slot;
                    return !bookedSlots.contains(norm);
                })
                .collect(Collectors.toList());

        return available;
    }

    // ---------------------------
    // CRUD & management
    // ---------------------------

    /**
     * Save a new doctor.
     * @param doctor Doctor entity
     * @return 1 on success, -1 if doctor already exists (by email), 0 on error
     */
    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            Doctor existing = doctorRepository.findByEmail(doctor.getEmail());
            if (existing != null) {
                return -1; // already exists
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Update an existing doctor.
     * @param doctor Doctor with updates (must have id)
     * @return 1 on success, -1 if not found, 0 on error
     */
    @Transactional
    public int updateDoctor(Doctor doctor) {
        try {
            Optional<Doctor> opt = doctorRepository.findById(doctor.getId());
            if (opt.isEmpty()) return -1;
            Doctor exist = opt.get();
            // Update fields - adjust according to your model
            exist.setName(doctor.getName());
            exist.setEmail(doctor.getEmail());
            exist.setSpecialty(doctor.getSpecialty());
            exist.setMobile(doctor.getMobile());
            exist.setAvailability(doctor.getAvailability());
            exist.setPassword(doctor.getPassword()); // if password updates allowed; or skip for safety
            doctorRepository.save(exist);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Retrieve all doctors.
     * @return list of doctors
     */
    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    /**
     * Delete a doctor and associated appointments.
     * @param id doctor id
     * @return 1 success, -1 not found, 0 error
     */
    @Transactional
    public int deleteDoctor(long id) {
        try {
            Optional<Doctor> opt = doctorRepository.findById(id);
            if (opt.isEmpty()) return -1;

            // remove appointments first
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ---------------------------
    // Authentication
    // ---------------------------

    /**
     * Validate doctor credentials and return token or error in ResponseEntity map.
     *
     * @param login Login DTO containing identifier (email) and password
     * @return ResponseEntity with token in map on success or error message
     */
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, String>> validateDoctor(com.project.back_end.DTO.Login login) {
        Map<String, String> resp = new HashMap<>();
        try {
            // Using email as identifier for doctor
            Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());
            if (doctor == null) {
                resp.put("message", "Doctor not found");
                return ResponseEntity.status(401).body(resp);
            }

            // NOTE: replace this with a hashed password check if you store hashed passwords
            if (!Objects.equals(doctor.getPassword(), login.getPassword())) {
                resp.put("message", "Invalid credentials");
                return ResponseEntity.status(401).body(resp);
            }

            // Generate token. Assumption: tokenService.generateToken(identifier, role) exists
            String token = tokenService.generateToken(doctor.getEmail(), "doctor");
            resp.put("token", token);
            resp.put("message", "Login successful");
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.put("message", "Internal error");
            return ResponseEntity.status(500).body(resp);
        }
    }

    // ---------------------------
    // Search & filtering
    // ---------------------------

    /**
     * Find doctors by partial name (using repository convenience).
     * @param name search name
     * @return map containing doctors list
     */
    @Transactional(readOnly = true)
    public Map<String, Object> findDoctorByName(String name) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Doctor> doctors = doctorRepository.findByNameLike(name == null ? "" : name);
            result.put("doctors", doctors);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("doctors", Collections.emptyList());
            result.put("message", "Error searching doctors");
            return result;
        }
    }

    /**
     * Combined filter: name + specialty + AM/PM availability
     */
    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Doctor> candidates = doctorRepository
                    .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                            name == null ? "" : name, specialty == null ? "" : specialty);

            List<Doctor> filtered = filterDoctorByTime(candidates, amOrPm);
            result.put("doctors", filtered);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("doctors", Collections.emptyList());
            result.put("message", "Error filtering doctors");
            return result;
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Doctor> candidates = doctorRepository.findByNameLike(name == null ? "" : name);
            List<Doctor> filtered = filterDoctorByTime(candidates, amOrPm);
            result.put("doctors", filtered);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("doctors", Collections.emptyList());
            result.put("message", "Error filtering by name and time");
            return result;
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndSpecility(String name, String specialty) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Doctor> doctors = doctorRepository
                    .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name == null ? "" : name, specialty == null ? "" : specialty);
            result.put("doctors", doctors);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("doctors", Collections.emptyList());
            result.put("message", "Error filtering by name and specialty");
            return result;
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Doctor> candidates = doctorRepository.findBySpecialtyIgnoreCase(specialty == null ? "" : specialty);
            List<Doctor> filtered = filterDoctorByTime(candidates, amOrPm);
            result.put("doctors", filtered);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("doctors", Collections.emptyList());
            result.put("message", "Error filtering by time and specialty");
            return result;
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorBySpecility(String specialty) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty == null ? "" : specialty);
            result.put("doctors", doctors);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("doctors", Collections.emptyList());
            result.put("message", "Error filtering by specialty");
            return result;
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Doctor> all = doctorRepository.findAll();
            List<Doctor> filtered = filterDoctorByTime(all, amOrPm);
            result.put("doctors", filtered);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("doctors", Collections.emptyList());
            result.put("message", "Error filtering doctors by time");
            return result;
        }
    }

    /**
     * Private helper: filters doctor's availability list for AM or PM.
     * This inspects each doctor's availability slots (assumed List<String> like "09:00", "14:30")
     * and retains doctors that have at least one slot in the requested period.
     *
     * @param doctors list of doctors to filter
     * @param amOrPm  "AM" or "PM" (case-insensitive). If null/empty returns original list.
     * @return filtered list
     */
    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        if (amOrPm == null || amOrPm.trim().isEmpty()) return doctors;

        String mode = amOrPm.trim().toUpperCase(Locale.ROOT);
        if (!mode.equals("AM") && !mode.equals("PM")) return doctors;

        return doctors.stream()
                .filter(doc -> {
                    List<String> slots = Optional.ofNullable(doc.getAvailability()).orElse(Collections.emptyList());
                    for (String slot : slots) {
                        // normalize and parse time
                        String s = slot.length() > 5 ? slot.substring(0, 5) : slot;
                        try {
                            LocalTime t = LocalTime.parse(s);
                            if (mode.equals("AM") && t.getHour() < 12) return true;
                            if (mode.equals("PM") && t.getHour() >= 12) return true;
                        } catch (Exception ex) {
                            // ignore parse errors and continue
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }
}
