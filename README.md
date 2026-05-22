# Warm Tea and Honest Reviews Server

A Spring Boot-based backend application for managing book reviews. This project provides an
administrative system for creating and managing book reviews with cover images, integrated with
MongoDB for data persistence and Spring Security for authentication.

## Features

- **Admin Authentication**: Secure login using Spring Security with HTTP Basic authentication and
  custom entry points.
- **Review Management**: Create, retrieve, and delete book reviews including title, author, rating,
  categories, and content.
- **Image Storage**: Support for uploading book cover images to **AWS S3** and retrieving their
  public URLs.
- **Category Management**: Dynamic category handling and storage with automatic registration of new
  categories.
- **API Documentation**: Integrated with Spring REST Docs for automated API documentation
  generation.
- **Logging**: Comprehensive logging of controller activities using AspectJ.

## Tech Stack

- **Java**: 25
- **Framework**: Spring Boot 4.0.2
- **Database**: MongoDB (Atlas)
- **Cloud Storage**: AWS S3 (via Spring Cloud AWS)
- **Security**: Spring Security (HTTP Basic)
- **Build Tool**: Gradle
- **Testing**: JUnit 5, MockMvc, Spring REST Docs

## Project Structure

- `src/main/java/com/luna/warmteaandhonestreviews/`
    - `controller/`: REST endpoints for public and administrative access.
    - `service/`: Business logic for reviews, S3 storage, and user management.
    - `repository/`: MongoDB repositories including custom implementations.
    - `domain/`: Entity models (User, BookReview, Category).
    - `dto/`: Data Transfer Objects for API requests and responses.
    - `config/`: Configuration for MongoDB, Security, and Global Exception Handling.
    - `auth/`: Security-related components like UserDetailsService and Role enums.
    - `advisor/`: AOP-based logging for controllers.
    - `core/`: Utility classes.
    - `exception/`: Custom exception classes.

## Getting Started

### Prerequisites

- Java 25 JDK
- MongoDB (or access to MongoDB Atlas)
- Docker (optional, for containerized deployment)

### Configuration

The application is configured via `src/main/resources/application.yml` and profile-specific files
like `application-dev.yml` or `application-prod.yml`.

Key configuration items:

- **MongoDB URI**: Set your MongoDB connection string under `spring.data.mongodb.uri`.
- **AWS Credentials**: Set `spring.cloud.aws.credentials.access-key` and `secret-key`.
- **AWS Region**: Configured via `spring.cloud.aws.region.static`.
- **Upload Directory**: Configured via `app.upload.dir` (defaults to `public/covers`, used for local
  storage if S3 is disabled).

### Build and Run

To build the project and run tests:

```bash
./gradlew build
```

To run the application locally:

```bash
./gradlew bootRun
```

### Docker Support

The project includes a `Dockerfile` for containerization and is configured for CI/CD via GitHub
Actions.

To build and run using Docker:

```bash
docker build -t warm-tea-and-honest-api .
docker run -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI="your_mongodb_uri" \
  -e SPRING_CLOUD_AWS_CREDENTIALS_ACCESS_KEY="your_access_key" \
  -e SPRING_CLOUD_AWS_CREDENTIALS_SECRET_KEY="your_secret_key" \
  warm-tea-and-honest-api
```

The server will start on port `8080` by default.

### CI/CD

A GitHub Actions pipeline (`.github/workflows/deploy.yml`) is set up to:

1. Build and test on every push/PR to `main`.
2. Build and push a Docker image to Docker Hub on push to `main`.
3. Deploy the latest image to an Oracle Server via SSH.

### Default Admin User

On startup, if no admin user exists, a default user is created via `CustomCommandLineRunner`:

- **Username**: `NilKim`
- **Password**: `1234` (Note: This is hashed using BCrypt)

## API Endpoints

### Admin API (Requires Authentication)

- `POST /admin/login`: Admin login.
- `GET /admin/reviews`: Get a paginated list of reviews (`page`, `offset`).
- `GET /admin/reviews/{id}`: Get details of a specific review.
- `POST /admin/reviews`: Create a new review (`multipart/form-data`).
    - Parts: `cover` (file), `title`, `author`, `rating`, `page`, `language`, `category` (JSON
      array), `content`, `publishedAt` (yyyy-MM-dd), `excerpt` (optional).
- `DELETE /admin/reviews/{id}`: Delete a specific review.
- `GET /admin/reviews/{id}/image`: Get the S3 image URL for a specific review.

### Public API

- `GET /api/reviews`: Get reviews.
    - Paginated: `?page=0&offset=6&category=science`
    - Sorted/Recent: `?sort=latest`
- `GET /api/reviews/{id}`: Get details of a specific review.
- `GET /api/reviews/{id}/image`: Get the S3 image URL for a specific review.
- `GET /api/categories`: Get all available categories.

## Documentation

- **Database Schema**: Detailed MongoDB schema is available in [schema.md](schema.md).
- **API Documentation**: Can be generated using Asciidoctor:

```bash
./gradlew asciidoctor
```

The generated documentation will be available in `build/docs/asciidoc`.

## License

This project is for private use or according to the terms specified by the project owner.
