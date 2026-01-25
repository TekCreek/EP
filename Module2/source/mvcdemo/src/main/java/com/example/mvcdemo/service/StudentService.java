package com.example.mvcdemo.service;


import com.example.mvcdemo.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * MODEL - Service Layer
 * Contains business logic for student operations
 */
@Service
public class StudentService {

    // In-memory storage (simulating database)
    private List<Student> students = new ArrayList<>();
    private AtomicLong idCounter = new AtomicLong(1);

    // Constructor - Initialize with sample data
    public StudentService() {
        students.add(new Student(idCounter.getAndIncrement(),
                "John Doe", "john@example.com", 20, "Computer Science"));
        students.add(new Student(idCounter.getAndIncrement(),
                "Jane Smith", "jane@example.com", 22, "Mathematics"));
        students.add(new Student(idCounter.getAndIncrement(),
                "Bob Johnson", "bob@example.com", 21, "Physics"));
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    /**
     * Get student by ID
     */
    public Student getStudentById(Long id) {
        return students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Add new student
     */
    public Student addStudent(Student student) {
        student.setId(idCounter.getAndIncrement());
        students.add(student);
        return student;
    }

    /**
     * Get total number of students
     */
    public int getStudentCount() {
        return students.size();
    }
}