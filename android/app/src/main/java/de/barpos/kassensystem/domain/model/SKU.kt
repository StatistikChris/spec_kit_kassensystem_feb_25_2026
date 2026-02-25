package de.barpos.kassensystem.domain.model

/**
 * T010 — SKU (Artikel-Variante) domain model.
 *
 * The sellable unit. Each SKU belongs to exactly one [Artikel].
 * [variantenAttributen] holds free-form attributes (e.g. {"groesse": "0.5l"}).
 *
 * Inventory: [bestand] is decremented on every completed transaction (US-7).
 * [meldebestand] triggers a low-stock warning when [bestand] <= [meldebestand].
 */
data class SKU(
    val id: Long = 0,
    val artikelId: Long,
    val bezeichnung: String,                        // full name incl. variant, e.g. "Weizen 0,5 l"
    val variantenAttributen: Map<String, String> = emptyMap(), // e.g. {"groesse":"0.5l"}
    val normalpreisInCent: Long,
    val mwstSatz: Int = 19,
    val bestand: Int = 0,
    val meldebestand: Int = 5,
    val aktiv: Boolean = true
)
