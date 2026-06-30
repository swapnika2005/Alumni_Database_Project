# 🎓 Alumni Database Management System

A Java-based desktop application developed using **NetBeans IDE**, **Java Swing**, **JDBC**, and **MySQL** to efficiently manage alumni information, facilitate communication between alumni and administrators, and maintain institutional records through a secure and user-friendly interface.

---

## 📖 Overview

The Alumni Database Management System provides a centralized platform for educational institutions to manage alumni records, achievements, events, and user information. The system features separate Admin and User modules with secure authentication, making alumni data management efficient, organized, and accessible.

This project was developed as part of an academic software engineering project to demonstrate practical implementation of Java desktop application development and relational database management.

---

## ✨ Features

### 🔐 Authentication
- Admin Login
- User (Alumni) Login
- User Registration
- Forgot Password
- Password Authentication

### 👨‍💼 Admin Module
- Dashboard
- View Users
- Add New Users
- Delete Users
- Search Users
- Manage Alumni Records
- Manage Alumni Achievements
- Manage Alumni Events
- Logout

### 👨‍🎓 Alumni/User Module
- View Profile
- Update Profile
- View Alumni Achievements
- View Upcoming Events
- Logout

### 📂 Alumni Management
- Add Alumni
- Update Alumni Information
- Delete Alumni
- Search Alumni
- Store Academic & Professional Details

### 🏆 Achievement Management
- Add Achievements
- View Achievements
- Delete Achievements
- Track Alumni Success

### 📅 Event Management
- Create Alumni Events
- Update Events
- View Events
- Delete Events

### 🔍 Search System
- Search Users
- Search Alumni Records
- Quick Data Retrieval

---

## 🛠️ Technologies Used

| Technology | Purpose |
|------------|----------|
| Java | Core Programming Language |
| Java Swing | GUI Development |
| NetBeans IDE | Development Environment |
| MySQL | Database |
| JDBC | Database Connectivity |
| SQL | Database Queries |
| Git | Version Control |
| GitHub | Source Code Repository |

---

## 🏗️ System Architecture

```
User
   │
   ▼
Java Swing GUI
   │
   ▼
Business Logic (Java)
   │
   ▼
JDBC
   │
   ▼
MySQL Database
```

---

## 🗄️ Database Structure

The system consists of the following tables:

- Users
- Admin
- Alumni
- Achievements

These tables work together to securely manage user authentication, alumni information, achievements, and administrative operations.

---

## 📁 Project Structure

```
Alumni_Database_Project
│
├── src/
│   ├── alumni.db
│   ├── alumni.dao
│   ├── alumni.model
│   ├── alumni.ui
│   └── Main.java
│
├── database/
│   └── alumni_db.sql
│
├── screenshots/
│
├── README.md
│
└── LICENSE
```

---

## 🚀 Installation

### Prerequisites

- Java JDK 8 or above
- NetBeans IDE
- MySQL Server
- MySQL Connector/J (JDBC Driver)

### Steps

1. Clone the repository

```bash
git clone https://github.com/swapnika2005/Alumni_Database_Project.git
```

2. Open the project in NetBeans IDE.

3. Create a MySQL database.

```sql
CREATE DATABASE alumni_db;
```

4. Import the provided SQL file.

5. Configure database credentials inside:

```
DBConnection.java
```

Example:

```java
private static final String URL = "jdbc:mysql://localhost:3306/alumni_db";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

6. Add MySQL Connector/J library.

7. Build and Run the project.

---

## 💻 Application Modules

### Login Module
- Secure authentication
- Role selection
- Password validation
- Registration
- Forgot Password

### Admin Dashboard
- User Management
- Alumni Management
- Event Management
- Achievement Management
- Search Records

### User Dashboard
- Profile Management
- View Events
- View Achievements
- Update Profile

---

## 📸 Screenshots

Add screenshots of:

- Login Page
- User Registration
- Admin Dashboard
- User Dashboard
- Alumni Management
- Event Management
- Achievement Management
- Search Module

Example:

```
screenshots/
    login.png
    admin_dashboard.png
    user_dashboard.png
    alumni_management.png
```

---

## 🎯 Learning Outcomes

Through this project, I gained practical experience in:

- Java Programming
- Object-Oriented Programming (OOP)
- Java Swing GUI Development
- JDBC Integration
- MySQL Database Design
- CRUD Operations
- Authentication & Authorization
- Desktop Application Development
- Software Engineering Principles
- Git & GitHub Version Control

---

## 🔮 Future Enhancements

- Email notifications for alumni events
- Cloud database integration
- Online web-based version
- Mobile application support
- Role-based permission management
- Alumni networking forum
- Resume and job application portal
- Event registration system
- Real-time notifications

---

This project is developed for educational and learning purposes.

Feel free to use and enhance it with proper attribution.
