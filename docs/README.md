# Endterm Project — Spring Boot REST API with Design Patterns

## Project Overview
This project is a Spring Boot RESTful API developed as a final endterm assignment.  
It integrates *Design Patterns, **SOLID principles, **Component Principles, and **JDBC database access* into a clean layered backend architecture.

The system manages *tickets* with different types (STANDARD, VIP) and demonstrates a complete CRUD REST API with proper validation and error handling.

---

## Technology Stack
- Java 17+
- Spring Boot
- Spring Web
- Spring JDBC
- PostgreSQL
- Gradle
- Postman
- PlantUML

---

## REST API Documentation

### Base URL

http://localhost:8080


### Health Check
*GET*

/health


*Response*
json
OK


---

### Get All Tickets
*GET*

/api/tickets


*Response*
json
[
  {
    "id": 1,
    "customerId": 2,
    "movieId": 101,
    "type": "VIP",
    "basePrice": 2000.0,
    "finalPrice": 3000.0
  }
]


---

### Get Ticket by ID
*GET*

/api/tickets/{id}


*Response*
json
{
  "id": 1,
  "customerId": 2,
  "movieId": 101,
  "type": "VIP",
  "basePrice": 2000.0,
  "finalPrice": 3000.0
}


---

### Create Ticket
*POST*

/api/tickets


*Request Body*
json
{
  "customerId": 2,
  "movieId": 101,
  "type": "VIP",
  "basePrice": 2000
}


*Response (201 Created)*
json
{
  "id": 16,
  "customerId": 2,
  "movieId": 101,
  "type": "VIP",
  "basePrice": 2000.0,
  "finalPrice": 3000.0
}


---

### Delete Ticket
*DELETE*

/api/tickets/{id}


*Response*

204 No Content


---

## Error Handling

The API uses *global exception handling*.

Example error response:
json
{
  "timestamp": "2026-02-08T02:24:54.545059700Z",
  "status": 400,
  "error": "Bad Request",
  "message": "basePrice must be > 0"
}


Handled errors:
- 400 Bad Request — invalid input
- 404 Not Found — resource not found
- 409 Conflict — duplicate ticket
- 500 Internal Server Error — unexpected error

---

## Design Patterns

### Singleton Pattern
*Class:* DatabaseConfig  
*Purpose:*  
Ensures a single shared database configuration instance across the application.

---

### Factory Pattern
*Class:* TicketFactory  
*Purpose:*  
Creates subclasses of the abstract base class TicketBase:
- StandardTicket
- VipTicket

The factory always returns the base type (TicketBase) and supports easy extension.

---

### Builder Pattern
*Class:* TicketBuilder  
*Purpose:*  
Constructs complex ticket objects with validation and optional fields using a fluent API.

---

## Component Principles

### REP (Reuse / Release Equivalence Principle)
Reusable modules are grouped by responsibility:
- controller
- service
- repository
- patterns
- model

---

### CCP (Common Closure Principle)
Classes that change together are packaged together:
- Ticket-related logic inside service and repository
- Pattern implementations inside patterns

---

### CRP (Common Reuse Principle)
Modules depend only on what they use:
- Controllers depend only on services
- Services depend only on repositories and factories

---

## SOLID Principles

- *Single Responsibility* — each class has one clear responsibility
- *Open/Closed* — new ticket types can be added without modifying existing logic
- *Liskov Substitution* — StandardTicket and VipTicket substitute TicketBase
- *Interface Segregation* — clean DTOs for API communication
- *Dependency Inversion* — service depends on abstractions, not implementations

---

## Database Schema

*Table: tickets*

| Column        | Type      |
|--------------|-----------|
| id           | BIGSERIAL |
| customer_id  | BIGINT    |
| movie_id     | BIGINT    |
| type         | VARCHAR   |
| base_price   | DOUBLE    |
| final_price  | DOUBLE    |

---

## System Architecture

Layered architecture:

Controller → Service → Repository → Database


Design patterns are integrated inside the domain and infrastructure layers.

---

## UML Diagram
![UML Diagram](uml/uml.png)

---

## How to Run the Application

