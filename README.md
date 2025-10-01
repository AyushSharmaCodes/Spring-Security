# Spring Security Examples Repository

![Spring Security](https://img.shields.io/badge/Spring%20Security-6.0+-green.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0+-green.svg)
![Java](https://img.shields.io/badge/Java-17+-blue.svg)
![Last Updated](https://img.shields.io/badge/Last%20Updated-October%202025-orange.svg)

This repository contains a collection of Spring Security examples, demonstrating various authentication and authorization techniques, from basic authentication to JWT tokens, OAuth2, and more. Each project builds upon the knowledge of the previous one, making this repository an excellent learning path for Spring Security.

## Table of Contents
- [Overview](#overview)
- [Projects](#projects)
  - [01-Spring-Security-Basic-Demo](#01-spring-security-basic-demo)
  - [02-Login-Register-Demo](#02-login-register-demo)
  - [03-Login-Register-With-JWT-Demo](#03-login-register-with-jwt-demo)
  - [04-OAuth2-Authentication-Demo](#04-oauth2-authentication-demo) (Coming Soon)
  - [05-Refresh-Token-Implementation](#05-refresh-token-implementation) (Coming Soon)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Spring Security Core Concepts](#spring-security-core-concepts)
- [Best Practices](#best-practices)
- [Contributing](#contributing)
- [License](#license)

## Overview

Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications. This repository aims to provide clear examples of how to implement various security patterns with Spring Security, covering everything from basic setup to advanced features.

## Projects

### 01-Spring-Security-Basic-Demo

**Key Features:**
- Basic form-based authentication
- In-memory user management
- Basic authorization rules
- Custom login page
- Remember-me functionality
- CSRF protection

This project demonstrates the fundamental concepts of Spring Security with a simple web application. It's the perfect starting point for understanding how Spring Security works.

[Go to Basic Demo README](./01-Spring-Security-Basic-Demo/README.md)

### 02-Login-Register-Demo

**Key Features:**
- Database-based user authentication
- User registration with email verification
- Password encoding with BCrypt
- Role-based authorization
- Custom authentication success/failure handlers
- Password reset functionality
- Session management

This project builds upon the basic demo by adding user registration, database persistence, and more advanced authentication features.

[Go to Login-Register Demo README](./02-Login-Register-Demo/README.md)

### 03-Login-Register-With-JWT-Demo

**Key Features:**
- Stateless authentication with JWT (JSON Web Tokens)
- Token-based authentication flow
- JWT creation, validation, and parsing
- Securing REST endpoints with JWT
- Exception handling for token errors
- Custom security configurations
- User registration and authentication

This project demonstrates how to implement stateless authentication using JWT tokens, ideal for securing RESTful APIs and single-page applications.

[Go to JWT Authentication Demo README](./03-Login-Register-With-JWT-Demo/README.md)

### 04-OAuth2-Authentication-Demo (Coming Soon)

**Key Features:**
- OAuth2 authentication flow
- Social login (Google, GitHub, Facebook)
- OAuth2 client registration
- Custom OAuth2 user service
- Handling OAuth2 authentication success/failure
- Integration with JWT for API security
- Role mapping from OAuth providers

This project will demonstrate how to implement authentication using OAuth2, allowing users to log in using their accounts from various providers.

### 05-Refresh-Token-Implementation (Coming Soon)

**Key Features:**
- Refresh token mechanism with JWT
- Token revocation
- Token rotation
- Enhanced security measures
- Sliding session expiration
- Handling concurrent token usage
- Token blacklisting

This project will show how to implement a secure refresh token mechanism to enhance the security and user experience of JWT authentication.

## Prerequisites

To run these projects, you'll need:

- Java Development Kit (JDK) 17 or newer
- Maven 3.6+ or Gradle 7.0+
- Your preferred IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)
- Postman or similar tool for API testing
- Git for cloning the repository

## Getting Started

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/spring-security.git
   cd spring-security
   ```

2. Navigate to the project you want to explore:
   ```bash
   cd 01-Spring-Security-Basic-Demo
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   or with Gradle:
   ```bash
   ./gradlew bootRun
   ```

4. Access the application:
   - For web applications: http://localhost:8080
   - For API documentation: http://localhost:8080/swagger-ui.html (if available)

Refer to each project's individual README for specific setup instructions and details.

## Spring Security Core Concepts

### Authentication vs. Authorization

- **Authentication**: Verifying who a user is (login, token validation)
- **Authorization**: Determining what a user can do (permissions, roles)

### SecurityFilterChain

The core component that processes HTTP requests through a series of filters to apply security rules.

### Authentication Providers

Components that validate credentials and create authenticated users:
- DaoAuthenticationProvider
- JwtAuthenticationProvider
- OAuth2LoginAuthenticationProvider
- etc.

### UserDetailsService

Interface for loading user-specific data. Implementations:
- InMemoryUserDetailsManager
- JdbcUserDetailsManager
- Custom implementations

### PasswordEncoder

Interface for encoding passwords securely:
- BCryptPasswordEncoder
- Argon2PasswordEncoder
- Pbkdf2PasswordEncoder

## Best Practices

1. **Never store passwords in plain text** - Always use strong password encoders
2. **Use HTTPS in production** - Encrypt all traffic to prevent MITM attacks
3. **Apply principle of least privilege** - Grant only the permissions needed
4. **Implement proper token validation** - Validate JWT signatures, expiration, etc.
5. **Set appropriate token expiration** - Balance security and user experience
6. **Use refresh tokens wisely** - Implement proper rotation and revocation
7. **Protect against common vulnerabilities** - CSRF, XSS, CORS, etc.
8. **Implement rate limiting** - Protect against brute force and DoS attacks
9. **Log security events** - Track authentication failures, access denials, etc.
10. **Keep dependencies updated** - Regularly update libraries to fix security vulnerabilities

## Contributing

Contributions are welcome! If you'd like to contribute:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Make your changes
4. Commit your changes (`git commit -m 'Add some feature'`)
5. Push to the branch (`git push origin feature/your-feature`)
6. Open a Pull Request

Please ensure your code follows the existing style and includes proper documentation.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Created and maintained by [Your Name]

Last updated: October 2025
