package com.example.imech.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imech.domain.usecase.LoginUseCase
import com.example.imech.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

/**
 * Краткое описание: ViewModel экранов регистрации и авторизации (PRESENTATION).
 * Управляет состоянием полей, валидирует email по паттерну
 * «name@domenname.ru» (маленькие буквы и цифры) и вызывает use case'ы.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
class AuthViewModel(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var passwordVisible by mutableStateOf(false)
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var serverError by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set

    companion object {
        // Паттерн из задания: имя и домен - только маленькие буквы и цифры, домен .ru
        private val EMAIL_PATTERN = Regex("^[a-z0-9]+@[a-z0-9]+\\.ru$")
    }

    fun onEmailChanged(value: String) {
        email = value
        emailError = null
    }

    fun onPasswordChanged(value: String) {
        password = value
        passwordError = null
    }

    /** Переключение отображения пароля (задание 3в). */
    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun dismissError() {
        serverError = null
    }

    /**
     * Регистрация: проверка email и пароля, затем вызов use case.
     * @param onSuccess вызывается при успешной регистрации
     */
    fun register(onSuccess: () -> Unit) {
        if (!validate()) return
        isLoading = true
        viewModelScope.launch {
            registerUseCase(email, password).fold(
                onSuccess = {
                    isLoading = false
                    onSuccess()
                },
                onFailure = { error ->
                    isLoading = false
                    serverError = error.message ?: "Ошибка сервера"
                }
            )
        }
    }

    /**
     * Авторизация.
     * @param onSuccess вызывается при успешном входе
     */
    fun login(onSuccess: () -> Unit) {
        if (!validate()) return
        isLoading = true
        viewModelScope.launch {
            loginUseCase(email, password).fold(
                onSuccess = {
                    isLoading = false
                    onSuccess()
                },
                onFailure = { error ->
                    isLoading = false
                    serverError = error.message ?: "Ошибка сервера"
                }
            )
        }
    }

    /** Локальная валидация полей перед отправкой на сервер. */
    private fun validate(): Boolean {
        var ok = true
        if (!EMAIL_PATTERN.matches(email.trim())) {
            emailError = "Введите email вида name@domenname.ru"
            ok = false
        }
        if (password.length < 6) {
            passwordError = "Пароль не короче 6 символов"
            ok = false
        }
        return ok
    }
}
