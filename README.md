# Homestay Management System

## Overview

**LullabyNest** is a web application currently under development using **Spring Boot** and **Spring MVC**. This platform is designed to  manage homestay bookings, providing an efficient solution for administrators to handle reservations, customer details, and branch operations. The system aims to improve the user experience for both administrators and customers by offering a centralized platform to manage bookings, search for reservations, and oversee branch activities.

The application allows administrators to:

- **Manage Bookings:** Create, read, update, and delete (CRUD) booking records, including check-in/check-out details, customer information, and payment status.
- **Search and Filter Bookings:** Search bookings by customer name, branch, status, date range, and sort them by check-in date.
- **Manage Branches and Rooms:** Oversee branch information and room.
- **Handle Customer Interactions:** View and manage customer details linked to bookings.

In addition to administrative features, the system is being designed with future enhancements for customer-facing functionalities, such as online booking and profile management.

## Features

- **Booking Management:** Administrators can perform CRUD operations on bookings, including detailed tracking of check-in, check-out, and total payment.
- **Advanced Search:** Filter bookings by keyword, branch, status, date range (currently supporting `dd/MM/yyyy` format), and sorting options.
- **Responsive Interface:** A user-friendly interface built with HTML, CSS, Bootstrap, and JSP for easy navigation and management.
- **Admin Dashboard:** Centralized panel for managing all homestay operations (still in development).

**Note:** The project is currently in the development phase. Features like customer-facing booking, payment integration, and advanced reporting are planned but not yet implemented.

## Technologies Used

- **Backend:** Spring Boot (Spring MVC, Data JPA)
- **Database:** SQL Server
- **Frontend:** HTML, CSS, JavaScript, jQuery, Bootstrap, JSP
- **Build Tool:** Maven
- **Web Server:** Embedded Tomcat (via Spring Boot)
- **IDE:** Visual Studio Code

## Development Progress

This project is an ongoing effort, building on the experience gained from implementing core functionalities such as:
- Integration of Spring MVC for handling HTTP requests and responses.
- Development of a search feature using JPA Specifications for dynamic filtering (e.g., by branch, status, and date range).
- Implementation of `bootstrap-daterangepicker` for date range selection, with ongoing adjustments to support `dd/MM/yyyy` format.
- Creation of a responsive admin interface using JSP and Bootstrap.

**Current Status:**
- The admin booking management page is partially functional, allowing search and display of bookings.
- Date range filtering is implemented but requires further testing and refinement.
- Database integration and full CRUD operations are in progress.
- Customer-facing features (e.g., online booking) and security (e.g., authentication) are yet to be developed.

## Future Plans

- **Complete CRUD Operations:** Finalize create, update, and delete functionalities for bookings, branches, rooms, etc...
- **Customer Interface:** Develop a front-end for customers to book homestays online.
- **Payment Integration:** Add payment processing for bookings.
- **Security:** Implement user authentication and authorization (Spring Security).
- **Reporting:** Add reporting features for administrators to analyze booking trends.

## Note

This project is a work in progress, developed as a graduation thesis and a self-learning project using Spring Boot and related technologies. It does not yet follow all best practices for production-ready applications. The goal is to create a robust homestay management system that enhances operational efficiency and user experience. Feedback and contributions are welcome as the project evolves!
