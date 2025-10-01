# Spring Security: Login-Register with JWT Authentication

This project demonstrates how to implement a secure user authentication system using Spring Boot, Spring Security, and JSON Web Tokens (JWT). The application provides endpoints for user registration, login, and secure resource access through JWT authentication.

## Table of Contents
- [Project Overview](#project-overview)
- [Prerequisites](#prerequisites)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Step-by-Step Setup Guide](#step-by-step-setup-guide)
  - [1. Setting Up the Project](#1-setting-up-the-project)
  - [2. Database Configuration](#2-database-configuration)
  - [3. Creating the User Entity](#3-creating-the-user-entity)
  - [4. JWT Authentication Implementation](#4-jwt-authentication-implementation)
  - [5. Spring Security Configuration](#5-spring-security-configuration)
  - [6. User Controller Implementation](#6-user-controller-implementation)
  - [7. Testing the Application](#7-testing-the-application)
- [API Endpoints](#api-endpoints)
- [Security Considerations](#security-considerations)
- [Troubleshooting](#troubleshooting)
- [Further Enhancements](#further-enhancements)

## Project Overview

This application implements a complete authentication system with the following features:
- User registration with password encryption
- User login with JWT token generation
- JWT-based authentication for secured endpoints
- Token validation and error handling

## Prerequisites

Before you begin, ensure you have the following installed:
- Java Development Kit (JDK) 17 or newer
- Maven 3.6 or newer
- Your preferred IDE (IntelliJ IDEA, Eclipse, VS Code)
- Postman or similar tool for API testing

## Technologies Used

- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- JWT (JSON Web Tokens) using JJWT library
- BCrypt Password Encoder
- H2/MySQL/PostgreSQL Database (configurable)
- Lombok for reducing boilerplate code

## Project Structure

The project follows a standard Spring Boot application structure:

```
src/main/java/com/example/springsecurity/
├── LoginRegisterWithJwtApplication.java    # Main application class
├── config/
│   └── WebSecurityConfig.java              # Security configuration
├── controller/
│   └── UserController.java                 # REST endpoints
├── entity/
│   └── User.java                           # User entity
├── filter/
│   └── JwtAuthenticationFilter.java        # JWT auth filter
├── repository/
│   └── UserRepo.java                       # User repository
├── service/
│   └── UserDetailsServiceImpl.java         # UserDetails service
└── util/
    └── JwtUtil.java                        # JWT generation & validation
```

## Step-by-Step Setup Guide

### 1. Setting Up the Project

1. **Create a new Spring Boot project:**
   - You can use Spring Initializr (https://start.spring.io/) with the following dependencies:
     - Spring Web
     - Spring Security
     - Spring Data JPA
     - H2 Database (for development) or your preferred database
     - Lombok

2. **Add JWT dependencies to your pom.xml:**
   ```xml
   <dependency>
       <groupId>io.jsonwebtoken</groupId>
       <artifactId>jjwt-api</artifactId>
       <version>0.11.5</version>
   </dependency>
   <dependency>
       <groupId>io.jsonwebtoken</groupId>
       <artifactId>jjwt-impl</artifactId>
       <version>0.11.5</version>
       <scope>runtime</scope>
   </dependency>
   <dependency>
       <groupId>io.jsonwebtoken</groupId>
       <artifactId>jjwt-jackson</artifactId>
       <version>0.11.5</version>
       <scope>runtime</scope>
   </dependency>
   ```

### 2. Database Configuration

Configure your database connection in `application.properties`:

```properties
# Database configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# JWT configuration
jwt.secret.key=YourBase64EncodedSecretKey(At_Least_64_Bytes_Long_For_Security)
jwt.expiration.time=86400000
jwt.token.issuer=your-application-name
jwt.token.audience=your-application-users

# Enable H2 Console (optional, for development only)
spring.h2.console.enabled=true
```

> **Important:** For production, use a strong, randomly generated secret key of at least 32 bytes (256 bits) and store it securely.

### 3. Creating the User Entity

Create a User entity class that will represent users in your database:

```java
@Entity
@Table(name = "users")  // Avoid conflict with reserved SQL keywords
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
}
```

Create a repository interface:

```java
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

### 4. JWT Authentication Implementation

1. **Create JwtUtil class** for JWT token generation and validation:

The `JwtUtil` class provides methods for:
- Generating JWT tokens
- Extracting claims from tokens
- Validating tokens
- Handling token errors

Key aspects include:
- Using a secure signing key
- Setting proper token expiration
- Including issuer and audience claims
- Robust exception handling

2. **Implement JWT Authentication Filter:**

The `JwtAuthenticationFilter` extends `OncePerRequestFilter` and:
- Extracts JWT from the Authorization header
- Validates the token
- Loads user details if the token is valid
- Sets the authentication in Spring Security context

### 5. Spring Security Configuration

Configure Spring Security in the `WebSecurityConfig` class:

```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // Configure authentication manager, password encoder, etc.
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request -> request
                .requestMatchers("/register", "/login").permitAll()
                .anyRequest().authenticated())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

### 6. User Controller Implementation

Create a controller with endpoints for registration, login, and protected resources:

```java
@RestController
public class UserController {

    // Login endpoint - authenticates user and returns JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // Authenticate user
        // Generate JWT token
        // Return token in response
    }

    // Registration endpoint - stores new user with encrypted password
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        // Encode password
        // Save user
        // Return success message
    }

    // Protected endpoint - only accessible with valid JWT
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the JWT Authentication Demo!";
    }
}
```

### 7. Testing the Application

1. **Start the application:**
   ```
   mvn spring-boot:run
   ```

2. **Register a new user:**
   - Send a POST request to `/register` with user details
   ```json
   {
       "username": "testuser",
       "email": "user@example.com",
       "password": "securepassword"
   }
   ```

3. **Login with the user:**
   - Send a POST request to `/login` with credentials
   ```json
   {
       "email": "user@example.com",
       "password": "securepassword"
   }
   ```
   - You will receive a JWT token in the response

4. **Access protected resources:**
   - Send a GET request to `/welcome` with the Authorization header:
   ```
   Authorization: Bearer {your-jwt-token}
   ```

## API Endpoints

| Method | URL       | Description                              | Access     |
|--------|-----------|------------------------------------------|------------|
| POST   | /register | Register new user                        | Public     |
| POST   | /login    | Login and retrieve JWT token             | Public     |
| GET    | /welcome  | Example protected resource               | Protected  |

## Security Considerations

1. **Token Security:**
   - Use a strong secret key (at least 32 bytes/256 bits)
   - Set appropriate token expiration time
   - Include issuer and audience claims

2. **Password Security:**
   - Always store passwords using BCryptPasswordEncoder
   - Never expose password data in responses

3. **Error Handling:**
   - Implement proper exception handling
   - Don't expose sensitive information in error messages

4. **Production Settings:**
   - Use HTTPS for all communication
   - Store secrets in environment variables or a secure vault
   - Consider implementing token refresh mechanism

## Troubleshooting

Common issues and solutions:

1. **Authentication fails with valid credentials:**
   - Check if the user exists in the database
   - Verify that password encoder configuration is consistent

2. **JWT token validation fails:**
   - Ensure the secret key is consistent
   - Check token expiration time
   - Verify the token format in the Authorization header

3. **Database connection issues:**
   - Verify database connection properties
   - Check if the database server is running

## Further Enhancements

Consider these improvements for a production-ready application:

1. **Role-based authorization**
2. **Token refresh mechanism**
3. **Email verification during registration**
4. **Password reset functionality**
5. **Account locking after failed login attempts**
6. **OAuth2 integration for social login**
7. **Two-factor authentication**

---

This project provides a solid foundation for implementing JWT-based authentication in Spring Boot applications. Feel free to customize it according to your specific requirements.
