Project Summary: CandyCo (Java + Spring Boot)

CandyCo is a full-stack educational project developed as part of a Java and Spring Boot curriculum. The application models an e-commerce backend for a fictional candy store, including support for customers, addresses, orders, and products.

This project demonstrates my understanding of object-oriented programming, RESTful APIs, data validation, error handling, layered architecture (controller, service, repository), integration with PostgreSQL, and testing using both unit and integration tests.

 ---
 
Features and Task Overview

Customer Management

-	Implemented full CRUD functionality using a CustomerController and CustomerService.
-	Customers have relationships to both orders and addresses.
-	Includes batch deletion endpoint.
-	Validations on required fields and ID checks.

What I learned:

-	Building REST endpoints with Spring Boot.
-	Connecting and managing relationships between entities using JPA.
-	Designing clean, testable service layers.

 ---
 
Customer Address Handling

-	Managed in a separate CustomerAddressController.
-	Addresses are linked to customers via a foreign key.
-	All address fields are required and validated in the service layer.
-	Error handling for missing or invalid input.

What I learned:

-	Creating and enforcing relational database constraints.
-	Implementing custom exception handling.
-	Validating user input programmatically in service layers.

  ---
 
Order Handling

-	Orders can be created, retrieved, updated, and deleted.
-	Each order is optionally linked to a customer.
-	Integration with products is partially modeled (expandable).

What I learned:

-	Mapping one-to-many and many-to-one relationships with JPA.
-	Using @ResponseBody and @RestController correctly.
-	Creating and managing repository-based database access.

  ---
 
Product Management

-	Supports full CRUD operations for candy products.
-	Each product includes fields for name, description, price, status, and quantity.
-	Includes basic validation and error handling.

What I learned:

-	Structuring and testing domain models with business logic.
-	Building scalable and maintainable REST APIs.

  ---
 
Technical Stack

-	Java 17
-	Spring Boot
-	Maven
-	PostgreSQL (via Testcontainers)
-	JUnit & Mockito for testing
-	Postman used for API testing
-	Thymeleaf for minimal frontend

  ---
 
Testing

Unit Tests

-	Implemented for all service layers using JUnit and Mockito.
-	Covers valid/invalid inputs, exceptions, and edge cases.

Integration Tests

-	Built using Testcontainers with a real PostgreSQL instance.
-	Focused on CRUD operations and entity relationships.
-	Repository tests validate database queries.

Controller Tests

-	Used MockMvc to simulate HTTP requests and responses.
-	Validated status codes, response structure, and exception handling.

What I learned:

-	Writing comprehensive tests for multiple layers of a Spring Boot application.
-	Mocking dependencies and simulating real-world behavior.
-	Measuring and improving test coverage.

  ---
 
Note on Postman Requests

Due to the sequence of deletions in my testing process, some Postman requests may reference IDs that were previously deleted. This means some endpoints (e.g. updating or retrieving a customer or address) may return errors when tested in isolation. This is expected behavior and reflects realistic API behavior when resources no longer exist.

  ---
 
Key Learnings

-	Structuring real-world Spring Boot applications using MVC principles.
-	Writing clear, maintainable, and well-tested code.
-	Managing foreign key relationships in SQL-backed applications.
-	Balancing backend logic with proper error handling and validation.
-	Using Postman and testing frameworks in a structured development workflow.

  ---
 
Future Improvements (if continued)

-	Expand order-product relationship into a full OrderItem model.
-	Add authentication and role-based access (e.g. admin).
-	Create a full frontend in React or Vue.js.
-	Add CI/CD pipeline and Docker containerization.

 ---
 
Contact

If you have any questions, or if you're a recruiter interested in learning more, feel free to get in touch via GitHub or my LinkedIn profile – www.linkedin.com/in/anders-myhrvang-9a6629339.

