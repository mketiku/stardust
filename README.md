# Office Booking System

This project is a refined exercise in **Test-Driven Development (TDD)**, focused on solving core domain challenges in a shared workspace environment. It demonstrates how to build a reliable, thread-safe reservation system using a clean, test-first approach.

## The Mission

The primary goal of this iteration was to transition from a permissive "trust-based" booking logic to an "enforced-rule" system. We addressed three critical engineering requirements:

1.  **Floor-Based Permissions**: Mapping organizational structure to physical space by restricting desk bookings to specific department zones.
2.  **Double-Booking Integrity**: Ensuring that state transitions (booking a desk) are guarded by strict occupancy checks.
3.  **Race Condition Mitigation**: Leveraging Optimistic Locking via JPA `@Version` to handle high-concurrency scenarios where multiple users might attempt to reserve the same desk simultaneously.

## Engineering Stack

Chosen for type safety, performance, and developer experience:

- **Language**: Kotlin 1.9.25 (Expressive, null-safe, and concise)
- **Framework**: Spring Boot 3.4.1 (The industry standard for JVM microservices)
- **Data Layer**: Spring Data JPA with H2 (Enabling fast, in-memory feedback loops during development)
- **Testing Philosophy**: JUnit 5 & MockK (Focusing on behavior verification and isolation)

## System Architecture & Patterns

The implementation follows standard Clean Architecture principles:

- **Entities**: Foundational data models (`Desk`, `Employee`) that encapsulate core attributes.
- **Service Layer**: Where the business rules live. Our `BookingService` is designed to be transactional and atomic.
- **Controller Layer**: A clean REST interface (`/api/bookings`) that bridges external requests to our internal domain logic.

## The TDD Workflow

This project wasn't just built with tests; it was *driven* by them. The development cycle followed the strict Red-Green-Refactor loop:

1.  **Red**: Defined the expected behavior in `BookingServiceTest.kt` (e.g., preventing unauthorized departmental bookings).
2.  **Green**: Implemented the minimum viable logic in `BookingService.kt` to satisfy the failing tests.
3.  **Refactor**: Cleaned up the implementation while maintaining a passing test suite.

## Getting Started

### Prerequisites
Construction and execution require **JDK 21**.

### Setup
```bash
# Clone the repository
git clone <repository-url>
cd office-booking

# Execute the full test suite to verify the domain logic
./gradlew test

# Launch the application locally
./gradlew bootRun
```

## Logic Snapshot: `reserveDesk`

The core logic handles the following check-list before persisting state:
- **Existence**: Does the desk/employee exist?
- **Occupancy**: Is the desk currently free?
- **Authorization**: Does the employee's department match the desk's designated zone?
- **Concurrency**: Did another thread modify this desk while we were processing? (Managed by JPA Versioning)
