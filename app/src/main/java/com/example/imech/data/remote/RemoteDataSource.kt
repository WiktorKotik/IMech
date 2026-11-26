package com.example.imech.data.remote

/**
 * Краткое описание: сетевой слой (DATA) - DTO и абстракция источника данных.
 * Все вызовы методов сети идут через этот интерфейс: реализацию
 * (имитацию REST API) можно заменить на Retrofit без изменения остальных слоёв.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */

/** DTO ответа сервера для пользователя. */
data class UserDto(
    val id: Int,
    val email: String
)

/** DTO запроса и ответа сервера для игры. */
data class GameDto(
    val id: Int,
    val ownerEmail: String,
    val date: String,
    val time: String
)

/** Абстракция бэкенда (SAAS-решение с доступом через REST API). */
interface RemoteDataSource {
    /** POST /api/register - регистрация нового пользователя. */
    suspend fun register(email: String, password: String): Result<UserDto>

    /** POST /api/login - авторизация. */
    suspend fun login(email: String, password: String): Result<UserDto>

    /** POST /api/games - сохранение созданной игры на сервере. */
    suspend fun createGame(ownerEmail: String, date: String, time: String): Result<GameDto>

    /** GET /api/games - список игр пользователя. */
    suspend fun fetchGames(ownerEmail: String): Result<List<GameDto>>
}
