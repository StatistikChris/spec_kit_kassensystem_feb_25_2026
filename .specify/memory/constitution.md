# Kassensystem Constitution

## Core Principles

### I. Domain Integrity First
Every feature must preserve the integrity of the cash-register domain.
Business rules (price calculation, tax handling, receipt generation, shift management) are non-negotiable invariants.
No shortcut may violate fiscal correctness — if in doubt, treat it as a compliance issue.

### II. Compliance by Design (KassenSichV / TSE)
The system must be compliant with the German Kassensicherungsverordnung (KassenSichV) and support TSE (Technische Sicherheitseinrichtung) integration.
Every transaction must be immutably logged with a tamper-proof audit trail.
Receipt data must meet the legally required fields (TSE-Signatur, Kassennummer, Transaktionsnummer, Zeitstempel).

### III. Test-First (NON-NEGOTIABLE)
TDD is mandatory: tests are written → approved → fail → then implementation follows.
Red-Green-Refactor cycle strictly enforced.
Every business-logic module must have unit tests covering the golden path, edge cases, and error states.

### IV. Separation of Business Logic and I/O
Business logic (pricing, taxes, discounts, receipt composition) lives in pure, side-effect-free modules.
I/O (hardware peripherals, UI, networking, persistence) is strictly separated.
This enables independent testing of core logic without hardware or database dependencies.

### V. Observability and Auditability
Every transaction, void, refund, and shift-close event must produce a structured log entry.
Errors must go to a dedicated error log (not silently swallowed).
Provide human-readable and machine-readable (JSON) output formats for all reports.

### VI. Simplicity and YAGNI
Start with the minimal viable feature; no speculative abstractions.
Complexity must be explicitly justified in the spec.
Prefer clear, readable code over clever optimisations unless performance is a documented requirement.

## Technical Constraints

- Transactions must be atomic: either fully committed or fully rolled back — no partial states.
- All monetary values are stored and calculated as integer cents (or the smallest currency unit) — no floating-point arithmetic.
- Date/time handling uses UTC internally; local time (Europe/Berlin) is used only for display and legal receipt output.
- Hardware peripheral communication (receipt printer, barcode scanner, cash drawer) is abstracted behind interfaces / adapters.
- The system must operate offline-first; cloud sync is a secondary concern.

## Development Workflow

- Every feature begins with a `/specify` spec, then `/plan`, then `/tasks`, then `/implement`.
- PRs must include: passing tests, updated spec (if behaviour changed), and a checklist sign-off.
- Breaking changes to the transaction or receipt model require a migration plan documented in the spec.
- No direct commits to `main`; all work goes through feature branches named `feature/NNN-short-description`.

## Governance

This constitution supersedes all other practices and conventions.
Amendments require: documentation of the change, rationale, and (if applicable) a migration plan.
All pull request reviews must verify compliance with this constitution.
Complexity must be justified; simplicity is the default.

**Version**: 1.0.0 | **Ratified**: 2026-02-25 | **Last Amended**: 2026-02-25
