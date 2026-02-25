package de.barpos.kassensystem.domain.model

import org.threeten.bp.Instant

/**
 * T012a — Ausfallzeit entry domain model (EC-06, FR-016).
 *
 * Records system outage intervals. Created by [AppStartupManager] on every app
 * launch by computing the gap between last-recorded shutdown time (DataStore) and
 * the current [Instant.now()].
 *
 * These entries appear in the Verfahrensdokumentation and must be surfaced during
 * Kassennachschau (§ 146b AO audit).
 */
data class AusfallzeitEintrag(
    val id: Long = 0,
    val startZeit: Instant,
    val endZeit: Instant?,              // null if outage is still ongoing (defensive)
    val dauerSekunden: Long?,           // pre-computed for display; null if endZeit null
    val ursache: String = "Systemausfall / unbekannt",
    val bedienerId: Long? = null,       // operator who acknowledged the outage (if any)
    val createdAt: Instant
)
