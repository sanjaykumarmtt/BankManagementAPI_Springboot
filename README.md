# Bank Management System API

## Project Overview

The **Bank Management System API** is a robust and secure RESTful backend service built to handle core banking operations. Transitioned from a legacy console-based application, this modern architecture enforces strict separation of concerns, offering a dedicated portal for both Customers and Managers. It provides essential banking functionalities such as user authentication, secure account management, and reliable transaction processing.

## Tech Stack

*   **Backend Framework**: Java 17, Spring Boot 3.4
*   **Build Tool**: Maven
*   **Database**: MySQL
*   **ORM / Data Access**: Spring Data JPA, Hibernate
*   **Security & Validation**: Spring Security, Spring Boot Validation
*   **Frontend**: React JS (Customer and Manager Portals)
*   **Documentation**: Springdoc OpenAPI (Swagger UI)
*   **Deployment**: Docker, Google Cloud Run

## Key Features

*   **Secure Authentication & Authorization**: Secure signup and signin processes for both Managers and Customers with robust role separation.
*   **Strict Input Validation**: Utilizes Hibernate and Spring Validation to enforce strict data integrity (e.g., precise 10-digit mobile number validation).
*   **Transaction Management**: Atomic and transactional processing of core banking operations, including:
    *   Deposits
    *   Withdrawals
    *   Account Transfers
*   **Architectural Refactoring**: Implements DRY principles utilizing a `BaseTransactionService` and unified `TransactionRequest` DTOs for clean and modular business logic.

## Getting Started

### Prerequisites

*   **Java Development Kit (JDK) 17** or higher
*   **Maven** 3.8+
*   **MySQL Server** (Running locally or remotely)
*   **Docker** (Optional, for containerization)

### Local Setup

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd BankManagementSystemAPI
    ```

2.  **Configure the Database:**
    Ensure your MySQL server is running and create a database for the application. Update the `src/main/resources/application.properties` or `application.yml` file with your MySQL credentials:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```

3.  **Build the Project:**
    Use the Maven wrapper to clean and build the application:
    ```bash
    ./mvnw clean package -DskipTests
    ```

4.  **Run the Application:**
    Start the Spring Boot server:
    ```bash
    ./mvnw spring-boot:run
    ```
    The API should now be accessible at `http://localhost:8080`.

## API Documentation

Once the application is running locally, you can access the interactive Swagger UI documentation at:
```
http://localhost:8080/swagger-ui/index.html
```

## Deployment (Google Cloud Run)

This API is configured to be deployed as a containerized serverless application on **Google Cloud Run**.

1.  **Build the Docker Image:**
    The provided `Dockerfile` uses a multi-stage build to compile the Maven project and package it into a minimal Java 17 JRE environment.
    ```bash
    docker build -t bank-api-image .
    ```

2.  **Deploy to Cloud Run:**
    You can deploy directly using the Google Cloud CLI (`gcloud`). Cloud Run will automatically bind to the `$PORT` environment variable used by the Spring Boot container.
    ```bash
    gcloud run deploy bank-api-service \
      --source . \
      --region <your-preferred-region> \
      --allow-unauthenticated
    ```
