package kr.co.oadan.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val OadanLightColors = lightColorScheme(
    primary = OasisTeal,
    onPrimary = Color.White,
    primaryContainer = Teal50,
    onPrimaryContainer = Teal900,
    secondary = OrganicSage,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDF1E7),
    onSecondaryContainer = Color(0xFF233614),
    tertiary = SunsetCoral,
    onTertiary = DeepInk,
    background = Sand,
    onBackground = DeepInk,
    surface = Color.White,
    onSurface = DeepInk,
    surfaceVariant = Dune,
    onSurfaceVariant = Stone,
    outline = Pebble,
    error = ErrorRed,
    onError = Color.White
)

val OadanDarkColors = darkColorScheme(
    primary = Color(0xFF72C1B2),
    onPrimary = Color(0xFF063530),
    primaryContainer = Color(0xFF0B4D44),
    onPrimaryContainer = Color(0xFFC6E7E0),
    secondary = Color(0xFFA5BF93),
    onSecondary = Color(0xFF1B2C15),
    secondaryContainer = Color(0xFF2F4522),
    onSecondaryContainer = Color(0xFFCCE4BD),
    tertiary = Color(0xFFF2A27E),
    onTertiary = DeepInk,
    background = Color(0xFF0F1A19),
    onBackground = Color(0xFFE6EFEC),
    surface = Color(0xFF182624),
    onSurface = Color(0xFFE6EFEC),
    surfaceVariant = Color(0xFF22322F),
    onSurfaceVariant = Color(0xFFA3B1AD),
    outline = Color(0xFF3A4A47),
    error = Color(0xFFEF8A82),
    onError = Color(0xFF4E0E0A)
)

@Composable
fun OadanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) OadanDarkColors else OadanLightColors
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = OadanTypography,
        content = content
    )
}
