package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AdminController handles login requests for the Admin user.
 * It validates credentials and issues JWT tokens upon successful authentication.
 */
@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

    private final AdminService adminService;

    // ✅ Constructor Injection
    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ---------------------------------------------------------------------
    // 🟩 POST: /api/v1/admin/login
    // ---------------------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Admin admin) {
        // Delegates login validation to the service layer
        return adminService.validateAdmin(admin);
    }
}
