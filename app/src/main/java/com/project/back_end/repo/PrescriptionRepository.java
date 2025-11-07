package com.project.back_end.repo;

import com.project.back_end.model.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PrescriptionRepository
 *
 * Provides CRUD operations and custom query methods for Prescription documents stored in MongoDB.
 * This repository allows fetching prescriptions linked to a specific appointment.
 */
@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    /**
     * Find all prescriptions associated with a specific appointment ID.
     *
     * @param appointmentId the ID of the appointment
     * @return a list of Prescription documents linked to that appointment
     */
    List<Prescription> findByAppointmentId(Long appointmentId);
}
