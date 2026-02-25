package de.barpos.kassensystem.domain.model

import org.threeten.bp.Instant

/**
 * T012 — TSE protocol entry (Technisches Sicherheitsprotokoll).
 *
 * One entry per transaction lifecycle event (START, UPDATE, FINISH, CANCEL).
 * Immutable after creation — stored verbatim from the TSE response.
 * Required per KassenSichV § 2 / FR-006.
 */
data class TseProtokollEintrag(
    val id: Long = 0,
    val transaktionId: Long,
    val typ: TseVorgangTyp,
    val tseSerialNumber: String,
    val signatur: String,
    val zeitpunktStart: Instant,
    val zeitpunktEnd: Instant,
    val txNummer: Long,
    val signaturZaehler: Long,
    val vorgangsDaten: String               // raw process data signed by TSE
)

enum class TseVorgangTyp {
    START,
    UPDATE,
    FINISH,
    CANCEL,
    EXPORT
}
