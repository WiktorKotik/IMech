package com.example.imech.domain.usecase

import com.example.imech.domain.model.Game
import com.example.imech.domain.model.User
import com.example.imech.domain.repository.AuthRepository
import com.example.imech.domain.repository.GamesRepository

/**
 * Краткое описание: сценарии использования (DOMAIN, Use Cases).
 * Каждый сценарий - отдельный класс с методом invoke (SOLID).
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */

/** Регистрация нового пользователя (экран Create Account). */
class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> =
        repository.register(email, password)
}

/** Авторизация пользователя (экран Log In). */
class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> =
        repository.login(email, password)
}

/** Создание игры на выбранную дату и время (экран Schedule Game). */
class CreateGameUseCase(private val repository: GamesRepository) {
    suspend operator fun invoke(date: String, time: String): Result<Game> =
        repository.createGame(date, time)
}

/** Получение списка игр текущего пользователя (экран Landing). */
class GetGamesUseCase(private val repository: GamesRepository) {
    suspend operator fun invoke(): Result<List<Game>> = repository.getGames()
}
