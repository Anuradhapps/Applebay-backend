
package com.applebay.backend.controller;

import com.applebay.backend.entity.Enrollment;
import com.applebay.backend.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(
            EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public Enrollment getEnrollmentById(
            @PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id);
    }

    @PostMapping
    public Enrollment createEnrollment(
            @RequestBody Map<String, Long> request) {

        Long studentId = request.get("studentId");
        Long courseId = request.get("courseId");

        if (studentId == null || courseId == null) {
            throw new IllegalArgumentException(
                    "studentId and courseId are required");
        }

        return enrollmentService.createEnrollment(
                studentId, courseId);
    }

    @DeleteMapping("/{id}")
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
    }
}
