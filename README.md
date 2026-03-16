# Warm Tea and Honest Reviews Server

A Spring Boot-based backend application for managing book reviews. This project provides an administrative system for creating and managing book reviews with cover images, integrated with MongoDB for data persistence and Spring Security for authentication.

## Features

- **Admin Authentication**: Secure login using Spring Security with HTTP Basic authentication and custom entry points.
- **Review Management**: Create and retrieve book reviews including title, author, rating, categories, and content.
- **Image Storage**: Support for uploading and serving book cover images.
- **Category Management**: Dynamic category handling and storage.
- **API Documentation**: Integrated with Spring REST Docs for automated API documentation generation.
- **Logging**: Comprehensive logging of controller activities using AspectJ.

## Tech Stack

- **Java**: 25
- **Framework**: Spring Boot 4.0.2
- **Database**: MongoDB (Atlas)
- **Security**: Spring Security
- **Build Tool**: Gradle
- **Testing**: JUnit 5, MockMvc, Spring REST Docs

## Project Structure

- `src/main/java/com/luna/warmteaandhonestreviews/`
    - `controller/`: REST endpoints for authentication and review management.
    - `service/`: Business logic for reviews, storage, and users.
    - `repository/`: MongoDB repositories.
    - `domain/`: Entity models (User, BookReview, Category).
    - `dto/`: Data Transfer Objects for API requests and responses.
    - `config/`: Configuration for MongoDB, Security, and Global Exception Handling.
    - `auth/`: Security-related components like UserDetailsService and Role enums.
    - `advisor/`: AOP-based logging for controllers.

## Getting Started

### Prerequisites

- Java 25 JDK
- MongoDB (or access to MongoDB Atlas)

### Configuration

The application is configured via `src/main/resources/application.yml`. 

Key configuration items:
- **MongoDB URI**: Set your MongoDB connection string under `spring.mongodb.uri`.
- **Upload Directory**: Configured via `app.upload.dir` (defaults to `public/covers`).

### Build and Run

To build the project and run tests:
```bash
./gradlew build
```

To run the application:
```bash
./gradlew bootRun
```

The server will start on port `8080` by default.

### Default Admin User

On startup, if no admin user exists, a default user is created via `CustomCommandLineRunner`:
- **Username**: `NilKim`
- **Password**: `1234` (Note: This is hashed using BCrypt)

## API Endpoints

### Authentication
- `POST /admin/login`: Admin login.

### Reviews
- `GET /admin/reviews`: Get a paginated list of reviews.
- `GET /admin/reviews/{id}`: Get details of a specific review.
- `POST /admin/reviews`: Create a new review (multipart/form-data).
- `GET /admin/reviews/{id}/image`: Download/view the cover image for a review.

## Documentation

API documentation can be generated using Asciidoctor:
```bash
./gradlew asciidoctor
```
The generated documentation will be available in `build/docs/asciidoc`.

## License

This project is for private use or according to the terms specified by the project owner.
