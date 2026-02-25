package de.barpos.kassensystem.domain.model

import org.threeten.bp.Instant

/**
 * T012 — Bon domain model.
 *
 * Immutable receipt record. Created once per completed [Transaktion].
 * Contains all Pflichtangaben per § 146a Abs. 2 AO.
 *
 * [isNachdruck] is set to true when the bon is reprinted (T100).
 * It does NOT guard against storno — storno checks use BonDao.findByTransaktionId().
 */
data class Bon(
    val id: Long = 0,
    val transaktionId: Long,
    val zeitstempel: Instant,
    val tseSignatur: String,
    val tseSerialNumber: String,
    val tseZeitpunktStart: Instant,
    val tseZeitpunktEnd: Instant,
    val tseTxNummer: Long,
    val isNachdruck: Boolean = false,
    val nachdruckZeit: Instant? = null
)
