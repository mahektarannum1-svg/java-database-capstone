package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import com.project.back_end.service.Service; // Replace with your actual service class package
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private Service service; // This service should contain validateToken(token, role)

    /**
     * Admin Dashboard Access
     * Validates the admin token and returns the admin dashboard view if valid
     */
    @GetMapping("/adminDashboard/{token}")
    public Object adminDashboard(@PathVariable("token") String token) {
        Map<String, Object> validationResult = service.validateToken(token, "admin");

        // If the returned map is empty → token valid
        if (validationResult.isEmpty()) {
            return "admin/adminDashboard"; // Thymeleaf template path
        } else {
            // Token invalid → redirect to login page
            return new RedirectView("http://localhost:8080");
        }
    }

    /**
     * Doctor Dashboard Access
     * Validates the doctor token and returns the doctor dashboard view if valid
     */
    @GetMapping("/doctorDashboard/{token}")
    public Object doctorDashboard(@PathVariable("token") String token) {
        Map<String, Object> validationResult = service.validateToken(token, "doctor");

        if (validationResult.isEmpty()) {
            return "doctor/doctorDashboard"; // Thymeleaf template path
        } else {
            return new RedirectView("http://localhost:8080");
        }
    }
}
