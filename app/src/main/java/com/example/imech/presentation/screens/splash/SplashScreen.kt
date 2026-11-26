package com.example.imech.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Краткое описание: SplashScreen (модуль Б, п.1).
 * Показывает логотип приложения 1,5 секунды, затем открывает
 * следующий экран: онбординг при первом запуске либо последний экран.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
@Composable
fun SplashScreen(
    nextRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(1500)
        onNavigate(nextRoute)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Логотип ИМЭЧ",
            tint = Color.White,
            modifier = Modifier.size(96.dp)
        )
        Text(
            text = "ИМЭЧ",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.offset(y = 96.dp)
        )
    }
}
