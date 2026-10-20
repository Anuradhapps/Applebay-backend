
package com.applebay.backend.repository;

import com.applebay.backend.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {

    boolean existsByStudent_IdAndCourse_Id(
            Long studentId, Long courseId);
}
