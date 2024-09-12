# Auth Service API

This project is a Spring Boot-based authentication service that supports user registration, login,
and JWT-based authentication. It allows users to register with a username, password, and role and
provides JWT tokens for secure access to protected endpoints.

## Features

- User registration with role assignment
- User login and JWT token generation
- JWT-based stateless authentication
- MySQL database integration with Docker support

## Stack Used

- Java 21
- Spring Boot
- Spring Security
- JWT
- MySQL
- Docker and Docker Compose

## Getting Started

```bash
  git clone git@github.com:mcoldibelli/spring-security
```

Create a .env file in the root of the project, follow the .env.example in order to setup.

Run the containers

```bash
  docker-compose up -d
```

This will:

	•	Start a MySQL database container
	•	Build the Spring Boot application and run it in a container
	•	Map port 8080 for the application and port 3306 for the database [IF DEFAULT]

## API Docs

### Create a new user

```http
  POST /auth/register
```

Response Body:

    {
        "token": "your-jwt-token-here"
    }

• 200 OK: User registered successfully

• 400 Bad Request: User already exists



----

### Sign In with a user

```http
  POST /auth/login
```

Request Body:

    {
        "login": "user123",
        "password": "strongPassword123"
    }

Response Body:

    {
        "token": "your-jwt-token-here"
    }

• 200 OK: Authentication successful

• 403 Forbidden: Invalid credentials