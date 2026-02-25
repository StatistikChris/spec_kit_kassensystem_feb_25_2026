package de.barpos.kassensystem.domain.repository

/**
 * Bon/receipt printer abstraction (T027a).
 * Concrete implementation is [de.barpos.kassensystem.data.printer.BonDruckerImpl].
 * May target an ESC/POS network printer over TCP/IP.
 */
interface IBonDrucker {

    /** Print a receipt for the given Bon ID. */
    suspend fun bonDrucken(bonId: Long): DruckErgebnis

    /** Print a reprint copy flagged as "KOPIE". */
    suspend fun nachdruckDrucken(bonId: Long): DruckErgebnis

    /** Print a daily Z-Bon summary. */
    suspend fun zBonDrucken(zBonId: Long): DruckErgebnis
}

sealed class DruckErgebnis {
    data object Erfolg : DruckErgebnis()
    data class Fehler(val meldung: String, val ursache: Throwable? = null) : DruckErgebnis()
    data object NichtVerbunden : DruckErgebnis()
}
