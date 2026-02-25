package de.barpos.kassensystem.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─── Colour tokens ───────────────────────────────────────────────────────────
private val BrandAmber = Color(0xFFFFC107)
private val BrandAmberDark = Color(0xFFFF8F00)
private val SurfaceDeep = Color(0xFF1A1A1A)
private val SurfaceCard = Color(0xFF2C2C2C)
private val OnSurface = Color(0xFFEEEEEE)
private val ErrorRed = Color(0xFFCF6679)

private val KassenDarkColorScheme = darkColorScheme(
    primary = BrandAmber,
    onPrimary = Color.Black,
    primaryContainer = BrandAmberDark,
    onPrimaryContainer = Color.Black,
    background = SurfaceDeep,
    onBackground = OnSurface,
    surface = SurfaceCard,
    onSurface = OnSurface,
    error = ErrorRed,
    onError = Color.Black
)

/**
 * T036 — App-wide Material3 theme.
 * Always dark (bar environment). Typography and shape use M3 defaults.
 */
@Composable
fun KassenTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KassenDarkColorScheme,
        content = content
    )
}
