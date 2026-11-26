package com.example.imech.data.repositoryImpl

import com.example.imech.data.mapper.toDomain
import com.example.imech.data.remote.RemoteDataSource
import com.example.imech.domain.model.Game
import com.example.imech.domain.model.User
import com.example.imech.domain.repository.AuthRepository
import com.example.imech.domain.repository.GamesRepository

/**
 * Краткое описание: реализации репозиториев (DATA).
 * Работают только с абстракцией RemoteDataSource: при замене
 * сетевой библиотеки меняется только этот слой.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */

/** Реализация репозитория аутентификации через REST API. */
class AuthRepositoryImpl(
    private val remote: RemoteDataSource
) : AuthRepository {

    override suspend fun register(email: String, password: String): Result<User> =
        remote.register(email, password).map { it.toDomain() }

    override suspend fun login(email: String, password: String): Result<User> =
        remote.login(email, password).map { it.toDomain() }
}

/** Реализация репозитория игр через REST API. */
class GamesRepositoryImpl(
    private val remote: RemoteDataSource
) : GamesRepository {

    override suspend fun createGame(date: String, time: String): Result<Game> =
        remote.createGame(ownerEmail = currentEmail, date = date, time = time).map { it.toDomain() }

    override suspend fun getGames(): Result<List<Game>> =
        remote.fetchGames(ownerEmail = currentEmail).map { list -> list.map { it.toDomain() } }

    /** Email текущего пользователя задаётся после успешной авторизации или регистрации. */
    var currentEmail: String = ""
        set(value) {
            field = value.trim().lowercase()
        }
}
