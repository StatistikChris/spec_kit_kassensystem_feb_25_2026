package de.barpos.kassensystem.domain.model

/**
 * T011 — Bediener domain model.
 *
 * [pinHash] is a SHA-256 hex digest of the PIN — never stored in plaintext.
 * [isTraining] marks the operator as a Trainee; their transactions appear
 * separately on the Z-Bon and are excluded from Z-Bon revenue totals (FR-011).
 */
data class Bediener(
    val id: Long = 0,
    val name: String,
    val kurzname: String,           // max 4 chars, shown on receipts
    val pinHash: String,            // SHA-256(pin) hex
    val rolle: BedienerRolle,
    val isTraining: Boolean = false,
    val aktiv: Boolean = true
)

enum class BedienerRolle {
    ADMIN,
    BEDIENER,
    TRAINEE
}
