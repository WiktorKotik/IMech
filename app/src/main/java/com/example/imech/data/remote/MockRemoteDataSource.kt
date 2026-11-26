package com.example.imech.data.remote

import com.example.imech.data.local.AppPreferences
import com.example.imech.domain.exception.DomainException
import kotlinx.coroutines.delay

/**
 * Краткое описание: реализация RemoteDataSource - имитация SAAS-бэкенда
 * с REST API (задержка сети, проверки на «сервере»).
 * Зарегистрированные пользователи сохраняются через AppPreferences,
 * поэтому аккаунт живёт и после перезапуска приложения.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
class MockRemoteDataSource(private val prefs: AppPreferences) : RemoteDataSource {

    private var nextUserId = 1
    private var nextGameId = 1
    private val games = mutableListOf<GameDto>()

    /** Имитация сетевого запроса к REST API. */
    private suspend fun simulateNetwork() {
        delay(600)
    }

    override suspend fun register(email: String, password: String): Result<UserDto> =
        try {
            simulateNetwork()
            if (prefs.findUser(email) != null) {
                throw DomainException.EmailAlreadyTaken()
            }
            prefs.registerUser(email, password)
            Result.success(UserDto(id = nextUserId++, email = email))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun login(email: String, password: String): Result<UserDto> =
        try {
            simulateNetwork()
            val user = prefs.findUser(email)
                ?: throw DomainException.InvalidCredentials()
            if (user.second != password) {
                throw DomainException.InvalidCredentials()
            }
            Result.success(UserDto(id = nextUserId++, email = email))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun createGame(
        ownerEmail: String,
        date: String,
        time: String
    ): Result<GameDto> =
        try {
            simulateNetwork()
            val game = GameDto(id = nextGameId++, ownerEmail = ownerEmail, date = date, time = time)
            games.add(game)
            Result.success(game)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun fetchGames(ownerEmail: String): Result<List<GameDto>> =
        try {
            simulateNetwork()
            Result.success(games.filter { it.ownerEmail == ownerEmail })
        } catch (e: Exception) {
            Result.failure(e)
        }
}
