# Feature Specification: Bar-Kassensystem

**Feature Branch**: `001-bar-kassensystem`
**Created**: 2026-02-25
**Status**: Draft
**Input**: User description: "Erstelle ein modernes Kassensystem für eine Bar, bedienbar per Android-Tablet, Touch-optimiert, basierend auf deutschem Fiskalrecht (KassenSichV, GoBD, AO)."

## User Scenarios & Testing *(mandatory)*

### User Story 1 – Bestellung kassieren (Priority: P1)

Ein Barkeeper steht am Tresen. Ein Gast bestellt zwei Getränke. Der Barkeeper tippt die Artikel auf dem Tablet an, wählt „Bar zahlen", und der Kassiervorgang wird abgeschlossen. Das System signiert den Vorgang via Cloud-TSE und druckt sofort einen gesetzeskonformen Bon.

**Why this priority**: Das ist der Core-Use-Case. Ohne funktionierenden Kassiervorgang ist kein anderes Feature nutzbar.

**Independent Test**: Kann vollständig mit einem Mock-TSE-Adapter und einem simulierten Drucker getestet werden. Liefert einen signierten Transaktionsdatensatz und einen validen Bon-Inhalt.

**Acceptance Scenarios**:

1. **Given** der Barkeeper ist angemeldet und das System ist betriebsbereit, **When** er zwei Artikel antippt und „Bar zahlen" bestätigt, **Then** wird eine Transaktion mit fortlaufender Nummer, Datum/Uhrzeit (UTC), Bedienerkennung, Artikeln, Einzelpreisen, Gesamtbetrag, USt-Satz 19 %, Zahlungsart und TSE-Signatur gespeichert.
2. **Given** eine Transaktion wurde abgeschlossen, **When** der Bon ausgegeben wird, **Then** enthält er alle Pflichtangaben gemäß § 146a AO: Name/Anschrift des Betreibers, Belegdatum, Transaktionsnummer, Artikel, Bruttoentgelt, USt-Satz, Kassennummer, TSE-Seriennummer, Prüfwert.
3. **Given** der Gast zahlt per EC-Karte (externes Terminal), **When** der Barkeeper „EC zahlen" wählt, **Then** wird die Zahlungsart im Datensatz als „unbar" markiert und erscheint nicht im Kassenbuch-Bargeldsaldo.
4. **Given** eine Transaktion wurde gebucht, **When** ein Storno notwendig ist, **Then** wird keine Änderung an der Originalbuchung vorgenommen, sondern eine neue Gegenbuchung mit Verweis auf die Original-Transaktionsnummer erstellt.

---

### User Story 2 – Tischverwaltung & offene Rechnung (Priority: P2)

Ein Gast setzt sich an einen Tisch. Der Barkeeper öffnet Tisch 3, bucht mehrere Runden über den Abend. Am Ende bezahlt der Gast die gesammelte Rechnung.

**Why this priority**: Für einen Bar-Betrieb essenziell; ohne Tischverwaltung müsste jede Runde sofort kassiert werden, was unpraktisch ist.

**Independent Test**: Testbar durch Öffnen von zwei Tischen, Hinzubuchen von Positionen an jeweils einem Tisch und separates Abrechnen.

**Acceptance Scenarios**:

1. **Given** kein Tisch ist geöffnet, **When** der Barkeeper „Tisch 3 öffnen" antippt, **Then** wird ein neuer offener Tisch-Bon mit Zeitstempel und Bedienerkennung angelegt.
2. **Given** Tisch 3 ist geöffnet, **When** der Barkeeper Artikel hinzubucht, **Then** werden die Positionen dem Tisch-Bon hinzugefügt und der aktuelle Zwischenbetrag angezeigt.
3. **Given** Tisch 3 hat offene Positionen, **When** der Barkeeper „Tisch abrechnen" wählt und die Zahlung bestätigt, **Then** wird eine abschließende Transaktion erzeugt, der TSE-Bon gedruckt und der Tisch als „geschlossen" markiert.
4. **Given** ein Tisch ist geöffnet und Mitternacht kommt, **When** der Kalendertag wechselt, **Then** bleiben die offenen Positionen erhalten; die Transaktion wird dem Buchungstag ihres Abschlusses zugeordnet, und der Z-Bon des Vortages enthält ausschließlich abgeschlossene Transaktionen.

