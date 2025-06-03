# LullabyNest - Homestay Management System

## 🏡 Overview

**LullabyNest** is a full-featured homestay management web application built with **Spring Boot** and **Spring MVC**, developed as part of a **graduation thesis** and for self-learning purposes. The system is designed to help administrators efficiently manage homestay operations, including booking, service management, inventory, staff, and customer interactions across multiple branches.

It also includes an integrated customer-facing platform where registered users can manage their bookings, request services, and provide reviews.

---

## ✨ Features

### ✅ Admin Features

#### 🛏️ Booking Management

- Create, and cancel room bookings.
- Auto-cancel unpaid bookings after a set duration.
- Auto-complete bookings after check-out time and award **Reward Points** based on value.
- Booking extension management: create, delete unconfirmed requests.
- Send key booking details to customer's email 30 minutes before check-in.
- View full **transaction history**.
- Automatically generate **refund requests** for paid bookings that are canceled (manual refund only, due to sandbox limitations).

#### 🛎️ Service Booking Management

- Allow pre-paid and post-paid service booking.
- Modify service quantity, update status, or remove booking services from bookings.

#### 🏬 Homestay Information Management

- Manage general information and branch details.
- Define rules and FAQs.
- Handle rooms, room types, amenities, and amenity categories.
- Manage services.

#### 📦 Inventory Management

- Track available inventory (e.g., appliances, supplies).
- Manage inventory categories.
- Record import/export transactions for stock movement history.

#### 🛠️ Maintenance Requests

- Submit, update, delete, and track maintenance requests.
- Manage maintenance status (e.g., Pending, In Progress, Completed, Cancelled, On Hold).

#### 👨‍💼 User & Staff Management

- Manage roles and permissions via **Spring Security**.
- Role types: Admin, Staff, Housekeeper, Registered Customer.
- View and edit user profiles, categorize customers.

#### 📊 Dashboard & Reporting

- Revenue analysis by booking, services, and extensions.
- Top-performing rooms and services.
- Identify loyal customers by **Reward Points**.

#### 🔐 Security & Scheduled Tasks

- Role-based access control using **Spring Security**.
- Scheduled jobs:

  - Auto-complete bookings and assign reward points.
  - Upgrade customer tier if points meet threshold.
  - Cancel unpaid bookings automatically.
  - Delete unpaid booking extensions.
  - Send important emails before check-in.

#### 📧 Email Integration

- Send verification email on registration.
- Forgot password and reset password flow.
- Notify user of password change for added security.
- Send booking confirmation and branch info via email.

---

### 🌐 Customer Features

- **Register/Login** (email verification required).
- **Book Rooms** and **Request Services** (prepaid or postpaid).
- **Cancel Bookings** within allowed time.
- **Extend Bookings** and view status.
- **Make Payments** via **VNPAY (Sandbox Integration)**.
- **Send Reviews** after completed stays.
- **Earn Reward Points** based on completed bookings.

---

## 🛠️ Technologies Used

| Layer      | Technology                                    |
| ---------- | --------------------------------------------- |
| Backend    | Spring Boot, Spring MVC, Spring Data JPA      |
| Security   | Spring Security (role-based authentication)   |
| Frontend   | JSP, Bootstrap, HTML, CSS, JavaScript, jQuery |
| Database   | MySQL                                         |
| Build Tool | Maven                                         |
| Email      | JavaMailSender (SMTP)                         |
| Payment    | VNPAY Sandbox                                 |
| Server     | Embedded Tomcat                               |
| IDE        | Visual Studio Code                            |

---

## 🚀 Current Status

- ✅ Booking, room, branch, and customer management completed.
- ✅ Full CRUD for services, inventory, maintenance, FAQs, and rules.
- ✅ Customer features including booking, cancelation, review, and VNPAY payment completed.
- ✅ Scheduled tasks and email integration implemented.
- ✅ Spring Security with full role-based permission applied.

---

## 🔮 Future Improvements

- Integrate **OAuth2 login via Google and Facebook**.
- Use **JWT** for enhanced authentication and stateless APIs.
- Build **real-time analytics dashboard** with live updates.
- Add **WebSocket chat** between customers and homestay staff.
- Develop **AI-powered chatbot** for instant response.
- Optimize database queries and implement **caching** for performance improvements.
- Add support for **multilingual interfaces** (EN/VI).
- Improve frontend UI/UX, especially for customer module.

---

## ⚙️ Installation & Usage

### 1. Clone the Repository

```bash
git clone https://github.com/nhuytran122/java-spring-mvc-homestaymanagement.git
```

### 2. Setup Database

Open MySQL Workbench or HeidiSQL, and run the provided SQL script to initialize the database structure and seed data.

### 3. Create `.env` File

Create a `.env` file in the project root with the following (replace placeholders as needed):

```
PASSWORD_MYSQL=yourDatabasePassword
PAY_URL=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
TMN_CODE=yourVNPayTmnCode
SECRET_KEY=yourVNPaySecretKey
RETURN_URL=http://localhost:8080/checkout/vn-pay-callback
VERSION=2.1.0
COMMAND=pay
ORDER_TYPE=170000
APP_PASSWORD=yourAppPassword
APP_EMAIL=yourAppEmail@example.com
```

### 4. Configure Application Properties

Open `src/main/resources/application.properties` and set:

```properties
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/homestay_management_db
spring.datasource.username=root
spring.datasource.password=${PASSWORD_MYSQL}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 5. Build the Project

Use Maven to build:

```bash
mvn clean install
```

### 6. Run the Application

- Option 1: Via Maven

  ```bash
  mvn spring-boot:run
  ```

- Option 2: In Visual Studio Code

  - Install **Spring Boot Extension Pack**.
  - Use the Spring Boot dashboard to start and manage the app.

### 7. Access the Application

Open browser at: `http://localhost:8080`

### 🧪 Sample Test Accounts

| Role          | Email                     | Password          |
| ------------- | ------------------------- | ----------------- |
| Customer      | `21t1020105@husc.edu.vn`  | `@Nhuy122`        |
| Admin Manager | `nhuytran12223@gmail.com` | `@Nhuy122`        |
| Staff Member  | `quocanh@gmail.com`       | `lullabyhomestay` |
| Housekeeper   | `thaonguyen@gmail.com`    | `lullabyhomestay` |

---

## 📚 Notes

This system was developed as a **graduation thesis** and a **self-study project**. While already feature-rich, the application may still lack production-level optimizations. The goal is to create a robust, scalable homestay management platform that supports real-world use.

**Feedback and contributions are warmly welcome!**

---
