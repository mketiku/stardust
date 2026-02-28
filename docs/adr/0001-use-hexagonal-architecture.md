# ADR 0001: Use Hexagonal Architecture (Ports & Adapters)

## Status
Accepted

## Context
The office booking system requires high maintainability and testability. Traditional layered architectures often lead to tight coupling between business logic and infrastructure (e.g., Spring Data or Web).

## Decision
We will use **Hexagonal Architecture**. 
- The **Domain** contains pure business logic and model entities, having zero dependencies on external frameworks.
- **Ports** (interfaces) define how the Domain interacts with the outside world.
- **Adapters** (infrastructure implementations) bridge the gap to actual technologies like JPA, REST Controllers, or external APIs.

## Consequences
- **Pros**: 
  - Business logic is testable without starting a Spring Context.
  - Data storage or API delivery mechanisms can be swapped with minimal impact.
  - Architectural boundaries are strictly enforced via ArchUnit.
- **Cons**: 
  - Higher initial boiler-plate (interfaces, DTO mappings).
  - Learning curve for developers used to simple MVC.
