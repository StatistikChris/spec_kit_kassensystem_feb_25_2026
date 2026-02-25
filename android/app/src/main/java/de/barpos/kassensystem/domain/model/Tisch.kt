package de.barpos.kassensystem.domain.model

import org.threeten.bp.Instant

/**
 * T011 — Tisch domain model.
 *
 * Represents a physical table in the bar.
 * [version] is used for optimistic locking (EC-08: simultaneous table + Direktkassierung).
 */
data class Tisch(
    val id: Long = 0,
    val bezeichnung: String,   // e.g. "Tisch 3", "Bar"
    val status: TischStatus = TischStatus.FREI,
    val version: Long = 0,     // incremented on every state change; CAS guard
    val updatedAt: Instant
)

enum class TischStatus {
    FREI,
    OFFEN,
    RESERVIERT
}
