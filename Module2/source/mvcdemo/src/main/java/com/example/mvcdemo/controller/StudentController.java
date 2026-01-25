package com.example.mvcdemo.controller;

import com.example.mvcdemo.model.Student;
import com.example.mvcdemo.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER - Student Controller
 * Handles HTTP requests related to students
 */
@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    // Constructor Injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Display all students
     * URL: GET /students
     */
    @GetMapping
    public String listStudents(Model model) {
        // Get data from service (MODEL)
        List<Student> students = studentService.getAllStudents();
        int studentCount = studentService.getStudentCount();

        // Add data to model for VIEW
        model.addAttribute("students", students);
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("pageTitle", "Student List");

        // Return view name (VIEW)
        return "students";  // Resolves to /WEB-INF/views/students.jsp
    }

    /**
     * Show add student form
     * URL: GET /students/add
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        // Create empty student object for form binding
        model.addAttribute("student", new Student());
        model.addAttribute("pageTitle", "Add Student");

        return "add-student";  // Resolves to /WEB-INF/views/add-student.jsp
    }

    /**
     * Process add student form
     * URL: POST /students/add
     */
    @PostMapping("/add")
    public String addStudent(@ModelAttribute("student") Student student,
                             Model model) {
        // Save student through service
        studentService.addStudent(student);

        // Add success message
        model.addAttribute("message", "Student added successfully!");

        // Redirect to student list
        return "redirect:/students";
    }
}