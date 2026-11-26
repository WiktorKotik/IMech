package com.example.imech.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Краткое описание: тема приложения ИМЭЧ (PRESENTATION).
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    tertiary = Tertiary,
    background = Background,
    surface = Surface,
    error = ErrorColor
)

@Composable
fun ImechTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}
