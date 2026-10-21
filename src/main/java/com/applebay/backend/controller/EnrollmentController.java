
package com.applebay.backend.controller;

import com.applebay.backend.entity.Enrollment;
import com.applebay.backend.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createEnrollment(
            @RequestBody Map<String, Long> request) {

        Long studentId = request.get("studentId");
        Long courseId = request.get("courseId");

        if (studentId == null || courseId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "studentId and courseId are required"));
        }

        try {
            Enrollment enrollment = enrollmentService.createEnrollment(
                    studentId, courseId);

            return ResponseEntity.ok(enrollment);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEnrollment(
            @PathVariable Long id) {

        enrollmentService.deleteEnrollment(id);

        return ResponseEntity.ok(
                Map.of("message", "Enrollment deleted successfully"));
    }
}
