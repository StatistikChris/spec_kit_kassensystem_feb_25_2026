package de.barpos.kassensystem.domain.model

/**
 * T010 — Artikel domain model.
 *
 * A product group (e.g. "Weizen"). One Artikel has 1..n SKUs (variants).
 * [mwstSatz] is the default VAT rate for all SKUs under this Artikel.
 * Individual SKUs may override the rate.
 */
data class Artikel(
    val id: Long = 0,
    val name: String,
    val kategorie: String,
    val mwstSatz: Int = 19,    // default 19 %; 7 % for Außer-Haus (configurable)
    val aktiv: Boolean = true
)