---

### User Story 3 – Tagesabschluss / Z-Bon (Priority: P3)

Am Ende der Schicht erstellt der Schichtleiter den Z-Bon, kontrolliert den Kassenstand und schließt den Tag ab.

**Why this priority**: Gesetzlich vorgeschrieben (§ 146 AO); ohne Z-Bon keine ordnungsmäßige Kassenführung.

**Independent Test**: Testbar durch Anlegen mehrerer Testtransaktionen (inkl. Storno, Bar und EC) und anschließendes Auslösen des Tagesabschlusses — der erzeugte Z-Bon wird gegen die Pflichtfelder-Liste aus FR-010 geprüft.

**Acceptance Scenarios**:

1. **Given** mehrere Transaktionen wurden am Tag gebucht, **When** der Schichtleiter „Tagesabschluss" auslöst, **Then** erzeugt das System einen Z-Bon mit: fortlaufend automatisch generierter Nummer, Name des Geschäfts, Datum/Uhrzeit, Bruttoeinnahmen je USt-Satz (7 %/19 %), Storno-/Retourbuchungen, Entnahmen, Zahlungsarten (Bar/EC getrennt), Nullstellungszähler.
2. **Given** der Z-Bon wurde erzeugt, **When** er gedruckt wird, **Then** bleibt er unveränderbar archiviert und ist mit allen Einzeltransaktionen des Tages verknüpft.
3. **Given** Trainee-Bediener haben Vorgänge gebucht, **When** der Z-Bon erzeugt wird, **Then** werden die Trainee-Umsätze separat ausgewiesen.

---

### User Story 4 – Happy Hour / Zeitbasierte Preisregeln (Priority: P4)

Täglich von 17:00–19:00 Uhr sind alle Bierpreise 20 % günstiger. Das System soll dies automatisch anwenden, ohne dass der Barkeeper etwas tun muss.

**Why this priority**: Häufiger Bar-Use-Case; erhöht Effizienz und verhindert manuelle Preisfehler.

**Independent Test**: Testbar durch Anlegen einer Preisregel für ein Zeitfenster und Simulieren von Transaktionen innerhalb und außerhalb des Zeitfensters — ohne reale Uhrzeit (feste Testzeitstempel).

**Acceptance Scenarios**:

1. **Given** eine Preisregel gilt für 17:00–19:00 Uhr, **When** ein Artikel um 17:30 Uhr gescannt wird, **Then** wird der reduzierte Preis automatisch angewendet und der Rabattgrund auf dem Bon ausgewiesen.
2. **Given** eine Preisregel ist aktiv, **When** eine Transaktion um 19:05 Uhr gestartet wird, **Then** gilt der reguläre Preis ohne Rabatt.
3. **Given** eine Happy-Hour-Transaktion wurde abgeschlossen, **When** ein Storno erfolgt, **Then** wird zum Originalpreis (reduzierter Preis) storniert.

---

### User Story 5 – Schichtwechsel & Bedienerverwaltung (Priority: P5)

Schicht A endet, Schicht B beginnt. Der Kassenstand wird übergeben und dokumentiert.

**Why this priority**: Gesetzlich erforderlich (Bedienerkennung je Vorgang, § 146 AO); verhindert Fehlzuordnungen bei Betriebsprüfungen.

**Independent Test**: Testbar durch Anlegen von zwei Bedienern, Buchen je einer Transaktion pro Bediener und Prüfen, dass beide Transaktionen die korrekte Bedienerkennung tragen.

**Acceptance Scenarios**:

1. **Given** Bediener A ist aktiv, **When** Bediener B sich anmeldet, **Then** werden alle folgenden Transaktionen mit der Kennung von Bediener B versehen.
2. **Given** ein Schichtwechsel wird ausgelöst, **When** der Kassensturz erfasst wird, **Then** werden Soll- und Ist-Bestand dokumentiert und unveränderbar gespeichert.
3. **Given** ein Bediener ist als „Trainee" markiert, **When** er Transaktionen bucht, **Then** sind diese Transaktionen auf dem Z-Bon gesondert ausgewiesen.

