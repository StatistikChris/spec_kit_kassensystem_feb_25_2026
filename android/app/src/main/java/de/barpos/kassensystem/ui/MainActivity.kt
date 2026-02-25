package de.barpos.kassensystem.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import de.barpos.kassensystem.ui.navigation.KassenNavGraph
import de.barpos.kassensystem.ui.theme.KassenTheme

/**
 * T038 — Single-Activity entry point.
 * - Annotated with @AndroidEntryPoint so Hilt can inject ViewModels in the compose tree.
 * - Keeps the screen on (persistent tablet kiosk mode).
 * - Landscape orientation is enforced in AndroidManifest.xml.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep screen on — bar POS must never auto-dim/lock during service
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            KassenTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    KassenNavGraph(navController = navController)
                }
            }
        }
    }
}
