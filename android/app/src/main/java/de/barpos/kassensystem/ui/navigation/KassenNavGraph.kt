package de.barpos.kassensystem.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import de.barpos.kassensystem.ui.tischplan.TischplanScreen

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

        composable(Screen.Tischplan.route) {
            TischplanScreen(
                onTischClick = { tischId ->
                    navController.navigate(Screen.Bestellung.withArgs(tischId))
                }
            )
        }

        composable(
            route = Screen.Bestellung.ROUTE,
            arguments = listOf(navArgument("tischId") { type = NavType.LongType })
        ) { backStack ->
            val tischId = backStack.arguments?.getLong("tischId") ?: 0L
            PlaceholderScreen("Bestellung Tisch $tischId (US-2)")
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
