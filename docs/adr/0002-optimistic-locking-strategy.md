# ADR 0002: Optimistic Locking Strategy

## Status
Accepted

## Context
Concurrent starship docking requests are a high-risk scenario. Without concurrency guards, the station could suffer from "Double Docking" errors where two starships are assigned to the same physical bay.

## Decision
We will use **Optimistic Locking** via JPA's `@Version` annotation on the `DockingBay` entity.
- The `DockingBay` entity holds a `version` field.
- Hibernate automatically checks this field before every update.
- If a version mismatch is detected (meaning another sector update occurred), an `OptimisticLockingFailureException` is thrown.

## Consequences
- **Pros**: 
  - Non-blocking: Multiple starships can request scans simultaneously.
  - Scale-friendly: No database-level row-locking required for read-heavy docking scans.
- **Cons**: 
  - The "Arrival Scan" must be retried or the starship must wait if a conflict occurs.
