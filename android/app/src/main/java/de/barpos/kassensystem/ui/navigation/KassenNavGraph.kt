package de.barpos.kassensystem.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import de.barpos.kassensystem.ui.tischplan.TischplanScreen
import de.barpos.kassensystem.ui.bestellung.BestellungScreen

/**
 * T037 — Root NavGraph.
 * Each composable destination is a thin placeholder until the corresponding user-story phase
 * provides the real screen implementation.
 */
@Composable
fun KassenNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Tischplan.route
    ) {

        composable(KassenNavRoutes.TISCHPLAN_ROUTE) {
            TischplanScreen(
                onNavigateToBestellung = { tischId, tischNummer ->
                    navController.navigate("${KassenNavRoutes.BESTELLUNG_ROOT}/$tischId/$tischNummer")
                }
            )
        }

        composable(
            route = KassenNavRoutes.BESTELLUNG_ROUTE,
            arguments = listOf(
                navArgument(KassenNavArgs.TISCH_ID_ARG) { type = NavType.LongType },
                navArgument(KassenNavArgs.TISCH_NUMMER_ARG) { type = NavType.StringType }
            )
        ) {
            BestellungScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(KassenNavRoutes.PLATZHALTER_ROUTE) {
            PlaceholderScreen(text = "Platzhalter")
        }
    }
}

        composable(
            route = Screen.Zahlung.ROUTE,
            arguments = listOf(navArgument("transaktionId") { type = NavType.LongType })
        ) { backStack ->
            val id = backStack.arguments?.getLong("transaktionId") ?: 0L
            PlaceholderScreen("Zahlung TX $id (US-3)")
        }

        composable(
            route = Screen.Storno.ROUTE,
            arguments = listOf(navArgument("bonId") { type = NavType.LongType })
        ) { backStack ->
            val bonId = backStack.arguments?.getLong("bonId") ?: 0L
            PlaceholderScreen("Storno Bon $bonId (US-4)")
        }

        composable(Screen.Tagesabschluss.route) {
            PlaceholderScreen("Tagesabschluss / Z-Bon (US-5)")
        }

        composable(Screen.Artikelverwaltung.route) {
            PlaceholderScreen("Artikelverwaltung (US-6)")
        }

        composable(Screen.Berichte.route) {
            PlaceholderScreen("Berichte & Export (US-7)")
        }

        composable(Screen.Einstellungen.route) {
            PlaceholderScreen("Einstellungen")
        }

        composable(Screen.AusfallzeitDialog.route) {
            PlaceholderScreen("Systemausfall bestätigen (EC-06)")
        }
    }
}

object KassenNavArgs {
    const val TISCH_ID_ARG = "tischId"
    const val TISCH_NUMMER_ARG = "tischNummer"
}

object KassenNavRoutes {
    const val TISCHPLAN_ROUTE = "tischplan"
    const val BESTELLUNG_ROOT = "bestellung"
    const val BESTELLUNG_ROUTE = "$BESTELLUNG_ROOT/{${KassenNavArgs.TISCH_ID_ARG}}/{${KassenNavArgs.TISCH_NUMMER_ARG}}"
    const val PLATZHALTER_ROUTE = "platzhalter"
}
