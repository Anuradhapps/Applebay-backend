
package com.applebay.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "attendance", uniqueConstraints = {
        @UniqueConstraint(name = "uq_enrollment_attendance_date", columnNames = { "enrollment_id", "attendance_date" })
})
@Getter
@Setter
@NoArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(nullable = false, length = 10)
    private String status;

}
