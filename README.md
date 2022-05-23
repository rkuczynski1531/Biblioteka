## Description

It's an application for library written in HTML 5, CSS 3 and Java using Spring Framework and Thymeleaf.

## Features

- CRUD operation on books
- Registration and login
- Adding and deleting comments
- The comments have to be accepted by moderator or admin, if they are written by normal user
- Borrowing books or reserving if there aren none in the stock at the moment
- Rating books
- The books can be filtered by category or searched by inputting the title
- Sending messages to other users
- Pagination and sorting
- Changing role of a user

## Built with

- Java 16
- Apache Maven 3.8.1
- Spring Boot 2.5.3
- Spring Framework
- Spring Security
- Spring Data JPA
- Spring MVC
- Bootstrap 5.1.1
- JQuery 3.6.0
- Lombok 1.18.22
- Thymeleaf 3.0.4
- Commons IO 2.6
- H2 Database


## Compilation and usage

To compile the application:

    mvn clean install
    
To run the application simply paste and run the following command in your CLI

On Windows:

    ./mvnw.cmd spring-boot:run

On Linux:

    ./mvnw spring-boot:run
