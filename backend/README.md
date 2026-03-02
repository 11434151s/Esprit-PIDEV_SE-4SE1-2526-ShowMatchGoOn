# Content Management System Backend

This is a Spring Boot backend for a Content Management System, featuring Content (Film, Series, Documentary), Category, and Notification management.

## Features

- **Content Management**: Full CRUD for Films, Series, and Documentaries.
- **Category Management**: Organize content into categories.
- **Notification System**: Internal (DB-stored) and structure for external (Email/SMS) notifications.
- **Security**: Basic Authentication with Role-Based Access Control (ADMIN/USER).
- **Architecture**: Clean layered architecture (Controller -> Service -> Repository) with DTO pattern.
- **Validation**: Jakarta Validation for request payloads.
- **Data Initialization**: Automatically creates default `admin` and `user` accounts on startup.

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.3**
- **Spring Data JPA / Hibernate**
- **Spring Security**
- **MySQL**
- **Lombok**
- **Maven**

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.6+
- MySQL Server

### Configuration

Update `src/main/resources/application.properties` with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/content_db?createDatabaseIfNotExist=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application

1. Navigate to the project root:
   ```bash
   cd content-management-backend
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Troubleshooting Database Connection Issues

If you encounter a `Communications link failure` error, please ensure the following:

1.  **MySQL Server is Running**: Verify that your MySQL server instance is actively running. You can typically check its status via your operating system's service manager or by attempting to connect using a MySQL client.
2.  **Correct Credentials**: Double-check the `spring.datasource.username` and `spring.datasource.password` in `application.properties` match your MySQL server's root (or configured) credentials.
3.  **Port Availability**: Ensure that MySQL is running on port `3306` and that no firewall is blocking the connection.
4.  **Database Exists**: While `createDatabaseIfNotExist=true` is set, sometimes manual creation or verification of the `content_db` database can resolve issues.

## Default Credentials

- **Admin**: `admin` / `admin123`
- **User**: `user` / `user123`

## API Endpoints

### Authentication
- Uses Basic Auth. Include `Authorization: Basic <base64_credentials>` in headers.

### Categories
- `GET /api/categories` - List all categories
- `POST /api/categories` - Create category (USER/ADMIN)
- `DELETE /api/categories/{id}` - Delete category (ADMIN)

### Contents
- `GET /api/contents` - List all content
- `POST /api/contents/films` - Create film (USER/ADMIN)
- `POST /api/contents/series` - Create series (USER/ADMIN)
- `POST /api/contents/documentaries` - Create documentary (USER/ADMIN)
- `DELETE /api/contents/{id}` - Delete content (ADMIN)

### Notifications
- `GET /api/notifications/user/{userId}` - Get user notifications
- `PATCH /api/notifications/{id}/read` - Mark as read
