# Tasks: Bar-Kassensystem

**Input**: Design documents from `/specs/001-bar-kassensystem/`
**Prerequisites**: plan.md ✅, spec.md ✅

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.
**TDD**: Constitution mandates Test-First — unit tests written → verified FAIL → implementation follows.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1 … US7)
- Exact file paths are relative to `android/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Android project initialization, Gradle configuration, CI skeleton.

- [ ] T001 Create Android Gradle project at `android/` with `minSdk 29`, `targetSdk 35`, `compileSdk 35`, namespace `de.barpos.kassensystem`
- [ ] T002 Create Gradle version catalog at `android/gradle/libs.versions.toml` (Compose BOM, Room 2.7, Hilt 2.55, Retrofit 2.11, WorkManager 2.10, Coroutines 1.9, DataStore 1.1, JUnit 5, MockK, Robolectric)
- [ ] T003 [P] Configure `android/app/build.gradle.kts`: enable Compose, Hilt KSP, `allowBackup=false`, landscape-only orientation lock
- [ ] T004 [P] Configure `android/app/proguard-rules.pro`: protect domain package, strip logging in release
- [ ] T005 [P] Add `android/app/src/main/AndroidManifest.xml`: INTERNET permission (TSE + printer), RECEIVE_BOOT_COMPLETED (WorkManager)
- [ ] T006 Create `android/app/src/main/java/de/barpos/kassensystem/KassenApp.kt`: `@HiltAndroidApp Application` subclass

**Checkpoint**: Project compiles; empty app launches on API-29 emulator (10" landscape).

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that ALL user stories depend on — DB schema, domain models, DI, TSE pipeline.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete.

### Tests — Foundation

- [ ] T007 Write unit tests for `TypeConverter` (Instant ↔ Long, JSON ↔ Map) in `app/src/test/.../db/converter/TypeConverterTest.kt` — **MUST FAIL first**
- [ ] T008 Write unit tests for integer-cent arithmetic helpers (`CentMath`) in `app/src/test/.../domain/CentMathTest.kt` — **MUST FAIL first**

### Domain Models

- [ ] T009 [P] Create domain model `Transaktion.kt` (id, tischId, bedienerid, schichtId, status, locked, isTraining, positionen) in `app/src/main/.../domain/model/`
- [ ] T010 [P] Create domain models `Artikel.kt`, `SKU.kt`, `Preisregel.kt` in `app/src/main/.../domain/model/`
- [ ] T011 [P] Create domain models `Tisch.kt`, `Bediener.kt`, `Schicht.kt` in `app/src/main/.../domain/model/`
- [ ] T012 [P] Create domain models `Bon.kt`, `ZBon.kt`, `TseProtokollEintrag.kt` in `app/src/main/.../domain/model/`
- [ ] T013 [P] Create `CentMath.kt` utility (Long arithmetic, HALF_UP rounding display) in `app/src/main/.../domain/`

### Room Database

- [ ] T014 Create `TypeConverter.kt` (Instant↔Long, List/Map↔JSON) in `app/src/main/.../data/db/converter/`
- [ ] T015 [P] Create Room `@Entity` classes for all 12 entities + `DsFinVKArchivSatz` in `app/src/main/.../data/db/entity/` (mirror domain models; use `Long` for all monetary fields)
- [ ] T016 Create `KassenDatabase.kt` `@Database` (all 13 entity classes, WAL mode, `exportSchema=true`) in `app/src/main/.../data/db/`
- [ ] T017 [P] Create `TransaktionDao.kt` + `TransaktionsPositionDao.kt` in `app/src/main/.../data/db/dao/`
- [ ] T018 [P] Create `ArtikelDao.kt`, `SkuDao.kt`, `PreisregelDao.kt` in `app/src/main/.../data/db/dao/`
- [ ] T019 [P] Create `TischDao.kt`, `BedienerDao.kt`, `SchichtDao.kt` in `app/src/main/.../data/db/dao/`
- [ ] T020 [P] Create `BonDao.kt`, `ZBonDao.kt`, `TseProtokollEintragDao.kt` in `app/src/main/.../data/db/dao/`

### Repository Interfaces & Implementations

- [ ] T021 [P] Create repository interfaces `ITransaktionRepository`, `IArtikelRepository`, `ITischRepository` in `app/src/main/.../domain/repository/`
- [ ] T022 [P] Create repository interfaces `IBedienerRepository`, `ISchichtRepository`, `IBonRepository`, `IZBonRepository` in `app/src/main/.../domain/repository/`
- [ ] T023 Implement `TransaktionRepository.kt` (atomic Room `@Transaction` save; enforce `locked` guard) in `app/src/main/.../data/repository/`
- [ ] T024 [P] Implement `ArtikelRepository.kt`, `SkuRepository.kt` in `app/src/main/.../data/repository/`
- [ ] T025 [P] Implement `TischRepository.kt` (optimistic locking on `version` column) in `app/src/main/.../data/repository/`
- [ ] T026 [P] Implement `BedienerRepository.kt` (PIN hash via SHA-256), `SchichtRepository.kt` in `app/src/main/.../data/repository/`

### TSE Integration

- [ ] T027 Create `TseClient.kt` interface (`startTransaction`, `updateTransaction`, `finishTransaction(type)`, `exportTar`) in `app/src/main/.../domain/repository/`
- [ ] T028 Implement `TseClientImpl.kt` Retrofit service targeting Deutsche Fiskal DFKA Cloud-TSE REST V2 in `app/src/main/.../data/hardware/`
- [ ] T029 Implement `TseOfflineBuffer.kt`: Room-backed queue for pending TSE calls; exponential-backoff retry in `app/src/main/.../data/repository/`
- [ ] T030 Create `TseOfflineWorker.kt` WorkManager `CoroutineWorker` that drains `TseOfflineBuffer` in `app/src/main/.../data/worker/`
- [ ] T031 Add `TseSignUseCase.kt`: wrap every `Transaktion` save — sign via `TseClient`; on failure enqueue to `TseOfflineBuffer`; block if buffer full per KassenSichV §146a in `app/src/main/.../domain/usecase/`

### Hilt DI

- [ ] T032 [P] Create `DatabaseModule.kt` Hilt module (provides `KassenDatabase`, all DAOs) in `app/src/main/.../di/`
- [ ] T033 [P] Create `RepositoryModule.kt` Hilt module (binds interfaces → implementations) in `app/src/main/.../di/`
- [ ] T034 [P] Create `NetworkModule.kt` Hilt module (provides Retrofit, `TseClientImpl`, `BonDrucker`) in `app/src/main/.../di/`
- [ ] T035 [P] Create `WorkerModule.kt` Hilt `@HiltWorker` bindings for `TseOfflineWorker`, `DsFinVKExportWorker` in `app/src/main/.../di/`

### UI Theme & Navigation

- [ ] T036 Create MaterialTheme, colours, typography in `app/src/main/.../ui/theme/`
- [ ] T037 Create `NavGraph.kt` (NavHost with routes for all screens) in `app/src/main/.../ui/nav/`
- [ ] T038 Create `MainActivity.kt` (`@AndroidEntryPoint`, sets Compose content, landscape lock, immersive mode) in `app/src/main/.../`

**Checkpoint**: Foundation ready — Room DB migrates cleanly; Hilt graph compiles; TSE offline buffer enqueues and drains in tests.

---

## Phase 3: User Story 1 — Bestellung kassieren (Priority: P1) 🎯 MVP

**Goal**: Cashier can open, build, pay and finalise a transaction with full TSE signing and receipt print.

**Independent Test**: Complete a cash transaction for 2 articles → bon printed → TSE entry persisted → SC-001 < 30 s.

### Tests — US-1 (Write first, verify FAIL)

- [ ] T039 [P] [US1] Unit test `TransaktionViewModelTest`: add/remove position, VAT split 7%/19%, total calculation in `app/src/test/.../ui/kasse/`
- [ ] T040 [P] [US1] Integration test `TransaktionRepositoryTest` (in-memory Room): full save → lock → storno cycle in `app/src/androidTest/.../data/repository/`

### Implementation — US-1

- [ ] T041 [US1] Create `SaveTransaktionUseCase.kt` (assembles `Transaktion`, calls `TransaktionRepository.save`, then `TseSignUseCase`) in `app/src/main/.../domain/usecase/`
- [ ] T042 [US1] Create `StornoUseCase.kt` (TSE `finishTransaction(type=CANCEL)`, create linked `StornoTransaktion`, guard if `Bon.isNachdruck`) in `app/src/main/.../domain/usecase/`
- [ ] T043 [US1] Create `TransaktionViewModel.kt` (StateFlow UiState: positions, totals, VAT, payment mode) in `app/src/main/.../ui/kasse/`
- [ ] T044 [US1] Build `KassenScreen.kt` Compose screen: split layout — left product grid (category tabs, search bar), right order list; touch targets ≥ 48dp in `app/src/main/.../ui/kasse/`
- [ ] T045 [US1] Build `ProductGridItem.kt` and `OrderPositionItem.kt` composables with swipe-to-delete in `app/src/main/.../ui/kasse/`
- [ ] T046 [US1] Build `PaymentDialog.kt` Compose dialog: cash (change calculation) and EC-extern modes; GwG guard for > 10 000 € in `app/src/main/.../ui/kasse/`
- [ ] T047 [US1] Implement `BonDrucker.kt` ESC/POS adapter (TCP socket, printer IP from DataStore, all Pflichtfelder incl. TSE-Signatur, Kassennummer) in `app/src/main/.../data/hardware/`
- [ ] T048 [US1] Create `PrintBonUseCase.kt` (fetch `Bon` from repo, format ESC/POS, call `BonDrucker`) in `app/src/main/.../domain/usecase/`

**Checkpoint**: US-1 fully functional; standard transaction completes in < 30 s on device.

---

## Phase 4: User Story 2 — Tischverwaltung & offene Rechnung (Priority: P2)

**Goal**: Staff can open tables, accumulate orders, split bills, and merge/close tables.

**Independent Test**: Open table → add multiple rounds → partial payment → close table; open table survives app restart.

### Tests — US-2 (Write first, verify FAIL)

- [ ] T049 [P] [US2] Unit test `TischViewModelTest`: open, split, merge, optimistic-lock conflict in `app/src/test/.../ui/tisch/`
- [ ] T050 [P] [US2] Integration test `TischRepositoryTest` (in-memory Room): version conflict throws `OptimisticLockException` in `app/src/androidTest/.../data/repository/`

### Implementation — US-2

- [ ] T051 [US2] Create `OpenTischUseCase.kt`, `CloseTischUseCase.kt`, `SplitTischUseCase.kt` in `app/src/main/.../domain/usecase/`
- [ ] T052 [US2] Create `TischViewModel.kt` (StateFlow: floor plan grid, table statuses, current open transactions) in `app/src/main/.../ui/tisch/`
- [ ] T053 [US2] Build `TischScreen.kt` Compose screen: configurable grid floor plan, colour-coded status badges (`FREI` / `OFFEN` / `RESERVIERT`) in `app/src/main/.../ui/tisch/`
- [ ] T054 [US2] Build `RechnungsvorschauSheet.kt` bottom sheet: itemised bill per table, partial-payment input, seat-based split in `app/src/main/.../ui/tisch/`
- [ ] T055 [US2] Add `direktkassierung` mode flag to `KassenScreen` (bypasses table selection; concurrent with open tables — edge case EC-08 via optimistic lock) in `app/src/main/.../ui/kasse/`

**Checkpoint**: US-2 functional — open tables persist across restarts; split/merge works.

---

## Phase 5: User Story 3 — Tagesabschluss / Z-Bon (Priority: P3)

**Goal**: Manager can perform end-of-day Z-Bon, which freezes all transactions and produces a legally compliant report.

**Independent Test**: Z-Bon aggregates all transactions since last Z-Bon; all transactions locked afterward; overnight open-table edge case flagged (EC-04).

### Tests — US-3 (Write first, verify FAIL)

- [ ] T056 [P] [US3] Unit test `ZBonAggregationTest`: correct totals, MwSt split, Storno exclusion, Training exclusion in `app/src/test/.../domain/usecase/`
- [ ] T057 [P] [US3] Integration test `TagesabschlussTest` (in-memory Room): locked flag set on all Transaktionen after Z-Bon; write to locked throws in `app/src/androidTest/.../data/repository/`

### Implementation — US-3

- [ ] T058 [US3] Create `GenerateZBonUseCase.kt`: aggregate all unlocked `Transaktion` since last `ZBon`; compute Gesamtumsatz, MwSt 7%/19%, Anzahl Buchungen/Stornos; persist `ZBon`; set `locked=true` on all included transactions in `app/src/main/.../domain/usecase/`
- [ ] T059 [US3] Create `PrintZBonUseCase.kt` (format ESC/POS Z-Bon with all §146a Pflichtfelder, TSE-Seriennummer, Signatur) in `app/src/main/.../domain/usecase/`
- [ ] T060 [US3] Create `TagesabschlussViewModel.kt` (StateFlow: open-tables warning, training-tx warning, Z-Bon preview, confirm flow) in `app/src/main/.../ui/abschluss/`
- [ ] T061 [US3] Build `TagesabschlussScreen.kt` Compose screen: summary preview, admin-PIN override for open tables, confirm button in `app/src/main/.../ui/abschluss/`

**Checkpoint**: US-3 functional — Z-Bon persisted; all FRs for Tagesabschluss satisfied; transactions immutably locked.

---

## Phase 6: User Story 4 — Happy Hour / Zeitbasierte Preisregeln (Priority: P4)

**Goal**: Admin can define time/weekday-based price rules; rules apply automatically to transactions.

**Independent Test**: Create a 50% Happy Hour rule for Mon–Fri 17:00–19:00; trigger sale during window → correct discounted price; no discount outside window.

### Tests — US-4 (Write first, verify FAIL)

- [ ] T062 [P] [US4] Unit test `PreisregelEngineTest`: PERCENT/FIXED/OVERRIDE rules, priority stacking, weekday bitmask, boundary times, no active rule in `app/src/test/.../domain/engine/`
- [ ] T063 [P] [US4] Unit test `PreisregelRepositoryTest` (in-memory Room): CRUD, active-rules-at-time query in `app/src/androidTest/.../data/repository/`

### Implementation — US-4

- [ ] T064 [US4] Create `PreisregelEngine.kt` pure Kotlin object: calculates final price for a SKU given `Instant` and ordered `List<Preisregel>` in `app/src/main/.../domain/engine/`
- [ ] T065 [US4] Implement `PreisregelRepository.kt` + `IPreisregelRepository` interface in `app/src/main/.../data/repository/` and `domain/repository/`
- [ ] T066 [US4] Wire `PreisregelEngine` into `TransaktionViewModel`: evaluate on every position add; show active-rule banner in `KassenScreen` in `app/src/main/.../ui/kasse/`
- [ ] T067 [US4] Build `PreisregelAdminScreen.kt` Compose screen (PIN-protected): list, create, edit, delete rules; time-range picker in `app/src/main/.../ui/preisregel/`
- [ ] T068 [US4] Create `PreisregelViewModel.kt` for admin CRUD in `app/src/main/.../ui/preisregel/`

**Checkpoint**: US-4 functional — PreisregelEngine unit-test passes; Happy Hour discount auto-applies in KassenScreen.

---

## Phase 7: User Story 5 — Schichtwechsel & Bedienerverwaltung (Priority: P5)

**Goal**: Each operator logs in with PIN; transactions are attributed per operator; shift-close produces a handover report.

**Independent Test**: Login Bediener A → create 3 transactions → shift-close → handover report printed; Trainee transactions excluded from Z-Bon totals.

### Tests — US-5 (Write first, verify FAIL)

- [ ] T069 [P] [US5] Unit test `BedienerAuthUseCaseTest`: correct PIN hash, wrong PIN rejected, Trainee flag in `app/src/test/.../domain/usecase/`
- [ ] T070 [P] [US5] Integration test `SchichtTest` (in-memory Room): shift-close sets `endZeit`; transactions linked via `bedienerid` in `app/src/androidTest/.../data/repository/`

### Implementation — US-5

- [ ] T071 [US5] Create `BedienerAuthUseCase.kt` (SHA-256 PIN hash, role verification) in `app/src/main/.../domain/usecase/`
- [ ] T072 [US5] Create `OpenSchichtUseCase.kt`, `CloseSchichtUseCase.kt` in `app/src/main/.../domain/usecase/`
- [ ] T073 [US5] Create `SchichtViewModel.kt` (StateFlow: current Bediener, shift start time, shift summary) in `app/src/main/.../ui/schicht/`
- [ ] T074 [US5] Build `SchichtScreen.kt` Compose screen: PIN pad login, active-shift display, shift-close button, handover report trigger in `app/src/main/.../ui/schicht/`
- [ ] T075 [US5] Create `PrintSchichtberichtUseCase.kt` (ESC/POS shift handover: per-operator totals, start/end times) in `app/src/main/.../domain/usecase/`
- [ ] T076 [US5] Thread current `Bediener` through `TransaktionViewModel`; set `IS_TRAINING` flag for Trainee role in `app/src/main/.../ui/kasse/`

**Checkpoint**: US-5 functional — every transaction carries `bedienerid`; Trainee tx excluded from Z-Bon; FR-017/FR-018 satisfied.

---

## Phase 8: User Story 6 — Produktvarianten (Priority: P6)

**Goal**: Articles can have multiple SKU variants (e.g., Bier 0.3l / 0.5l); cashier selects variant on article tap.

**Independent Test**: Tap an article with 2 variants → variant picker appears → selecting one adds correct SKU to order with correct price.

### Tests — US-6 (Write first, verify FAIL)

- [ ] T077 [P] [US6] Unit test `VariantPickerViewModelTest`: single-variant auto-select, multi-variant shows bottom sheet in `app/src/test/.../ui/varianten/`

### Implementation — US-6

- [ ] T078 [US6] Add `variantenAttributeJson` (JSON column + TypeConverter) to `SKU` Room entity; add `SkuDao.findByArtikelId()` in `app/src/main/.../data/db/`
- [ ] T079 [US6] Create `VariantPickerViewModel.kt` (loads SKUs for given Artikel, resolves auto-select vs. picker) in `app/src/main/.../ui/varianten/`
- [ ] T080 [US6] Build `VariantPickerSheet.kt` Compose bottom sheet: grid of variant chips with name + price in `app/src/main/.../ui/varianten/`
- [ ] T081 [US6] Wire `VariantPickerSheet` into `KassenScreen` product tap flow in `app/src/main/.../ui/kasse/`

**Checkpoint**: US-6 functional — variant picker appears for multi-SKU articles; single-SKU articles add directly.

---

## Phase 9: User Story 7 — Einfache Lagerverwaltung (Priority: P7)

**Goal**: System tracks stock per SKU; warns when stock is critically low; manager can adjust counts and export CSV.

**Independent Test**: Set SKU stock to 2 → sell 2 → low-stock warning shown → sell 1 more (goes negative, warning persisted) → LagerScreen shows correct counts.

### Tests — US-7 (Write first, verify FAIL)

- [ ] T082 [P] [US7] Unit test `StockDecrementUseCaseTest`: decrement, negative stock allowed, low-stock threshold triggered in `app/src/test/.../domain/usecase/`

### Implementation — US-7

- [ ] T083 [US7] Create `DecrementStockUseCase.kt` (decrement `SKU.bestand` in Room transaction alongside `SaveTransaktionUseCase`; emit low-stock event if `bestand < threshold`) in `app/src/main/.../domain/usecase/`
- [ ] T084 [US7] Create `LagerViewModel.kt` (StateFlow: SKU list with current stock, adjustment inputs, export trigger) in `app/src/main/.../ui/lager/`
- [ ] T085 [US7] Build `LagerScreen.kt` Compose screen: list of SKUs with inline ± adjustments, low-stock highlights in `app/src/main/.../ui/lager/`
- [ ] T086 [US7] Implement CSV export in `LagerViewModel` (write `lager_<date>.csv` to `Documents/` via `MediaStore`) in `app/src/main/.../ui/lager/`
- [ ] T087 [US7] Show low-stock `Snackbar` in `KassenScreen` when `DecrementStockUseCase` emits threshold event in `app/src/main/.../ui/kasse/`

**Checkpoint**: US-7 functional — stock decrements on sale; low-stock warning visible; CSV exports correctly.

---

## Phase 10: DSFinV-K Export & Archiv

**Goal**: Generate legally required DSFinV-K 2.3 CSV export zip including TSE TAR archive.

### Tests — DSFinV-K

- [ ] T088 [P] Unit test `DsFinVKMapperTest`: CSV row output matches DSFinV-K 2.3 schema (TRANSACTIONS, LINES, PAYMENT, CASHPOINTCLOSING) in `app/src/test/.../domain/usecase/`

### Implementation — DSFinV-K

- [ ] T089 Create `DsFinVKExportUseCase.kt`: map locked Transaktionen → DSFinV-K CSV rows → `DsFinVKArchivSatz` shadow records in `app/src/main/.../domain/usecase/`
- [ ] T090 Create `DsFinVKExportWorker.kt` WorkManager one-shot: run `DsFinVKExportUseCase`, then call `TseClient.exportTar()`, zip all, write to `Documents/DSFinV-K/<date>/` in `app/src/main/.../data/worker/`
- [ ] T091 Build `ExportScreen.kt` Compose screen: manual trigger, progress indicator, last-export info, error log in `app/src/main/.../ui/export/`
- [ ] T092 Auto-trigger `DsFinVKExportWorker` after successful Z-Bon in `GenerateZBonUseCase` in `app/src/main/.../domain/usecase/`

**Checkpoint**: Export zip produced; `dfka-validator.jar` passes with 0 errors for test dataset.

---

## Phase 11: Compliance Screens

**Goal**: In-app Verfahrensdokumentation viewer and Kassennachschau read-only mode (SC-007 < 5 min).

- [ ] T093 Bundle `verfahrensdokumentation.pdf` in `app/src/main/assets/`; create `VerfahrensdokumentationScreen.kt` PDF viewer composable in `app/src/main/.../ui/settings/`
- [ ] T094 Create `KassennachschauScreen.kt` read-only deep-link mode (special admin PIN): surfaces all audit data — Transaktionen, Bons, Z-Bons, TSE entries, DSFinV-K export list in `app/src/main/.../ui/settings/`
- [ ] T095 Add `KassennachschauViewModel.kt` with combined audit query (all data in single StateFlow) in `app/src/main/.../ui/settings/`
- [ ] T096 Add GwG guard in `PaymentDialog.kt`: hard-block Barzahlung > 1 000 000 ct (10 000 €) with non-dismissible dialog referencing GwG §10 (edge case EC-05) in `app/src/main/.../ui/kasse/`

**Checkpoint**: Kassennachschau mode usable end-to-end in < 5 min (SC-007).

---

## Phase 12: Polish & Cross-Cutting Concerns

**Purpose**: Settings, error handling, hardening, full test sweep.

- [ ] T097 [P] Build `SettingsScreen.kt`: printer IP, TSE credentials (stored in EncryptedSharedPreferences), VAT rates, low-stock threshold, backup export path in `app/src/main/.../ui/settings/`
- [ ] T098 [P] Implement global `ErrorHandler.kt` (non-dismissible dialogs for TSE failures, print failures, DB constraint violations; structured error log entries) in `app/src/main/.../ui/`
- [ ] T099 [P] Enforce ≥ 48dp touch targets in all composables; verify with Accessibility Scanner lint rule in `app/src/main/.../ui/`
- [ ] T100 [P] Add reprint flow: `ReprintBonUseCase.kt` fetches `Bon`, marks `isNachdruck=true`, re-sends to `BonDrucker` in `app/src/main/.../domain/usecase/`
- [ ] T101 [P] Add `WorkManagerInitializer` to schedule `TseOfflineWorker` as periodic 15-min check on app start in `app/src/main/.../data/worker/`
- [ ] T102 Run full unit test suite `./gradlew test`; fix all failures in `app/src/test/`
- [ ] T103 Run Room integration tests on API-29 emulator `./gradlew connectedAndroidTest`; fix failures in `app/src/androidTest/`
- [ ] T104 [P] Add Compose UI test `KassenHappyPathTest`: US-1 full transaction < 30 s (SC-001) in `app/src/androidTest/.../ui/`
- [ ] T105 [P] Add Compose UI test `ZBonFlowTest`: US-3 end-to-end Z-Bon and lock verification in `app/src/androidTest/.../ui/`
- [ ] T106 [P] Add Compose UI test `KassennachschauTest`: audit mode operational in < 5 min (SC-007) in `app/src/androidTest/.../ui/`

**Checkpoint**: All tests green; `dfka-validator.jar` passes; manual smoke test with Deutsche Fiskal sandbox TSE complete.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: No dependencies — start immediately
- **Phase 2 (Foundational)**: Depends on Phase 1 — **BLOCKS all user stories**
- **Phases 3–9 (User Stories)**: All depend on Phase 2; can proceed in priority order (P1 → P7)
- **Phase 10 (DSFinV-K)**: Depends on Phase 5 (Z-Bon locking) + Phase 2 (DB)
- **Phase 11 (Compliance)**: Depends on Phase 3 (Bon), Phase 5 (Z-Bon), Phase 10 (Export)
- **Phase 12 (Polish)**: Depends on all prior phases

### Within Each User Story

1. Tests written → verified to FAIL
2. Domain models / use cases
3. Repository / data layer
4. ViewModel
5. Compose screen
6. Wire into NavGraph

### Parallel Opportunities Per Story

All `[P]`-marked tasks within a phase can be launched simultaneously (they touch different files).

---

## Implementation Strategy

### MVP (User Story 1 Only)

1. Phase 1: Setup
2. Phase 2: Foundational ← **blocks all stories**
3. Phase 3: US-1 (Kassieren)
4. **STOP**: validate transaction < 30 s, TSE signed, bon printed

### Incremental Delivery

- P1 → working cashier
- P2 → table management
- P3 → end-of-day compliance
- P4–P7 → bar-specific features
- Each story independently testable and demoable

---

## Notes

- All monetary values: `Long` Eurocent — never `Float` or `Double`
- `Transaktion.locked = true` after Z-Bon — repository enforces this invariant
- `allowBackup=false` in `AndroidManifest.xml` — constitutional requirement
- TSE interface in `domain/` — swap DFKA provider without touching domain logic
- Trainee transactions (`isTraining=true`) excluded from Z-Bon aggregation (EC-07)
- Commit after each completed checkpoint
