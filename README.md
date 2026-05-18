Event Ticketing Platform

A microservices-based Event Ticketing Platform built for the Enterprise System Integration project.

The system includes:

- Event Service
- Venue Service
- API Gateway
- Vue Frontend
- PostgreSQL Database
- Docker Compose deployment



Event Service
Docker image: mohoshena/event-service:latest
Port: 8083

Endpoints:
GET    /events
POST   /events
GET    /events/{id}
PUT    /events/{id}
PATCH  /events/{id}/status
PUT    /events/{id}/venue?venueId={venueId}

Needs database:
PostgreSQL

Notes:
Event Service communicates with Venue Service when assigning a venue to an event.


Venue Service
Docker image: mohoshena/venue-service:latest
Port: 8084

Endpoints:
GET    /venues
POST   /venues
GET    /venues/{id}
PUT    /venues/{id}
DELETE /venues/{id}

Needs database:
PostgreSQL


















































The frontend communicates with backend services through the API Gateway. The Event Service communicates with the Venue Service to assign venues to events.
Technologies Used
Backend
Java 17
Spring Boot
Spring Web
Spring Data JPA
PostgreSQL
Maven
Spring Cloud Gateway
Frontend
Vue.js
Vite
JavaScript
HTML/CSS
Infrastructure
Docker
Docker Compose
PostgreSQL
Services
Event Service

The Event Service manages event-related functionality.

Direct service URL:

http://localhost:8083

Gateway URL:

http://localhost:8080/events

Main endpoints:

GET    /events
POST   /events
GET    /events/{id}
PUT    /events/{id}
PATCH  /events/{id}/status
PUT    /events/{id}/venue?venueId={venueId}

The following endpoint demonstrates service-to-service communication:

PUT /events/{id}/venue?venueId={venueId}

This endpoint assigns an existing venue to an existing event.

Venue Service

The Venue Service manages venue-related functionality.

Direct service URL:

http://localhost:8084

Gateway URL:

http://localhost:8080/venues

Main endpoints:

GET    /venues
POST   /venues
GET    /venues/{id}
PUT    /venues/{id}
DELETE /venues/{id}
API Gateway

The API Gateway is the central entry point for frontend and API requests.

Gateway URL:

http://localhost:8080

Gateway routes:

/events/**  -> event-service
/venues/**  -> venue-service

The frontend communicates only with the API Gateway.

Frontend

The frontend is a Vue application.

Frontend URL:

http://localhost:5173

Frontend features:

Dashboard
Venue Management
Event Management
Service Integration view


Database container:

postgres-db

Exposed port:

localhost:5432

The backend services connect to PostgreSQL through Docker Compose.

Running the Project
Prerequisites

Install:

Docker
Docker Compose
Java 17
Maven
Node.js
Git
Start the Full System

From the project root:

docker compose up --build

This starts:


Frontend
http://localhost:5173
API Gateway
http://localhost:8080
Event Service Swagger
http://localhost:8083/swagger-ui/index.html
Venue Service Swagger
http://localhost:8084/swagger-ui/index.html
Event API Through Gateway
http://localhost:8080/events
Venue API Through Gateway
http://localhost:8080/venues


