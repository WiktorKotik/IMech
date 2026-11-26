package com.example.imech.domain.model

/**
 * Краткое описание: чистые бизнес-модели предметной области (DOMAIN).
 * Не зависят от DTO, UI и сетевой библиотеки.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */

/** Пользователь приложения. */
data class User(
    val id: Int,
    val email: String
)

/** Игра, созданная пользователем на экране Schedule Game. */
data class Game(
    val id: Int,
    val ownerEmail: String,
    val date: String,
    val time: String
)
