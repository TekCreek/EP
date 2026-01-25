# Demo: Implementing MVC Architecture with Spring Boot and JSP

## Overview

We'll build a simple application to manage students with these features:
- View all students
- Add new student
- Home page

## Step 1: Project Setup

**Create Spring Boot Project:**

```xml
<!-- pom.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>student-management</artifactId>
    <version>1.0.0</version>
    <name>Student Management System</name>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- JSP Support -->
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
            <scope>provided</scope>
        </dependency>
        
        <!-- JSTL -->
        <dependency>
            <groupId>jakarta.servlet.jsp.jstl</groupId>

            <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
        </dependency>
        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
        </dependency>
        
        <!-- Spring Boot DevTools (optional) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## Step 2: Configuration

**application.properties:**

```properties
# JSP View Resolver Configuration
spring.mvc.view.prefix=/WEB-INF/views/

spring.mvc.view.suffix=.jsp

# Server Configuration
server.port=8080

# Application Name
spring.application.name=Student Management System

# Logging
logging.level.org.springframework.web=DEBUG
```

---

## Step-by-Step Implementation

### Step 1: Create Model (Student.java)

```java
package com.example.demo.model;

/**
 * MODEL - Student Entity
 * Represents student data
 */
public class Student {
    
    private Long id;
    private String name;
    private String email;
    private int age;
    private String course;
    
    // Default Constructor
    public Student() {
    }
    
    // Parameterized Constructor
    public Student(Long id, String name, String email, int age, String course) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.course = course;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getCourse() {
        return course;
    }
    
    public void setCourse(String course) {
        this.course = course;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", course='" + course + '\'' +
                '}';
    }
}
```

### Step 2: Create Service (StudentService.java)

```java
package com.example.demo.service;

import com.example.demo.model.Student;
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
```

### Step 3: Create Controller (StudentController.java)

```java
package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
```

### Step 4: Create Home Controller (HomeController.java)

```java
package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * CONTROLLER - Home Controller
 * Handles home page requests
 */
@Controller
public class HomeController {
    
