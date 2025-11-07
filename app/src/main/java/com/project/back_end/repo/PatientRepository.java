package com.project.back_end.repo;

import com.project.back_end.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PatientRepository
 *
 * Provides CRUD and custom query methods for the Patient entity.
 * Supports lookups by email and phone for authentication or validation.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Find a patient by their email address.
     *
     * @param email Patient’s email
     * @return Patient object if found, otherwise null
     */
    Patient findByEmail(String email);

    /**
     * Find a patient by either their email or phone number.
     *
     * @param email Patient’s email
     * @param phone Patient’s phone number
     * @return Patient object if found, otherwise null
     */
    Patient findByEmailOrPhone(String email, String phone);
}
