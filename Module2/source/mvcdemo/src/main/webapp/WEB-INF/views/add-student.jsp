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