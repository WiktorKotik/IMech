package com.example.imech.presentation.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
 * Краткое описание: экран «Регистрация» (модуль Б, п.3).
 * Надпись Create Account закреплена сверху, контент при нехватке места
 * прокручивается. Есть проверка email на паттерн «name@domenname.ru»
 * (только маленькие буквы и цифры) и показ пароля.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    // Колбэк принимает email нового пользователя
    onNavigateToLogin: (String) -> Unit,
    onBackToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Фиксированная надпись сверху (задание 3а)
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = viewModel::onEmailChanged,
                    label = { Text("Email") },
                    isError = viewModel.emailError != null,
                    supportingText = { viewModel.emailError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = viewModel::onPasswordChanged,
                    label = { Text("Пароль") },
                    isError = viewModel.passwordError != null,
                    supportingText = { viewModel.passwordError?.let { Text(it) } },
                    trailingIcon = {
                        TextButton(onClick = viewModel::togglePasswordVisibility) {
                            Text(if (viewModel.passwordVisible) "Скрыть" else "Показать")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { viewModel.register(onSuccess = { onNavigateToLogin(viewModel.email) }) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(if (viewModel.isLoading) "Регистрируем..." else "Создать аккаунт") }
                TextButton(
                    onClick = onBackToLogin,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) { Text("Уже есть аккаунт? Войти") }
            }
        }

        // Уведомление об ошибке сервера (модуль В)
        ErrorBanner(
            message = viewModel.serverError,
            onDismiss = viewModel::dismissError,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
