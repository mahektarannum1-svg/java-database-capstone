package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.back_end.models.Admin;

/**
 * AdminRepository
 *
 * This interface handles database operations for the Admin entity.
 * It extends JpaRepository to inherit CRUD operations and defines
 * a custom query method for finding admins by username.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * Find an admin by their username.
     *
     * @param username the username of the admin
     * @return the Admin entity matching the given username, or null if not found
     */
    Admin findByUsername(String username);
}