---

### User Story 6 – Produktvarianten (Priority: P6)

Bier wird in 0,3 l und 0,5 l angeboten, zu unterschiedlichen Preisen. Der Barkeeper soll beim Antippen direkt wählen können.

**Why this priority**: Verhindert Preisfehler; unterschiedliche Größen müssen als eigene SKUs mit eigenem Preis und vollständiger Bezeichnung auf dem Bon erscheinen.

**Independent Test**: Testbar durch Anlegen eines Artikels mit zwei Varianten und Durchführen jeweils einer Transaktion pro Variante.

**Acceptance Scenarios**:

1. **Given** „Weizen" hat Varianten 0,3 l (3,50 €) und 0,5 l (4,80 €), **When** der Barkeeper „Weizen 0,5 l" antippt, **Then** wird die korrekte SKU mit Preis 4,80 € gebucht.
2. **Given** eine Variante wird gebucht, **When** der Bon gedruckt wird, **Then** erscheint die genaue Artikelbezeichnung inkl. Variante (z. B. „Weizen 0,5 l") auf dem Bon.

---

### User Story 7 – Einfache Lagerverwaltung (Priority: P7)

Nach jedem Verkauf wird der Lagerbestand dekrementiert. Bei Unterschreitung eines Meldebestands wird eine Warnung angezeigt.

**Why this priority**: Unterstützt den Betriebsablauf; kein gesetzlich zwingend gefordertes Feature, aber für einen professionellen Bar-Betrieb sinnvoll.

**Independent Test**: Testbar durch Setzen eines Lagerbestands auf 3 Einheiten, Durchführen von 3 Verkäufen und Prüfen, ob die Warnung ausgelöst wird.

**Acceptance Scenarios**:

1. **Given** ein Artikel hat Lagerbestand 10, **When** eine Transaktion mit Menge 2 abgeschlossen wird, **Then** beträgt der Lagerbestand danach 8.
2. **Given** ein Meldebestand von 5 ist konfiguriert und der Bestand fällt auf 4, **When** der Barkeeper die Artikelliste öffnet, **Then** wird der Artikel mit einer Warnmarkierung angezeigt.

---

### Edge Cases

- **TSE-Cloud nicht erreichbar**: Das System puffert Transaktionen lokal und signiert sie nach Wiederherstellung der Verbindung nach. Laufender Betrieb bleibt möglich; Pufferzeitraum darf 24 Stunden nicht überschreiten (BSI-Anforderung TR-03153).
- **Storno nach Bon-Druck**: Keine Änderung der Originalbuchung erlaubt. Das System erzeugt ausschließlich eine neue Gegenbuchung mit Verweis auf die originale Transaktionsnummer.
- **Buchungsabbruch**: Angefangene Vorgänge können nur vollständig abgebrochen (verworfen) oder vollständig abgeschlossen werden. Ein teilgebuchter Vorgang ohne Abschluss ist nicht zulässig. (§ 146 Abs. 1 AO)
- **Tageswechsel bei offenem Tisch**: Offene Tische bleiben über Mitternacht bestehen. Transaktionen werden dem Buchungstag ihres Abschlusses zugeordnet.
- **Barzahlung über 10.000 €**: Das System fordert den Barkeeper zur Erfassung der Kundenidentität auf (§ 10 Abs. 6 GwG) und blockiert den Abschluss bis zur Bestätigung.
- **Systemausfall (Stromausfall)**: Beim Neustart wird die Ausfallzeit mit Zeitstempel festgehalten und dauerhaft protokolliert. Hinweis an Bediener zur manuellen Papiererfassung während des Ausfalls.
- **Trainee-Buchungen**: Transaktionen von Trainee-Bedienern werden regulär gespeichert, müssen aber auf dem Z-Bon gesondert ausgewiesen werden.
- **Gleichzeitig offene Tische und Direktkassierung**: Beide Modi müssen nebeneinander ohne gegenseitige Beeinflussung funktionieren.

---

## Requirements *(mandatory)*

### Functional Requirements

#### Einzelaufzeichnung & Buchführung

- **FR-001**: Das System MUSS jeden einzelnen Verkaufsvorgang mit folgenden Pflichtangaben aufzeichnen: Datum und Uhrzeit, Bedienerkennung, genaue Artikelbezeichnung, Einzelpreis, Menge, Gesamtpreis, USt-Satz, USt-Betrag, Zahlungsart. (§ 146 Abs. 1 AO, GoBD)
- **FR-002**: Alle Geldbeträge MÜSSEN intern als ganzzahlige Cent-Beträge gespeichert und berechnet werden — keine Gleitkomma-Arithmetik.
- **FR-003**: Gebuchte Vorgänge DÜRFEN NICHT im Nachhinein verändert oder gelöscht werden. Korrekturen erfolgen ausschließlich als neue Gegenbuchung (Storno) mit Verweis auf den Originalvorgang. (GoBD Tz. 3, § 146 Abs. 4 AO)
- **FR-004**: Buchungsabbrüche DÜRFEN NICHT möglich sein. Jeder gestartete Vorgang muss entweder vollständig abgeschlossen oder vollständig verworfen werden. (§ 146 Abs. 1 AO)

#### Technische Sicherheitseinrichtung (tSE)

- **FR-005**: Das System MUSS jeden Kassenzugriff lückenlos, fortlaufend nummeriert und manipulationssicher über eine zertifizierte Cloud-TSE (Deutsche Fiskal) signieren. (§ 146a AO, KassenSichV)
- **FR-006**: Der TSE-Protokolleintrag MUSS folgende Angaben enthalten: Zeitpunkt von Vorgangsbeginn und -ende, laufende Transaktionsnummer, Art des Vorgangs, Daten des Vorgangs, Zahlungsart, Prüfwert, Seriennummer der TSE. (KassenSichV § 2)
- **FR-007**: Bei temporärer Nicht-Erreichbarkeit der Cloud-TSE MUSS das System Transaktionen lokal puffern und nach Wiederherstellung der Verbindung nachsignieren. Die Ausfallzeit MUSS protokolliert werden.

#### Belegausgabepflicht

- **FR-008**: Das System MUSS unmittelbar nach Abschluss jedes Kassiervorgangs einen Beleg ausgeben (Papier-Bon über angebundenen Drucker). (§ 146a Abs. 2 AO)
- **FR-009**: Jeder Bon MUSS folgende Pflichtangaben enthalten: Name und Anschrift des Unternehmers, Belegdatum und Transaktionszeitraum, Anzahl und Art der gelieferten Waren/Dienstleistungen, Transaktionsnummer, Bruttoentgelt, USt-Satz, Seriennummer der TSE, Prüfwert. (§ 146a Abs. 2 AO)

#### Tagesabschluss / Z-Bon

- **FR-010**: Das System MUSS täglich einen Z-Bon erzeugen können mit: fortlaufend automatisch generierter Nummer, Name des Geschäftsinhabers, Datum/Uhrzeit des Ausdrucks, Bruttoeinnahmen je USt-Satz (7 %/19 %), Storno-/Retourbuchungen, Entnahmen, Zahlungsarten (Bar/EC getrennt), Nullstellungszähler. (Kassenrichtlinie BMF 26.11.2010)
- **FR-011**: Trainee-Bediener-Umsätze MÜSSEN auf dem Z-Bon gesondert ausgewiesen werden. (§ 146 Abs. 1 AO)

#### Aufbewahrung & Datenzugriff

- **FR-012**: Alle steuerrelevanten Daten MÜSSEN unveränderbar und maschinell auswertbar für mindestens 10 Jahre aufbewahrt werden. (§ 147 AO)
- **FR-013**: Das System MUSS einen strukturierten Datenexport im DSFinV-K-Format bereitstellen. (§ 146b AO, BSI TR-03153)
- **FR-014**: Das System MUSS der Finanzverwaltung auf Anfrage unmittelbaren oder mediengestützten Datenzugriff ermöglichen (Kassennachschau § 146b AO). Exportbereitstellung in unter 5 Minuten.

#### Meldepflicht

- **FR-015**: Das System MUSS alle für die gesetzliche Kassenmeldung notwendigen Daten bereitstellen: Kassensystem-Art, Seriennummer, Anschaffungs-/Inbetriebnahmedatum, TSE-Art und Zertifizierungsnummer, Betriebsstätten-Zuordnung. (§ 146a Abs. 4 AO)

#### Verfahrensdokumentation

- **FR-016**: Das System MUSS eine jederzeit abrufbare Verfahrensdokumentation führen mit: Kassenfabrikat und Seriennummer, Einsatzort und -zeitraum, Ausfallzeiten, Programmierungsänderungen mit Datum, Verweis auf Bedienungsanleitungen. (GoBD Tz. 10.1)

#### Bedienerverwaltung

- **FR-017**: Jeder Vorgang MUSS mit einer eindeutigen Bedienerkennung versehen sein. Bediener MÜSSEN sich vor Kassierung identifizieren. (§ 146 Abs. 1 AO)
- **FR-018**: Bediener MÜSSEN als „regulär" oder „Trainee" klassifizierbar sein.

#### Bar-spezifische Anforderungen

- **FR-019**: Das System MUSS eine Tischverwaltung unterstützen: Tische öffnen, Positionen hinzubuchen, Tisch abrechnen. Mehrere Tische MÜSSEN gleichzeitig offen sein können.
- **FR-020**: Das System MUSS zeitbasierte Preisregeln (Happy Hour) unterstützen, die automatisch anhand der aktuellen Uhrzeit angewendet werden. Der Rabattgrund MUSS auf dem Bon ausgewiesen werden.
- **FR-021**: Artikel MÜSSEN Varianten haben können (z. B. Größen: 0,3 l / 0,5 l), die als eigene Einträge mit eigenem Preis und vollständiger Bezeichnung auf dem Bon erscheinen.
- **FR-022**: Das System MUSS einen einfachen Lagerbestand pro SKU führen und bei Unterschreitung eines konfigurierbaren Meldebestands eine Warnung anzeigen.
- **FR-023**: Bei Barzahlungen über 10.000 € MUSS das System die Erfassung der Kundenidentität gemäß § 10 Abs. 6 GwG einfordern und den Abschluss blockieren bis zur Bestätigung.

#### Systemanforderungen

- **FR-024**: Das System MUSS offline-fähig sein. Alle Kernfunktionen (Kassieren, Tischverwaltung, Z-Bon) MÜSSEN ohne aktive Internetverbindung funktionieren.
- **FR-025**: Die Benutzeroberfläche MUSS Touch-optimiert sein und auf einem Android-Tablet (mind. Android 10, 10"-Display) vollständig bedienbar sein — alle interaktiven Elemente mind. 48 × 48 dp Zielgröße.
- **FR-026**: Das System MUSS einen ESC/POS-kompatiblen Bondrucker über Bluetooth oder WLAN ansprechen können.

---

### Key Entities

- **Transaktion**: Atomarer Kassiervorgang; trägt Datum/Uhrzeit (UTC), Bedienerkennung, Zahlungsart, Gesamtbetrag (Cent), TSE-Prüfwert, Transaktionsnummer (Sequenz + UUID), Status (abgeschlossen/storniert). Unveränderlich nach Abschluss.
- **TransaktionsPosition**: Einzelne Zeile einer Transaktion; verlinkt auf SKU, enthält Menge, Einzelpreis-Snapshot (Cent), angewendeten USt-Satz, angewendete Preisregel (optional).
- **SKU (Artikel-Variante)**: Verkaufbare Einheit; gehört zu genau einem Artikel, hat eindeutigen Namen (inkl. Variantenbezeichnung), Normalpreis (Cent), Lagerbestand, Meldebestand, USt-Satz.
- **Artikel**: Übergeordnete Produktgruppe (z. B. „Weizen"); enthält 1–n SKUs und 0–n Preisregeln.
- **Preisregel**: Zeitbasierte Rabattdefinition (Wochentag, Uhrzeit von/bis, Rabatt in %); gilt für Artikel oder Warengruppe.
- **Tisch**: Repräsentiert einen physischen Tisch; hat Status (offen/geschlossen), zugeordnete Bedienerkennung, Liste offener Positionen.
- **Bediener**: Kassenpersonal; hat eindeutige ID, Anzeigename, Rolle (regulär/Trainee), Identifikationsmerkmal (PIN oder Kurzname).
- **Schicht**: Zeitraum zwischen Öffnung und Kassensturz; trägt Bediener-ID, Startzeitpunkt, Endzeitpunkt, Soll- und Ist-Kassenbestand.
- **Bon**: Unveränderliche Druckrepräsentation einer Transaktion; enthält alle Pflichtangaben nach § 146a AO; archiviert als strukturierter Datensatz.
- **Z-Bon**: Tagesendsummen-Dokument; erzeugt beim Tagesabschluss; verlinkt auf alle Transaktionen und Schichten des Tages; trägt Nullstellungszähler.
- **TSE-Protokolleintrag**: Technisches Sicherheitsprotokoll pro Transaktion; enthält Transaktionsnummer, Zeitraum (Start/Ende), Prüfwert, TSE-Seriennummer. Unveränderlich.
- **Verfahrensdokumentation**: Systemkonfigurationsdatensatz; enthält Kassenfabrikat, Seriennummer, Betriebsstätte, Inbetriebnahmedatum, Änderungshistorie.
- **DSFinV-K-Export**: Zeitraumbezogener Datenexport; bündelt Transaktionen, Bons und TSE-Protokolle im standardisierten Format für die Finanzverwaltung.

---

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Ein Barkeeper schließt eine Standardtransaktion (Artikel antippen → Zahlung bestätigen → Bon gedruckt) in unter 30 Sekunden ab.
- **SC-002**: Der Z-Bon enthält nach einem Tagesabschluss alle gesetzlich vorgeschriebenen Pflichtangaben ohne manuellen Eingriff — prüfbar anhand der Pflichtfelder-Checkliste aus FR-010.
- **SC-003**: Ein DSFinV-K-Export für den gesamten verfügbaren Datenzeitraum steht in unter 5 Minuten bereit (Vorbereitung Kassennachschau § 146b AO).
- **SC-004**: Das System funktioniert bei unterbrochener Internetverbindung für mindestens 24 Stunden ohne Einschränkung der Kassierfunktionen (Offline-Puffer für TSE-Signaturen gemäß BSI TR-03153).
- **SC-005**: 95 % der Bediener schließen eine Transaktion bei der ersten selbstständigen Nutzung erfolgreich ab.
- **SC-006**: Alle Transaktionsdaten sind nach 10 Jahren noch vollständig lesbar und maschinell in DSFinV-K exportierbar.
- **SC-007**: Eine simulierte unangekündigte Kassennachschau kann vollständig bedient werden (Datenzugriff bereitstellen, Verfahrensdokumentation vorzeigen) in unter 5 Minuten.

---

## Assumptions

- Cloud-TSE: Deutsche Fiskal (BSI TR-03153 zertifiziert)
- Android-Tablet: mind. 10 Zoll, Android 10+, dauerhaft an Stromversorgung angeschlossen
- Bar-Betrieb mit primär 19 % USt (Gaststättenverzehr); 7 % für Außer-Haus (konfigurierbar)
- Externe EC-Terminal-Integration: Das System markiert EC-Zahlungen als „unbar"; die eigentliche Kartentransaktion läuft über ein separates Gerät (keine direkte P2PE-Integration in V1)
- Drucker: ESC/POS-kompatibler Bondrucker (Bluetooth oder WLAN)
- Betrieb im Inland (Deutschland), Währung EUR
- Einzel-Tablet-Betrieb (1 Kassenplatz); Mehrplatz-Erweiterung ist außerhalb von V1
- Gesetzesstand: KassenSichV, GoBD, AO in der zum 2026-02-25 gültigen Fassung
