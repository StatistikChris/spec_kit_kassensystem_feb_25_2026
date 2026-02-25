package de.barpos.kassensystem.domain.model

import org.threeten.bp.Instant

/**
 * T012 — Z-Bon domain model.
 *
 * End-of-day totals document (Tagesabschluss). Once persisted, IMMUTABLE.
 * Links all Transaktionen and Schichten of the period via the Z-Bon nummer.
 *
 * [nullstellungszaehler] is a monotonically increasing counter across all Z-Bons.
 * [entnahmenInCent] represents cash withdrawals during the period (finding C1 fix).
 */
data class ZBon(
    val id: Long = 0,
    val nummer: Long,                           // auto-incrementing Z-Bon number
    val vonZeit: Instant,                       // start of period (previous Z-Bon time)
    val bisZeit: Instant,                       // end of period (this Z-Bon time)
    val gesamtInCent: Long,                     // total gross revenue
    val barInCent: Long,
    val externInCent: Long,
    val mwst7InCent: Long,
    val mwst19InCent: Long,
    val entnahmenInCent: Long = 0L,             // cash withdrawals (FR-010)
    val anzahlBuchungen: Int,
    val anzahlStornos: Int,
    val traineeUmsatzInCent: Long = 0L,         // separately required per FR-011
    val nullstellungszaehler: Long,
    val bedienerId: Long,
    val tseSignatur: String,
    val tseSerialNumber: String,
    val createdAt: Instant
)
