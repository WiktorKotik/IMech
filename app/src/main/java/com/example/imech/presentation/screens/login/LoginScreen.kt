package com.example.imech.presentation.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.imech.presentation.components.ErrorBanner
import com.example.imech.presentation.viewmodels.AuthViewModel

/**
 * Краткое описание: экран авторизации (модуль Б, п.4).
 * Взаимодействует с сервером через абстракцию (модуль В).
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Log In",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = viewModel::onEmailChanged,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = viewModel::onPasswordChanged,
                label = { Text("Пароль") },
                trailingIcon = {
                    TextButton(onClick = viewModel::togglePasswordVisibility) {
                        Text(if (viewModel.passwordVisible) "Скрыть" else "Показать")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.login(onSuccess = onLoginSuccess) },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) { Text(if (viewModel.isLoading) "Входим..." else "Войти") }
            TextButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) { Text("Нет аккаунта? Зарегистрироваться") }
        }

        ErrorBanner(
            message = viewModel.serverError,
            onDismiss = viewModel::dismissError,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
