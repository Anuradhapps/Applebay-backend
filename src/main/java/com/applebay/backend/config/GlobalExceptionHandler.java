package com.applebay.backend.config;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Turns exceptions into {"message": "..."} JSON, which is the shape the
 * frontend Alert component already understands.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static ResponseEntity<Map<String, String>> body(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("message", message));
    }

    // Unique / NOT NULL / foreign key violations from the database
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleIntegrity(DataIntegrityViolationException ex) {
        String detail = String.valueOf(ex.getMostSpecificCause().getMessage()).toLowerCase();

        if (detail.contains("null value in column")) {
            if (detail.contains("email"))
                return body(HttpStatus.BAD_REQUEST, "Email is required.");
            return body(HttpStatus.BAD_REQUEST, "A required field is missing.");
        }
        if (detail.contains("students_nic_key") || detail.contains("(nic)")) {
            return body(HttpStatus.CONFLICT, "A student with this NIC already exists.");
        }
        if (detail.contains("students_student_no_key") || detail.contains("(student_no)")) {
            return body(HttpStatus.CONFLICT, "A student with this student number already exists.");
        }
        if (detail.contains("students_email_key") || detail.contains("(email)")) {
            return body(HttpStatus.CONFLICT, "A student with this email already exists.");
        }
        if (detail.contains("courses_code_key") || detail.contains("(code)")) {
            return body(HttpStatus.CONFLICT, "A course with this code already exists.");
        }
        if (detail.contains("uq_student_course")) {
            return body(HttpStatus.CONFLICT, "This student is already enrolled in this course.");
        }
        if (detail.contains("uq_enrollment_attendance_date")) {
            return body(HttpStatus.CONFLICT, "Attendance is already recorded for this date.");
        }
        if (detail.contains("value too long")) {
            return body(HttpStatus.BAD_REQUEST, "One of the values is too long.");
        }
        if (detail.contains("foreign key")) {
            return body(HttpStatus.CONFLICT, "This record is linked to other data and cannot be changed or deleted.");
        }
        return body(HttpStatus.CONFLICT, "The data conflicts with an existing record.");
    }

    @ExceptionHandler({ NoSuchElementException.class })
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException ex) {
        return body(HttpStatus.NOT_FOUND, ex.getMessage() != null ? ex.getMessage() : "Record not found.");
    }

    @ExceptionHandler({ IllegalArgumentException.class, HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Map<String, String>> handleBadRequest(Exception ex) {
        String msg = (ex instanceof IllegalArgumentException && ex.getMessage() != null)
                ? ex.getMessage()
                : "Invalid request. Please check the submitted values.";
        return body(HttpStatus.BAD_REQUEST, msg);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOther(Exception ex) {
        // Log the real cause on the server; keep the client message generic.
        ex.printStackTrace();
        return body(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong on the server. Please try again.");
    }
}