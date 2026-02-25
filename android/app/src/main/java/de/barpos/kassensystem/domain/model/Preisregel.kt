package de.barpos.kassensystem.domain.model

import org.threeten.bp.DayOfWeek
import org.threeten.bp.Instant
import org.threeten.bp.LocalTime

/**
 * T010 — Preisregel (Happy Hour / time-based pricing rule).
 *
 * Rules are evaluated by [PreisregelEngine] on every position add.
 * Multiple rules can be active simultaneously; [prioritaet] determines precedence.
 *
 * [weekdayBitmask] encodes active weekdays as bit flags:
 *   bit 0 = Monday, bit 1 = Tuesday … bit 6 = Sunday  (ISO order)
 *
 * [artikelFilter] determines scope:
 *   - ALLE: applies to all SKUs
 *   - KATEGORIE: applies to all SKUs in given [filterWert] category
 *   - SKU_ID: applies only to the SKU whose ID matches [filterWert]
 */
data class Preisregel(
    val id: Long = 0,
    val bezeichnung: String,
    val startZeit: LocalTime,
    val endZeit: LocalTime,
    val weekdayBitmask: Int = 0b1111111,    // all days active by default
    val rabattTyp: RabattTyp,
    val rabattWert: Long,                   // percent (0-100) for PROZENT; cents for ABZUG; override price for FESTPREIS
    val artikelFilter: ArtikelFilter = ArtikelFilter.ALLE,
    val filterWert: String? = null,         // category name or SKU id
    val prioritaet: Int = 0,               // higher = evaluated first
    val aktiv: Boolean = true,
    val createdAt: Instant
) {
    fun isActiveOnDay(day: DayOfWeek): Boolean {
        val bit = day.value - 1  // Monday=0 … Sunday=6
        return weekdayBitmask and (1 shl bit) != 0
    }
}

enum class RabattTyp {
    PROZENT,    // e.g. 20 % off
    ABZUG,      // e.g. 50 ct off
    FESTPREIS   // override to fixed price in cents
}

enum class ArtikelFilter {
    ALLE,
    KATEGORIE,
    SKU_ID
}