    /**
     * Home page
     * URL: GET /
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("welcomeMessage", 
                "Welcome to Student Management System");
        
        return "home";  // Resolves to /WEB-INF/views/home.jsp
    }
}
```

### Step 5: Create Views (JSP Files)


**1. home.jsp - Home Page**

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            text-align: center;
        }
        .welcome-message {
            text-align: center;
            font-size: 18px;
            color: #666;
            margin: 20px 0;
        }
        .menu {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-top: 40px;
        }
        .menu-item {
            background-color: #007bff;
            color: white;
            padding: 15px 30px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
            transition: background-color 0.3s;
        }
        .menu-item:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>${welcomeMessage}</h1>
        
        <div class="welcome-message">
            <p>Manage your students efficiently with our system</p>
        </div>
        
        <div class="menu">
            <a href="/students" class="menu-item">View Students</a>
            <a href="/students/add" class="menu-item">Add Student</a>
        </div>
    </div>
</body>
</html>
```

**2. students.jsp - Student List Page**

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1000px;
            margin: 30px auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 10px;
        }
        .student-count {
            text-align: center;
            color: #666;
            margin-bottom: 30px;
            font-size: 16px;
        }
        .nav-links {
            margin-bottom: 20px;
            display: flex;
            gap: 10px;
        }
        .nav-links a {
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
        }
        .nav-links a:hover {
            background-color: #218838;
        }
        .home-link {
            background-color: #6c757d !important;
        }
        .home-link:hover {
            background-color: #5a6268 !important;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th {
            background-color: #007bff;
            color: white;
            padding: 12px;
            text-align: left;
        }
        td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .no-students {
            text-align: center;
            padding: 40px;
            color: #666;
            font-style: italic;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>${pageTitle}</h1>
        <div class="student-count">
            Total Students: <strong>${studentCount}</strong>
        </div>
        
        <div class="nav-links">
            <a href="/" class="home-link">Home</a>
            <a href="/students/add">Add New Student</a>
        </div>
        
        <!-- Check if students list is empty -->
        <c:choose>
            <c:when test="${empty students}">
                <div class="no-students">
                    No students found. Please add some students.
                </div>
            </c:when>
            <c:otherwise>
                <!-- Display students table -->
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Age</th>
                            <th>Course</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!-- Loop through students using JSTL -->
                        <c:forEach items="${students}" var="student">
                            <tr>
                                <td>${student.id}</td>
                                <td>${student.name}</td>
                                <td>${student.email}</td>
                                <td>${student.age}</td>
                                <td>${student.course}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
```

**3. add-student.jsp - Add Student Form**

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        .nav-links {
            margin-bottom: 20px;
            display: flex;
            gap: 10px;
        }
        .nav-links a {
            padding: 10px 20px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
        }
        .nav-links a:hover {
            background-color: #5a6268;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #333;
            font-weight: bold;
        }
        input[type="text"],
        input[type="email"],
        input[type="number"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
            box-sizing: border-box;
        }
        input:focus {
            outline: none;
            border-color: #007bff;
        }
        .submit-btn {
            width: 100%;
            padding: 12px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .submit-btn:hover {
            background-color: #218838;
        }
        .message {
            padding: 10px;
            margin-bottom: 20px;
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>${pageTitle}</h1>
        
        <div class="nav-links">
            <a href="/">Home</a>
            <a href="/students">View All Students</a>
        </div>
        
        <!-- Display success message if exists -->
        <c:if test="${not empty message}">
            <div class="message">${message}</div>
        </c:if>
        
        <!-- Student Form -->
        <form action="/students/add" method="POST">
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" 
                       id="name" 
                       name="name" 
                       placeholder="Enter student name" 
                       required>
            </div>
            
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" 
                       id="email" 
                       name="email" 
                       placeholder="Enter student email" 
                       required>
            </div>
            
            <div class="form-group">
                <label for="age">Age:</label>
                <input type="number" 
                       id="age" 
                       name="age" 
                       min="15" 
                       max="100" 
                       placeholder="Enter student age" 
                       required>
            </div>
            
            <div class="form-group">
                <label for="course">Course:</label>
                <input type="text" 
                       id="course" 
                       name="course" 
                       placeholder="Enter course name" 
                       required>
            </div>
            
            <button type="submit" class="submit-btn">Add Student</button>
        </form>
    </div>
</body>
</html>
```

### Step 6: Main Application Class

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application
 * Entry point of the application
 */
@SpringBootApplication
public class StudentManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("Student Management System Started!");
        System.out.println("Access the application at:");
        System.out.println("http://localhost:8080");
        System.out.println("========================================\n");
    }
}
```

### Complete Folder Structure

```
student-management/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── demo/
│   │   │               ├── StudentManagementApplication.java
│   │   │               ├── controller/
│   │   │               │   ├── HomeController.java
│   │   │               │   └── StudentController.java
│   │   │               ├── model/
│   │   │               │   └── Student.java
│   │   │               └── service/
│   │   │                   └── StudentService.java
│   │   │
│   │   ├── resources/
│   │   │   └── application.properties
│   │   │
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── views/
│   │               ├── home.jsp
│   │               ├── students.jsp
│   │               └── add-student.jsp
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── demo/
│
└── target/
```

---

## Running the Application

### Step-by-Step Execution

```
┌────────────────────────────────────────────────────────┐
│         HOW TO RUN THE APPLICATION                     │
├────────────────────────────────────────────────────────┤
│                                                        │
│  STEP 1: Build the Project                             │
│  ┌────────────────────────────────────────────────┐    │
│  │ mvn clean install                              │    │
│  └────────────────────────────────────────────────┘    │
│                                                        │
│  STEP 2: Run the Application                           │
│  ┌────────────────────────────────────────────────┐    │
│  │ mvn spring-boot:run                            │    │
│  │                                                │    │
│  │ OR                                             │    │
│  │                                                │    │
│  │ java -jar target/student-management-1.0.0.jar  │    │
│  └────────────────────────────────────────────────┘    │
│                                                        │
│  STEP 3: Access in Browser                             │
│  ┌────────────────────────────────────────────────┐    │
│  │ http://localhost:8080                          │    │
│  └────────────────────────────────────────────────┘    │
│                                                        │
└────────────────────────────────────────────────────────┘
```

### Testing the Application

**1. Access Home Page**
```
URL: http://localhost:8080/
Expected: Welcome page with navigation links
```

**2. View Students**
```
URL: http://localhost:8080/students
Expected: Table showing 3 pre-loaded students
```

**3. Add New Student**
```
URL: http://localhost:8080/students/add
Expected: Form to add new student

