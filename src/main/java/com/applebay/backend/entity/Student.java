
package com.applebay.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_no", nullable = false, unique = true)
    private String studentNo;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String nic;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

}
