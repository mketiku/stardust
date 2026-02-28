# ADR 0002: Optimistic Locking Strategy

## Status
Accepted

## Context
Concurrent desk bookings are highly likely in an office booking system. Without concurrency guards, the system is susceptible to race conditions (e.g., two users booking the same desk simultaneously).

## Decision
We will use **Optimistic Locking** via JPA's `@Version` annotation on the `Desk` entity.
- The `Desk` entity holds a `version` field.
- Hibernate automatically checks this field before every update.
- If a version mismatch is detected at the database level, an `OptimisticLockingFailureException` is thrown.

## Consequences
- **Pros**: 
  - Non-blocking: Multiple users can read and prepare bookings at once.
  - Scale-friendly: No horizontal table-level locking required.
  - Fail-safe: Detects conflicts at save-time.
- **Cons**: 
  - UX Impact: Users who "lose the race" will need to retry or be notified of the failure.
  - Requires explicit `@Version` handling in the entity.
