package com.example.imech.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.imech.data.local.AppPreferences
import com.example.imech.data.repositoryImpl.GamesRepositoryImpl
import com.example.imech.presentation.screens.document.DocumentScreen
import com.example.imech.presentation.screens.landing.LandingScreen
import com.example.imech.presentation.screens.login.LoginScreen
import com.example.imech.presentation.screens.onboarding.OnboardingScreen
import com.example.imech.presentation.screens.register.RegisterScreen
import com.example.imech.presentation.screens.schedule.ScheduleGameScreen
import com.example.imech.presentation.screens.splash.SplashScreen
import com.example.imech.presentation.screens.success.SuccessScreen
import com.example.imech.presentation.viewmodels.AuthViewModel
import com.example.imech.presentation.viewmodels.ScheduleViewModel

/**
 * Краткое описание: навигация между экранами приложения (PRESENTATION).
 * Каждый переход сохраняется как «последний экран» (модуль Г):
 * после перезапуска приложение открывает его вместо Landing.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
@Composable
fun ImechNavHost(
    navController: NavHostController,
    prefs: AppPreferences,
    authViewModel: AuthViewModel,
    scheduleViewModel: ScheduleViewModel,
    gamesRepository: GamesRepositoryImpl
) {
    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            LaunchedEffect(Unit) { prefs.saveLastScreen("landing") }
            SplashScreen(
                nextRoute = if (prefs.isOnboardingDone()) {
                    prefs.loadLastScreen()?.takeIf { it != "splash" && it != "onboarding" } ?: "landing"
                } else {
                    "onboarding"
                },
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("onboarding") {
            LaunchedEffect(Unit) {
                prefs.saveLastScreen("landing")
                prefs.setOnboardingDone()
            }
            OnboardingScreen(onFinish = {
                navController.navigate("register") { popUpTo("onboarding") { inclusive = true } }
            })
        }

        composable("register") {
            LaunchedEffect(Unit) { prefs.saveLastScreen("register") }
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { email ->
                    gamesRepository.currentEmail = email
                    navController.navigate("login") { popUpTo("register") { inclusive = true } }
                },
                onBackToLogin = { navController.navigate("login") }
            )
        }

        composable("login") {
            LaunchedEffect(Unit) { prefs.saveLastScreen("landing") }
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    gamesRepository.currentEmail = authViewModel.email
                    navController.navigate("landing") { popUpTo("login") { inclusive = true } }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("landing") {
            LaunchedEffect(Unit) { prefs.saveLastScreen("landing") }
            LandingScreen(
                viewModel = scheduleViewModel,
                onOpenSchedule = { navController.navigate("schedule") },
                onOpenDocument = { docId -> navController.navigate("document/$docId") },
                onLogout = {
                    navController.navigate("login") { popUpTo("landing") { inclusive = true } }
                }
            )
        }

        composable("schedule") {
            LaunchedEffect(Unit) { prefs.saveLastScreen("schedule") }
            ScheduleGameScreen(
                viewModel = scheduleViewModel,
                onPublishSuccess = {
                    navController.navigate("success") { popUpTo("landing") }
                }
            )
        }

        composable("success") {
            LaunchedEffect(Unit) { prefs.saveLastScreen("landing") }
            SuccessScreen(onFinished = {
                navController.navigate("landing") { popUpTo("success") { inclusive = true } }
            })
        }

        composable("document/{docId}") { entry ->
            val docId = entry.arguments?.getString("docId") ?: "privacy_policy"
            DocumentScreen(
                docId = docId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
