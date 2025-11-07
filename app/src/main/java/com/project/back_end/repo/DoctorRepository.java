package com.project.back_end.repo;

import com.project.back_end.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * DoctorRepository
 *
 * Provides CRUD and custom query methods for the Doctor entity.
 * Supports search operations by email, name, and specialty.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /**
     * Find a doctor by their email.
     *
     * @param email Doctor’s email address
     * @return Doctor object if found, otherwise null
     */
    Doctor findByEmail(String email);

    /**
     * Find doctors whose names match the given pattern (partial name search).
     * Uses LIKE and CONCAT for flexible pattern matching.
     *
     * @param name Partial name to search
     * @return List of doctors matching the name pattern
     */
    @Query("SELECT d FROM Doctor d WHERE d.name LIKE CONCAT('%', :name, '%')")
    List<Doctor> findByNameLike(String name);

    /**
     * Find doctors by partial name (case-insensitive) and specialty (case-insensitive).
     * Useful for filtered searches in UI search bars or dashboards.
     *
     * @param name Doctor’s name (partial)
     * @param specialty Doctor’s specialty (exact, but case-insensitive)
     * @return List of doctors matching both filters
     */
    @Query("SELECT d FROM Doctor d " +
           "WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "AND LOWER(d.specialty) = LOWER(:specialty)")
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

    /**
     * Find doctors by specialty, ignoring case sensitivity.
     *
     * @param specialty Doctor’s specialty
     * @return List of doctors in that specialty
     */
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
