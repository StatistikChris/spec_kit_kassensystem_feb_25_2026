package de.barpos.kassensystem.ui.navigation

/**
 * T037 — Sealed hierarchy of all navigable destinations.
 * Screens are referenced by their [route] string in [KassenNavGraph].
 */
sealed class Screen(val route: String) {

    // ── Phase 3: US-1 Table overview ─────────────────────────────────────────
    data object Tischplan : Screen("tischplan")

    // ── Phase 4: US-2 Order entry ─────────────────────────────────────────────
    data class Bestellung(val tischId: Long = 0L) : Screen("bestellung/{tischId}") {
        companion object {
            const val ROUTE = "bestellung/{tischId}"
            fun withArgs(tischId: Long) = "bestellung/$tischId"
        }
    }

    // ── Phase 5: US-3 Payment ─────────────────────────────────────────────────
    data class Zahlung(val transaktionId: Long = 0L) : Screen("zahlung/{transaktionId}") {
        companion object {
            const val ROUTE = "zahlung/{transaktionId}"
            fun withArgs(id: Long) = "zahlung/$id"
        }
    }

    // ── Phase 6: US-4 Cancellation ────────────────────────────────────────────
    data class Storno(val bonId: Long = 0L) : Screen("storno/{bonId}") {
        companion object {
            const val ROUTE = "storno/{bonId}"
            fun withArgs(bonId: Long) = "storno/$bonId"
        }
    }

    // ── Phase 7: US-5 Z-Bon / daily close ────────────────────────────────────
    data object Tagesabschluss : Screen("tagesabschluss")

    // ── Phase 8: US-6 Inventory ───────────────────────────────────────────────
    data object Artikelverwaltung : Screen("artikelverwaltung")

    // ── Phase 9: US-7 Reports / export ───────────────────────────────────────
    data object Berichte : Screen("berichte")

    // ── Shared ────────────────────────────────────────────────────────────────
    data object Einstellungen : Screen("einstellungen")
    data object AusfallzeitDialog : Screen("ausfallzeit_dialog")
}
