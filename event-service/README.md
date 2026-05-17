# Event Management Service
This project implements the **Event Management microservice** for the Event Ticketing System, as defined in Assignment 3.

The service follows a **Spring Boot layered architecture** and provides REST APIs to manage events, including creation, retrieval, updates, and venue assignment.

## Responsibilities
This service is implemented by **Mohoshena Akhter** as part of the Enterprise System Integration project.

## Architecture

The application follows a standard layered architecture:

Client → Controller → Service → Repository → Database

Controller Layer: Handles HTTP requests and responses
Service Layer: Contains business logic
Repository Layer: Handles database operations
DTO Layer: Separates API from internal model
Domain Model: Represents event entity

## Technologies Used

* Java 17
* Spring Boot 3
* Spring Web
* Spring Data JPA
* PostgreSQL
* Swagger / OpenAPI
* Maven
* JUnit & MockMvc

---
Features (Endpoints)

Create Event

POST `/events`

 Get All Events

GET `/events`

 Get Event by ID

GET `/events/{id}`

Update Event

PUT `/events/{id}`

 Update Event Status

PATCH `/events/{id}/status`

 Assign Venue

PUT `/events/{id}/venue`


Example Request (POST /events)

```json
{
  "title": "Concert",
  "description": "Music event",
  "category": "Music",
  "organizerId": 1,
  "startDateTime": "2026-05-10T18:00:00",
  "endDateTime": "2026-05-10T22:00:00",
  "posterUrl": "test.jpg"
}
```

Database

* PostgreSQL is used for persistence
* Each service manages its own database
  
---
 How to Run

1. Ensure PostgreSQL is running
2. Configure database in `application.properties`
3. Run the application:

```
mvn spring-boot:run
```

4. Server runs on:

```
http://localhost:8083
```

---

 API Documentation (Swagger)

Open in browser:

```
http://localhost:8083/swagger-ui.html
```

Swagger provides:

* Endpoint documentation
* Request/response testing

---

 Testing

Controller-level tests are implemented using:

* `@WebMvcTest`
* Mocked service layer

Run tests:

```
mvn test
```

Expected result:

```
BUILD SUCCESS
```

---

API Testing (Postman)

You can test APIs using Postman.

Example:

POST `http://localhost:8083/events`

Use JSON body as shown above.

---

Project Structure

```
src/
 ├── main/
 │    ├── java/ee/ut/eventticketing/event/
 │    │     ├── controller/
 │    │     ├── service/
 │    │     ├── repository/
 │    │     ├── dto/
 │    │     ├── model/
 │    │
 │    └── resources/
 │          └── application.properties
 │
 └── test/
       └── java/.../EventControllerTest.java
```

---
Checkpoint 1 Requirements Coverage

-Running service
- REST API endpoints
- PostgreSQL integration
- Swagger documentation
- Controller-level testing
- API demonstration via Swagger/Postman

---

## Notes

* This implementation covers **Checkpoint 1 (Backend Service)**
* Docker, frontend, security, and async communication are part of later checkpoints

---

Use of AI Tools:

-Clarifying assignment requirements
-Understanding Spring Boot concepts and configurations
-Generating basic documentation structure
-Documentation 


