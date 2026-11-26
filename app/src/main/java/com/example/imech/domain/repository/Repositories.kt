package com.example.imech.domain.repository

import com.example.imech.domain.model.Game
import com.example.imech.domain.model.User

/**
 * Краткое описание: интерфейсы репозиториев (DOMAIN).
 * Абстракции для получения данных: слой presentation не знает,
 * откуда приходят данные (модуль В - вызовы сервера через абстракцию).
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */

/** Аутентификация: регистрация и авторизация пользователя. */
interface AuthRepository {
    /**
     * Регистрация нового пользователя.
     * @param email адрес почты
     * @param password пароль
     * @return зарегистрированный пользователь либо ошибка
     */
    suspend fun register(email: String, password: String): Result<User>

    /**
     * Авторизация существующего пользователя.
     * @return пользователь либо ошибка InvalidCredentials
     */
    suspend fun login(email: String, password: String): Result<User>
}

/** Работа с играми: создание и получение списка. */
interface GamesRepository {
    /**
     * Создание игры на сервере (экран Schedule Game).
     * @param date дата в формате дд-мм-гггг
     * @param time время в формате чч:мм
     */
    suspend fun createGame(date: String, time: String): Result<Game>

    /** Список игр текущего пользователя. */
    suspend fun getGames(): Result<List<Game>>
}
