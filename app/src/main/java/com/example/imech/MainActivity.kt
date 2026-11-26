package com.example.imech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.imech.data.local.AppPreferences
import com.example.imech.data.remote.MockRemoteDataSource
import com.example.imech.data.repositoryImpl.AuthRepositoryImpl
import com.example.imech.data.repositoryImpl.GamesRepositoryImpl
import com.example.imech.domain.usecase.CreateGameUseCase
import com.example.imech.domain.usecase.GetGamesUseCase
import com.example.imech.domain.usecase.LoginUseCase
import com.example.imech.domain.usecase.RegisterUseCase
import com.example.imech.presentation.navigation.ImechNavHost
import com.example.imech.presentation.theme.ImechTheme
import com.example.imech.presentation.viewmodels.AuthViewModel
import com.example.imech.presentation.viewmodels.ScheduleViewModel

/**
 * Краткое описание: точка входа приложения ИМЭЧ.
 * Собирает слои приложения (DI вручную): DATA (локальное хранилище
 * и имитация REST API), DOMAIN (use case'ы), PRESENTATION (экраны).
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DATA слой
        val prefs = AppPreferences(this)
        val remote = MockRemoteDataSource(prefs)
        val authRepository = AuthRepositoryImpl(remote)
        val gamesRepository = GamesRepositoryImpl(remote)

        // DOMAIN слой (use case'ы)
        val registerUseCase = RegisterUseCase(authRepository)
        val loginUseCase = LoginUseCase(authRepository)
        val createGameUseCase = CreateGameUseCase(gamesRepository)
        val getGamesUseCase = GetGamesUseCase(gamesRepository)

        // PRESENTATION слой (ViewModel'ы)
        val authViewModel = AuthViewModel(registerUseCase, loginUseCase)
        val scheduleViewModel = ScheduleViewModel(createGameUseCase, getGamesUseCase)

        setContent {
            ImechTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ImechNavHost(
                        navController = rememberNavController(),
                        prefs = prefs,
                        authViewModel = authViewModel,
                        scheduleViewModel = scheduleViewModel,
                        gamesRepository = gamesRepository
                    )
                }
            }
        }
    }
}
