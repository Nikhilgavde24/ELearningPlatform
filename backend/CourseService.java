package com.example.E.learning.Platform.Service;

import com.example.E.learning.Platform.Model.Course;
import com.example.E.learning.Platform.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourse() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }

    public List<Course> searchCoursesByTitle(String keyword) {
        return courseRepository.findByTitleContainingIgnoreCase(keyword);
    }
}
