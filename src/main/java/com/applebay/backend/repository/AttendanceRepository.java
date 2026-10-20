
package com.applebay.backend.repository;

import com.applebay.backend.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {

    List<Attendance> findByEnrollment_Student_IdOrderByAttendanceDateDesc(
            Long studentId);

    List<Attendance> findByEnrollment_Course_IdAndAttendanceDate(
            Long courseId, LocalDate attendanceDate);

    Optional<Attendance> findByEnrollment_IdAndAttendanceDate(
            Long enrollmentId, LocalDate attendanceDate);

    long countByEnrollment_Student_Id(Long studentId);

    long countByEnrollment_Student_IdAndStatus(
            Long studentId, String status);
}
