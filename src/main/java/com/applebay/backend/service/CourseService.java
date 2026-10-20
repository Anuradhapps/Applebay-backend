
package com.applebay.backend.service;

import com.applebay.backend.entity.Course;
import com.applebay.backend.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));
    }

    public Course createCourse(Course course) {
        if (courseRepository.existsByCodeIgnoreCase(course.getCode())) {
            throw new IllegalArgumentException(
                    "Course code already exists");
        }

        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Course course = getCourseById(id);

        if (!course.getCode().equalsIgnoreCase(updatedCourse.getCode())
                && courseRepository.existsByCodeIgnoreCase(
                        updatedCourse.getCode())) {
            throw new IllegalArgumentException(
                    "Course code already exists");
        }

        course.setCode(updatedCourse.getCode());
        course.setName(updatedCourse.getName());
        course.setLevel(updatedCourse.getLevel());
        course.setDurationMonths(updatedCourse.getDurationMonths());

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }
}
