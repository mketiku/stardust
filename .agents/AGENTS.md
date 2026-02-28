# Agent Development Best Practices

This repository serves as a baseline for high-quality, agent-driven engineering. We prioritize **Test-Driven Development (TDD)** and **Domain Integrity**.

## Repository Context

The **Office Booking System** is a JVM-based microservice built with **Kotlin** and **Spring Boot**. It manages physical workspace allocations (desks) for employees, ensuring departmental constraints and preventing concurrent booking conflicts.

### Key Architectural Decisions
- **TDD First**: Every feature begins with a failing test case. We maintain two types of tests:
    - **Service Tests**: Mock-based unit tests for core domain logic.
    - **Controller Tests**: `WebMvcTest` for API contract verification and exception mapping.
- **Optimistic Locking**: We use JPA `@Version` on the `Desk` entity to handle race conditions in a distributed-friendly way.
- **Transactional Atomicity**: State transitions (e.g., booking a desk *and* creating an audit record) are wrapped in `@Transactional` to prevent partial updates.
- **Custom Domain Exceptions**: We use specific exceptions like `DepartmentMismatchException` to differentiate between client errors and system failures.

## Best Practices for AI Agents

When contributing to this codebase, follow these standards:

### 1. The Red-Green-Refactor Loop
Always implement features in this order:
1. Create/Update a test in `src/test/kotlin` that fails for the new requirement.
2. Modify the `Service` or `Repository` to satisfy the test.
3. Refactor for clarity and performance.

### 2. Validation & Security
- **Never Trust the Request Body**: Always fetch entities by ID from the database to verify their existence and current state before acting.
- **Departmental Authorization**: Ensure cross-entity constraints (like Employee Department vs. Desk Zone) are checked in the service layer.
- **Safe Error Responses**: Use a `GlobalExceptionHandler` to map internal exceptions to clean, standardized HTTP responses. Avoid leaking stack traces.

### 3. Entity Integrity & Hexagonal Architecture
- **Hexagonal Architecture (Ports & Adapters)**: We separate the **Domain** (logic/ports) from **Infrastructure** (JPA/Web).
    - **Domain Port**: Define interfaces in `com.example.office.domain.port`.
    - **Adapter**: Implement database-specific or web-specific logic in `com.example.office.infrastructure`.
- **DTO Pattern**: Never expose database entities directly to the API. Always map domain models to DTOs (e.g., `DeskResponse`) in the controller or a mapper.
- **Use Audit Trails**: Create an explicit `Booking` record when a desk is reserved to provide a historical log.
- **Audit Domain Events**: Use Spring `ApplicationEventPublisher` to emit `DomainEvents` for side effects (e.g., async auditing, notifications). This keeps the core service decoupled from infrastructure-heavy tasks (Item 1 & 3 pattern).
- **Prefer Lazy Loading**: Use `FetchType.LAZY` for entity relationships to avoid large memory footprints.

### 4. Code Quality
- **Type Safety**: Leverage Kotlin's null safety and strong typing.
- **Succinct Commits**: Group related changes (Entity + Service + Test) into single atomic PRs/commits.
- **Commit Suggestions**: Always suggest a succinct, lowercase git commit message (following Conventional Commits) upon completion of a task.
