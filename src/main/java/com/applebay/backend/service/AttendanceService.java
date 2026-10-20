
package com.applebay.backend.service;

import com.applebay.backend.entity.Attendance;
import com.applebay.backend.entity.Enrollment;
import com.applebay.backend.repository.AttendanceRepository;
import com.applebay.backend.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            EnrollmentRepository enrollmentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Attendance> getStudentAttendance(Long studentId) {
        return attendanceRepository
                .findByEnrollment_Student_IdOrderByAttendanceDateDesc(
                        studentId);
    }

    @Transactional(readOnly = true)
    public List<Attendance> getCourseAttendance(
            Long courseId, LocalDate date) {
        return attendanceRepository
                .findByEnrollment_Course_IdAndAttendanceDate(
                        courseId, date);
    }

    public Attendance markAttendance(
            Long enrollmentId,
            LocalDate date,
            String status) {

        if (date == null) {
            throw new IllegalArgumentException(
                    "Attendance date is required");
        }

        if (status == null ||
                !(status.equals("Present") || status.equals("Absent"))) {
            throw new IllegalArgumentException(
                    "Status must be Present or Absent");
        }

        Enrollment enrollment = enrollmentRepository
                .findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException(
                        "Enrollment not found: " + enrollmentId));

        Attendance attendance = attendanceRepository
                .findByEnrollment_IdAndAttendanceDate(
                        enrollmentId, date)
                .orElseGet(Attendance::new);

        attendance.setEnrollment(enrollment);
        attendance.setAttendanceDate(date);
        attendance.setStatus(status);

        return attendanceRepository.save(attendance);
    }

    @Transactional(readOnly = true)
    public double getStudentAttendancePercentage(Long studentId) {
        long total = attendanceRepository
                .countByEnrollment_Student_Id(studentId);

        if (total == 0) {
            return 0.0;
        }

        long present = attendanceRepository
                .countByEnrollment_Student_IdAndStatus(
                        studentId, "Present");

        return Math.round((present * 10000.0) / total) / 100.0;
    }
}
