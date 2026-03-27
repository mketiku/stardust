# Professional Engineering Standards

This guide defines the architectural and code quality standards for this repository. Adherence ensures maintainability, scalability, and high quality.

## 1. Architectural Integrity (Hexagonal/Ports & Adapters)
- **Domain Isolation**: The core logic must reside in the `domain` package and be 100% agnostic of external frameworks (Spring, JPA, Web).
- **Dependency Flow**: Dependencies must point inward toward the Domain. External layers communicate via defined **Ports** (interfaces) in the domain.
- **Enforcement**: Run `ArchitectureTest` (ArchUnit) after any structural change to verify boundaries.

## 2. Concurrency & State Safety
- **Anti-Pattern**: Avoid global state or uncontrolled shared-mutable state.
- **Immutability & Snapshotting**: Always prefer immutable data structures (Kotlin `data class`). Use the `.copy()` pattern for all state transitions to ensure atomic, thread-safe updates and clear history tracking.
- **Optimistic Locking**: Use `@Version` (JPA) for any high-contention resource state changes to prevent "Lost Updates."
- **Transactional Atomicity**: Business operations that span multiple modifications must be wrapped in a single transaction.

## 3. Data Integrity & API Contracts
- **DTO Pattern**: Internal persistence entities MUST NOT leak to the web layer. Always map to discrete `Response` and `Request` DTOs.
- **Audit Trails**: Capture meaningful state transitions (e.g., creation, updates) rather than just overriding fields.
- **Fail Fast**: Implement strict validation on all incoming requests before they reach the service layer.

## 4. Workflow & Automation
- **Format Before Finishing**: Always run `./gradlew ktlintFormat` as the final step of any implementation task.
- **Verify Before Pushing**: Ensure all tasks pass `./gradlew check` to verify both formatting and tests.
- **Editor Synchronization**: Use the project's `.editorconfig` to ensure local IDEs match the CI linting rules.

## 5. Operational Excellence
- **Observability**: Instrument key business events using metrics (e.g., Micrometer) and structured logging.
- **Clean Commits**: Use short, lowercase prefixes: `feat:`, `fix:`, `test:`, `arch:`, `sec:`, or `docs:`.
- **Shift-Left Security**: Ensure high-severity dependencies are flagged in CI/CD.

## 6. Implementation Workflow
1.  **Red-Green-Refactor**: Prioritize TDD for complex domain logic.
2.  **ArchUnit Alignment**: Verify that new dependencies don't violate layer isolation.
3.  **Proactive Verification**: Run `./gradlew test ktlintCheck` before finalizing any change.
