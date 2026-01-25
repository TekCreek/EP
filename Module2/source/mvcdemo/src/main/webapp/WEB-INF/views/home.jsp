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