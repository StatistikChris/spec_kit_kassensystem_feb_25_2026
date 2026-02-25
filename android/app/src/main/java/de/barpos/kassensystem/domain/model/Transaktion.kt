package de.barpos.kassensystem.domain.model

import org.threeten.bp.Instant

/**
 * T009 — Core transaction domain model.
 *
 * Represents a single, atomic cash-register operation.
 * Once [status] is [TransaktionStatus.ABGESCHLOSSEN] and [locked] is true,
 * this record is IMMUTABLE — no field may be altered (GoBD §3, §146 Abs.4 AO).
 *
 * All monetary values are expressed in euro-cent (Long) — never Float/Double.
 */
data class Transaktion(
    val id: Long = 0,
    val uuid: String,                          // stable UUID for cross-system reference
    val tischId: Long?,                        // null = Direktkassierung
    val bedienerId: Long,
    val schichtId: Long,
    val status: TransaktionStatus,
    val zahlungsart: Zahlungsart,
    val gesamtInCent: Long,                    // gross total in euro-cent
    val stornoVonId: Long?,                    // if set: this is a Gegenbuchung
    val isTraining: Boolean = false,           // Trainee-Buchung: excluded from Z-Bon totals
    val locked: Boolean = false,               // true after Z-Bon; no writes allowed
    val createdAt: Instant,
    val updatedAt: Instant,
    val positionen: List<TransaktionsPosition> = emptyList()
)

enum class TransaktionStatus {
    OFFEN,           // transaction started, not yet paid
    ABGESCHLOSSEN,   // fully paid, TSE-signed, bon printed
    STORNIERT        // voided via Gegenbuchung
}

enum class Zahlungsart {
    BAR,    // cash — flows into Kassenbuch Bargeldsaldo
    EXTERN  // EC/credit card via external terminal — marked as "unbar", excluded from cash balance
}
