
package com.applebay.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "enrollments", uniqueConstraints = @UniqueConstraint(name = "uq_student_course", columnNames = {
        "student_id", "course_id" }))
@Getter
@Setter
@NoArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @PrePersist
    public void setDefaultEnrollmentDate() {
        if (enrollmentDate == null) {
            enrollmentDate = LocalDate.now();
        }
    }
}
