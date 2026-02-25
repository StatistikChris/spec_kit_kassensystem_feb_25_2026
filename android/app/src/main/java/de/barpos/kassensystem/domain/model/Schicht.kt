package de.barpos.kassensystem.domain.model

import org.threeten.bp.Instant

/**
 * T011 — Schicht domain model.
 *
 * Represents a work shift between cash-count handovers.
 * [sollBestandInCent] and [istBestandInCent] are recorded at shift close (US-5 AC-2).
 * Once both values are filled, the record is immutable.
 */
data class Schicht(
    val id: Long = 0,
    val bedienerId: Long,
    val startZeit: Instant,
    val endZeit: Instant? = null,           // null while shift is active
    val sollBestandInCent: Long? = null,    // expected cash balance at close
    val istBestandInCent: Long? = null,     // actual cash balance at close
    val geschlossen: Boolean = false
)