Fill Form:
- Name: Alice Brown
- Email: alice@example.com
- Age: 23
- Course: Engineering

Submit → Redirects to student list with new student
```

---

## How MVC Works in This Example

### Flow Diagram for Viewing Students

```
┌────────────────────────────────────────────────────────┐
│         VIEW STUDENTS FLOW                             │
├────────────────────────────────────────────────────────┤
│                                                        │
│  User clicks "View Students" button                    │
│              │                                         │
│              ▼                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │ Browser sends: GET /students                    │   │
│  └─────────────────────────────────────────────────┘   │
│              │                                         │
│              ▼                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │ DispatcherServlet receives request              │   │
│  └─────────────────────────────────────────────────┘   │
│              │                                         │
│              ▼                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │ Routes to: StudentController.listStudents()     │   │
│  └─────────────────────────────────────────────────┘   │
│              │                                         │
│              ▼                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │ CONTROLLER calls MODEL                          │   │
│  │ List<Student> students =                        │   │
│  │     studentService.getAllStudents();            │   │
│  └─────────────────────────────────────────────────┘   │
│              │                                         │
│              ▼                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │ MODEL (Service) returns data                    │   │
│  │ Returns: [Student1, Student2, Student3]         │   │
│  └─────────────────────────────────────────────────┘   │
│              │                                         │
│              ▼                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │ CONTROLLER adds data to Model                   │   │
│  │ model.addAttribute("students", students);       │   │
│  │ return "students";                              │   │
│  └─────────────────────────────────────────────────┘   │
│              │                                         │
│              ▼                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │ View Resolver finds VIEW                        │   │
│  │ /WEB-INF/views/students.jsp                     │   │
│  └─────────────────────────────────────────────────┘   │
│              │                                         │
│              ▼                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │ VIEW (JSP) renders with data                    │   │
│  │ <c:forEach items="${students}">                 │   │
│  │     Display each student in table               │   │
│  │ </c:forEach>                                    │   │
│  └─────────────────────────────────────────────────┘   │
│              │                                         │
│              ▼                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │ HTML sent to Browser                            │   │
│  │ User sees student table                         │   │
│  └─────────────────────────────────────────────────┘   │
│                                                        │
└────────────────────────────────────────────────────────┘
```

### Flow Diagram for Adding Student

```
┌────────────────────────────────────────────────────────┐
│         ADD STUDENT FLOW                               │
├────────────────────────────────────────────────────────┤
│                                                        │
│  PART 1: Show Form                                     │
│  ┌────────────────────────────────────────────────┐    │
│  │ GET /students/add                              │    │
│  │         ↓                                      │    │
│  │ StudentController.showAddForm()                │    │
│  │         ↓                                      │    │
│  │ Returns "add-student" view                     │    │
│  │         ↓                                      │    │
│  │ User sees form                                 │    │
│  └────────────────────────────────────────────────┘    │
│                                                        │
│  PART 2: Submit Form                                   │
│  ┌────────────────────────────────────────────────┐    │
│  │ User fills form and clicks "Add Student"       │    │
│  │         ↓                                      │    │
│  │ POST /students/add                             │    │
│  │ Body: name=Alice&email=alice@example.com...    │    │
│  │         ↓                                      │    │
│  │ StudentController.addStudent()                 │    │
│  │         ↓                                      │    │
│  │ Spring binds form data to Student object       │    │
│  │         ↓                                      │    │
│  │ Controller calls Service                       │    │
│  │ studentService.addStudent(student)             │    │
│  │         ↓                                      │    │
│  │ Service saves student to list                  │    │
│  │         ↓                                      │    │
│  │ Controller redirects to /students              │    │
│  │         ↓                                      │    │
│  │ User sees updated student list                 │    │
│  └────────────────────────────────────────────────┘    │
│                                                        │
└────────────────────────────────────────────────────────┘
```