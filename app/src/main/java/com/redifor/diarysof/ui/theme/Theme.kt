package com.redifor.diarysof.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FireColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant
)

private val PurpleColorScheme = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = PurpleDeep,
    onPrimaryContainer = Color(0xFFF3E5F5),
    
    secondary = GoldenYellow,
    onSecondary = Color(0xFF3E2723),
    secondaryContainer = Color(0xFF5D4037),
    onSecondaryContainer = Color(0xFFFFE082),
    
    tertiary = PurpleLight,
    onTertiary = Color(0xFF4A148C),
    tertiaryContainer = Color(0xFF7B1FA2),
    onTertiaryContainer = Color(0xFFF3E5F5),
    
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    
    background = PurpleDarkBg,
    onBackground = Color(0xFFE8DEF8),
    surface = PurpleDarkBg,
    onSurface = Color(0xFFE8DEF8),
    surfaceVariant = PurpleLightSmoke,
    onSurfaceVariant = Color(0xFFD0BCFF)
)

@Composable
fun RedFortuneDiaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    usePurpleTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (usePurpleTheme) PurpleColorScheme else FireColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
