
package com.applebay.backend.controller;

import com.applebay.backend.entity.Attendance;
import com.applebay.backend.service.AttendanceService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(
            AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    @GetMapping("/student/{studentId}")
    public List<Attendance> getStudentAttendance(
            @PathVariable Long studentId) {
        return attendanceService.getStudentAttendance(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<Attendance> getCourseAttendance(
            @PathVariable Long courseId,
            @RequestParam LocalDate date) {
        return attendanceService.getCourseAttendance(
                courseId, date);
    }

    @PostMapping
    public Attendance markAttendance(
            @RequestBody Map<String, String> request) {

        String enrollmentIdValue = request.get("enrollmentId");
        String dateValue = request.get("attendanceDate");
        String status = request.get("status");

        if (enrollmentIdValue == null || dateValue == null) {
            throw new IllegalArgumentException(
                    "enrollmentId and attendanceDate are required");
        }

        Long enrollmentId = Long.parseLong(enrollmentIdValue);
        LocalDate date = LocalDate.parse(dateValue);

        return attendanceService.markAttendance(
                enrollmentId, date, status);
    }

    @GetMapping("/student/{studentId}/percentage")
    public Map<String, Object> getAttendancePercentage(
            @PathVariable Long studentId) {

        double percentage = attendanceService
                .getStudentAttendancePercentage(studentId);

        return Map.of(
                "studentId", studentId,
                "attendancePercentage", percentage);
    }
}
