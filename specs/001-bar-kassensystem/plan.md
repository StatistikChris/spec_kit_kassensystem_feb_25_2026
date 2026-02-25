# Implementation Plan: Bar-Kassensystem

**Branch**: `001-bar-kassensystem` | **Date**: 2026-02-25 | **Spec**: [spec.md](spec.md)
**Input**: Feature specification from `/specs/001-bar-kassensystem/spec.md`

## Summary

Build a fully KassenSichV/GoBD/AO-compliant bar POS (Kassensystem) as a touch-optimised Android tablet app. The system covers 7 prioritised user stories (Kassieren, Tischverwaltung, Tagesabschluss, Happy Hour, Schichtwechsel, Produktvarianten, Lagerverwaltung) backed by 26 functional requirements and 7 success criteria. The technical approach uses Native Android (Kotlin + Jetpack Compose) with on-device Room/SQLite persistence and Deutsche Fiskal Cloud-TSE (DFKA REST API) for tamper-proof transaction signing.

## Technical Context

**Language/Version**: Kotlin 2.0, JVM 17  
**Primary Dependencies**: Jetpack Compose BOM 2025.x, Room 2.7, Hilt 2.55, Retrofit 2.11, WorkManager 2.10, Kotlin Coroutines 1.9, DataStore 1.1  
**Storage**: Room/SQLite (on-device, WAL mode); no cloud database  
**Testing**: JUnit 5, Robolectric, Compose Test, MockK  
**Target Platform**: Android 10+ (API 29+), 10" landscape tablet  
**Performance Goals**: Standard transaction < 30 s (SC-001); DSFinV-K export < 5 min (SC-003)  
**Constraints**: ≥ 24 h offline-capable (SC-004); 10-year data retention (SC-006); single-tablet deployment  
**Scale/Scope**: Single Android app; single operator tablet; Deutschland / EUR; Gesetzesstand 2026-02-25

## Constitution Check

| Principle | Status | Notes |
|-----------|--------|-------|
| Domain Integrity First | ✅ | Business rules in pure domain layer; UI never touches Room directly |
| Compliance by Design (KassenSichV / TSE) | ✅ | Every `Transaktion` mandatorily signed; `locked` flag enforces immutability after Z-Bon |
| Test-First (NON-NEGOTIABLE) | ✅ | Unit tests for PreisregelEngine, ZBon aggregation, DsFinVKExport; integration tests with in-memory Room |
| Separation of Business Logic and I/O | ✅ | `domain/` layer is pure Kotlin; `data/` and `ui/` are I/O layers; TSE client behind interface |
| Observability and Auditability | ✅ | Structured `TseProtokollEintrag` per transaction; dedicated error log; human- and machine-readable reports |
| Simplicity and YAGNI | ✅ | On-device only (no cloud sync); no speculative abstractions beyond what spec requires |

**Technical Constraints (Constitution)**:

| Constraint | Implementation |
|-----------|---------------|
| Atomic transactions | Room `@Transaction` wraps every Transaktion + Positionen save |
| Integer cents only | All `Betrag` fields are `Long` (Eurocent); no `Float`/`Double` anywhere in domain |
| UTC internally, Europe/Berlin for display | `Instant` in DB; `ZonedDateTime(Europe/Berlin)` for bon output |
| Hardware behind interfaces | `BonDrucker`, `TseClient`, `CashDrawer` are interfaces; concrete adapters in `data/hardware/` |
| Offline-first | TseOfflineBuffer + WorkManager; data never lost if TSE unreachable |
| `allowBackup=false` | Protects GoBD immutability; DSFinV-K export is the archival mechanism |

## Project Structure

### Documentation (this feature)

```text
specs/001-bar-kassensystem/
├── spec.md
├── plan.md              ← this file
├── tasks.md
└── checklists/
    └── requirements.md
```

### Source Code