1. Clone the repository
2. Create PostgreSQL database:

endterm_db

3. Configure database credentials in DatabaseConfig
4. Run the application:

./gradlew bootRun

5. Test endpoints using Postman

---

## Postman Screenshots

### Create ticket (POST)
![POST](screenshots/post-ticket.png)

### Get all tickets (GET)
![GET ALL](screenshots/get-all.png)

### Get ticket by id
![GET BY ID](screenshots/get-by-id.png)

### Delete ticket
![DELETE](screenshots/delete.png)

### update ticket error
![409](screenshots/UPDATE.png)

---

## Reflection

This project demonstrates how classical design patterns and component principles can be integrated into a modern Spring Boot REST API.  
It improved understanding of layered architecture, SOLID design, and real-world backend development practices.

---
---

# Bonus Task — Caching Layer (Simple In-Memory Cache)

## 1. Objective
This bonus task enhances application performance by introducing a *simple in-memory caching layer* for frequently accessed data.  
The goal is to reduce repetitive database queries and improve response time for common read operations.

---

## 2. Requirements Mapping (Checklist)

###  Implement a simple in-memory cache
- Implemented TicketCache that stores data *in memory* using:
  - List<TicketResponse> for getAll() results
  - Map<Long, TicketResponse> for getById(id) results

### Cache the result of at least one commonly used method
- Cached method:
  - TicketService.getAll() → endpoint: GET /api/tickets

###  Repeated calls return cached data instead of querying database
- TicketService.getAll() first checks cache:
  - If cached value exists → returns it immediately
  - Otherwise → queries DB, then stores result in cache

###  Properly implement Singleton pattern
- TicketCache is implemented as a *Singleton* (static holder idiom), ensuring:
  - exactly *one cache instance*
  - global shared access across the application runtime

###  Provide a cache clearing / invalidation mechanism
Two mechanisms are supported:

1) *Automatic invalidation*
- Cache is invalidated after:
  - POST /api/tickets
  - PUT /api/tickets/{id}
  - DELETE /api/tickets/{id}

2) *Manual cache clear*
- Implemented endpoint:
  - DELETE /api/cache/tickets

---

## 3. Design Constraints Compliance

### In-memory only (Map or similar)
- Cache is stored fully in memory via ConcurrentHashMap + List.

### Only one cache instance exists (Singleton)
- Singleton guarantees a single cache instance per application runtime.

### SOLID Principles
- *SRP (Single Responsibility):*
  - TicketCache handles only caching responsibilities.
  - TicketService handles business logic and coordinates cache usage.
- *OCP (Open/Closed):*
  - Cache can be extended (more keys/strategies) without changing repository logic.
- *DIP / Layered integrity:*
  - Repository layer remains unchanged and unaware of caching.

### Does not break layered architecture
- Controller → Service → Repository structure is preserved.
- Cache is managed inside service layer; repository still performs DB access only.

---

## 4. Endpoints Summary

### Cached endpoint
- GET /api/tickets  
  - First call: DB query, then cached  
  - Next calls: returned from cache

### Manual cache clear endpoint
- DELETE /api/cache/tickets  
  - Clears all cached entries

### Automatic invalidation triggers
- POST /api/tickets
- PUT /api/tickets/{id}
- DELETE /api/tickets/{id}

---

## 5. How to Test (Demonstration)

1) Call:
http
GET /api/tickets

Expected: data fetched from DB and cached.

2) Call again:
http
GET /api/tickets

Expected: data returned from cache (no DB query).

3) Perform update/delete/create:
http
POST /api/tickets
PUT /api/tickets/{id}
DELETE /api/tickets/{id}

Expected: cache invalidated automatically.

4) Manually clear cache:
http
DELETE /api/cache/tickets

Expected: cache cleared.

---

## 6. Technical Summary

- *Cache type:* Simple in-memory cache
- *Data structures:* List, ConcurrentHashMap
- *Singleton:* Static holder pattern
- *Cached method:* TicketService.getAll()
- *Invalidation:* Automatic + Manual endpoint
- *Architecture:* Layered architecture preserved

---
