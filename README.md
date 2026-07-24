# 🔐 Secure Gateway Messaging System

A secure web-based messaging system developed using Java, JSP, Servlets, JDBC, and MySQL. The application enables secure communication between administrators and officers through role-based authentication, encrypted passwords, and controlled message delivery.

## 📌 Project Overview

The Secure Gateway Messaging System is designed to provide a secure communication platform where administrators can manage officers and send messages, while officers can securely receive, reply to, and manage their messages. The system ensures authentication, authorization, and secure message handling.

---

## ✨ Features

### 👨‍💼 Admin Module
- Admin Login
- Dashboard with statistics
- Add Officer
- View Officers
- Edit Officer Details
- Delete Officer
- Search Officers
- Send Messages to Officers
- View Sent Messages

### 👮 Officer Module
- Officer Login
- Dashboard
- Inbox
- Compose Message
- Reply to Messages
- View Sent Messages
- Download Attachments

### 🔒 Security Features
- Role-Based Authentication
- BCrypt Password Encryption
- Session Management
- Authorization Filters
- SQL Injection Prevention using PreparedStatement
- Secure File Upload Support

---

## 🛠️ Technologies Used

### Backend
- Java
- JSP
- Servlets
- JDBC

### Frontend
- HTML
- CSS
- JavaScript

### Database
- MySQL

### Server
- Apache Tomcat 9

### Build Tool
- Maven

### IDE
- IntelliJ IDEA

---

## 📂 Project Structure

```
SecureGateway/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── config/
│   │   │   ├── dao/
│   │   │   ├── model/
│   │   │   ├── service/
│   │   │   ├── servlet/
│   │   │   ├── filter/
│   │   │   └── util/
│   │
│   └── webapp/
│       ├── admin/
│       ├── officer/
│       ├── css/
│       ├── js/
│       └── WEB-INF/
│
├── pom.xml
└── README.md
```

---

## 🗄️ Database

Database Name:

```
secure_gateway_db
```

Main Tables:

- users
- roles
- messages

---

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/SHOBANA-6/Secure_Gateway_Messaging_System.git
```

### 2. Open in IntelliJ IDEA

Open the project as a Maven project.

### 3. Configure Database

- Create a MySQL database named:

```
secure_gateway_db
```

- Import the SQL script.

### 4. Update Database Configuration

Edit:

```
DBConnection.java
```

Update:

- Database URL
- Username
- Password

### 5. Build the Project

```bash
mvn clean install
```

### 6. Deploy

Deploy the generated WAR file on Apache Tomcat 9.

---

## 👥 User Roles

### Admin

- Manage Officers
- Send Messages
- View Sent Messages
- Monitor Dashboard

### Officer

- Login
- Read Messages
- Reply to Messages
- Send Messages
- Download Attachments

---

## 🔐 Security Implementation

- BCrypt Password Hashing
- Session-Based Authentication
- Authorization Filters
- Secure JDBC Prepared Statements
- Role-Based Access Control

---

## 👩‍💻 Developed By

**Shobana M**

BCA Student

Queen Mary's College (Autonomous), Chennai.

---

## 📄 License

This project was developed for educational and internship purposes.