```text
android/                              ← Android Gradle project root
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   └── java/de/barpos/kassensystem/
│       │       ├── KassenApp.kt              ← Application class (Hilt)
│       │       ├── data/
│       │       │   ├── db/
│       │       │   │   ├── KassenDatabase.kt  ← Room DB (WAL, 12 entities)
│       │       │   │   ├── entity/            ← Room @Entity classes
│       │       │   │   ├── dao/               ← Room @Dao interfaces
│       │       │   │   └── converter/         ← TypeConverters (Instant, JSON)
│       │       │   ├── hardware/
│       │       │   │   ├── TseClientImpl.kt   ← Deutsche Fiskal DFKA REST
│       │       │   │   └── BonDrucker.kt      ← ESC/POS TCP adapter
│       │       │   ├── repository/            ← Repository implementations
│       │       │   └── worker/
│       │       │       ├── TseOfflineWorker.kt
│       │       │       └── DsFinVKExportWorker.kt
│       │       ├── domain/
│       │       │   ├── model/                 ← Pure Kotlin domain models
│       │       │   ├── repository/            ← Repository interfaces
│       │       │   ├── usecase/               ← Use cases (one per action)
│       │       │   └── engine/
│       │       │       └── PreisregelEngine.kt ← Pure pricing logic
│       │       ├── ui/
│       │       │   ├── theme/                 ← MaterialTheme, colours, typography
│       │       │   ├── kasse/                 ← KassenScreen (US-1)
│       │       │   ├── tisch/                 ← TischScreen (US-2)
│       │       │   ├── abschluss/             ← TagesabschlussScreen (US-3)
│       │       │   ├── preisregel/            ← Happy Hour admin (US-4)
│       │       │   ├── schicht/               ← Schichtwechsel (US-5)
│       │       │   ├── varianten/             ← Variant picker (US-6)
│       │       │   ├── lager/                 ← LagerScreen (US-7)
│       │       │   ├── export/                ← DSFinV-K export UI
│       │       │   ├── settings/              ← SettingsScreen
│       │       │   └── nav/                   ← NavHost, NavGraph
│       │       └── di/                        ← Hilt @Module classes
│       ├── test/                              ← JVM unit tests (JUnit5 + MockK)
│       └── androidTest/                       ← Compose/Room integration tests
├── build.gradle.kts
├── settings.gradle.kts
├── gradle/libs.versions.toml                  ← Version catalog
└── proguard-rules.pro
```

## Architecture

**Pattern**: Clean Architecture — three layers with strict dependency rule (UI → Domain ← Data).

```
UI layer          ← Jetpack Compose screens + ViewModels (using StateFlow)
    ↓ calls
Domain layer      ← Use cases + repository interfaces + domain models + PreisregelEngine
    ↑ implemented by
Data layer        ← Room DAOs, repository impls, TseClientImpl, BonDrucker, Workers
```

- ViewModels hold `StateFlow<UiState>` — no `LiveData`, no mutable state in Composables.
- Use cases are `operator fun invoke(...)` classes injected by Hilt.
- All monetary arithmetic is `Long` (cents). Rounding: `HALF_UP` at display time only.
- `Transaktion.locked = true` after Z-Bon — enforced at repository level (throws if write attempted).

## Data Model (12 Entities + 1 Archive)

| Entity | Key Fields | Relations |
|--------|-----------|-----------|
| `Artikel` | id, name, kategorie, mwstSatz | → many SKU |
| `SKU` | id, artikelId, variantenName, preisInCent, bestand | → Artikel |
| `Preisregel` | id, startTime, endTime, weekdays, discountType, filter | — |
| `Tisch` | id, bezeichnung, status, version | → Transaktion |
| `Bediener` | id, name, pinHash, rolle, isTraining | — |
| `Schicht` | id, bedienerid, startZeit, endZeit | → Bediener |
| `Transaktion` | id, tischId, bedienerid, schichtId, status, locked, isTraining | → Positionen, Bon |
| `TransaktionsPosition` | id, transaktionId, skuId, menge, preisInCent, mwstSatz | → Transaktion, SKU |
| `Bon` | id, transaktionId, zeitstempel, tseSignatur, tseSerial, tseZeitpunkt, tseTxNummer, isNachdruck | → Transaktion |
| `ZBon` | id, nummer, vonZeit, bisZeit, gesamtInCent, mwst7, mwst19, anzahlBuchungen, anzahlStornos, bedienerid, tseSignatur | — |
| `TseProtokollEintrag` | id, transaktionId, typ, tseSerial, signatur, zeitstempel, txNummer | → Transaktion |
| `Verfahrensdokumentation` | id, version, inhaltHash, zeitstempel | — |
| `DsFinVKArchivSatz` | id, transaktionId, csvRowJson, exportZeit | → Transaktion (immutable shadow) |

## Key Technical Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| UI toolkit | Jetpack Compose | Best Android performance & hardware access; touch-native |
| Persistence | Room / SQLite on-device | ≥ 24h offline (SC-004); single-tablet; no sync complexity |
| TSE provider | Deutsche Fiskal DFKA Cloud-TSE | Spec assumption; REST API behind `TseClient` interface (swappable) |
| Background jobs | WorkManager | Survives process death; Doze-safe; TSE offline buffer + DSFinV-K export |
| Monetary type | `Long` (Eurocent) | Constitution mandate; no floating-point rounding errors |
| Backup | `allowBackup=false` | GoBD immutability; DSFinV-K export is archival path |
| DI | Hilt | Official Android DI; reduces boilerplate; well-integrated with ViewModel/WorkManager |

## Complexity Justification

| Item | Why Needed | Simpler Alternative Rejected Because |
|------|-----------|--------------------------------------|
| TseOfflineBuffer + WorkManager | KassenSichV requires every Tx signed; network may be down 24h | Synchronous TSE call would block sale and violate SC-004 |
| `DsFinVKArchivSatz` shadow table | DSFinV-K requires immutable export snapshot; original records may be updated (Storno) | Reading live rows at export time risks inconsistency if schema changes |
| PreisregelEngine as pure module | Constitution: business logic separated from I/O; must be unit-tested without DB | Inline ViewModel logic untestable in isolation |
