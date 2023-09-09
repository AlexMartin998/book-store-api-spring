# Spring Boot 3.0 Security with JWT Implementation

This project demonstrates the implementation of security using Spring Boot 3.0 and JSON Web Tokens (JWT). It includes the following features:

## Features

⚡️ User registration and login with JWT authentication\
⚡️ Password encryption using BCrypt\
⚡️ Role-based authorization with Spring Security\
⚡️ Customized access denied handling\
⚡️ Role ManyToMany User

## Technologies

- Spring Boot 3.0.10
- Spring Security
- JSON Web Tokens (JWT)
- BCrypt
- Maven
- PostgreSQL

## Getting Started

To get started with this project, you will need to have the following installed on your local machine:

- JDK 8+
- Maven 3+

To build and run the project, follow these steps:

- Clone the repository: `https://github.com/AlexMartin998/inventory-management-springboot3.git`

### Local Environment

- Build the project: `mvn clean install`
- Run the project: `mvn spring-boot:run`
- Run DB:

```bash
docker compose -f docker-compose.dev.yml up
```

- Connect to a DB:
  - `application.properties`
    - Use Docker properties

### With Docker

#### Recommended option

```bash
docker compose up
```

#### Other way

Build JAR

```bash
mvn clean package -DskipTests
```

Run Docker Compose

- First of all, set the corresponding configuration in the `Dockerfile`

```bash
docker compose up
```

App will be running on PORT `3000`

- Consume API with:

```
http://localhost:3000
```

### Running the DB

Create the required volumes (if external is set to true)

```bash
docker volume create spring_postgresql
docker volume create spring_pgadmin
docker volume create spring_mysql
```

Run DB

```bash
docker compose -f docker-compose.dev.yml up --build -d
```

The application will be available at:

```bash
http://localhost:3000
```

PgAdmin:

```bash
http://localhost:8090
```

Connect to DB with TablePlus

```bash
# DB HOST
127.0.0.1

# DB PORT
5432

# DB USER
postgres

# DB PASSWORD
postgres
```
