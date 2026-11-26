package com.example.imech.data.mapper

import com.example.imech.data.remote.GameDto
import com.example.imech.data.remote.UserDto
import com.example.imech.domain.model.Game
import com.example.imech.domain.model.User

/**
 * Краткое описание: мапперы DTO в Entity (DATA).
 * Преобразуют модели сетевого слоя в бизнес-модели домена.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */

/** Преобразование пользователя из DTO в Entity. */
fun UserDto.toDomain(): User = User(id = id, email = email)

/** Преобразование игры из DTO в Entity. */
fun GameDto.toDomain(): Game = Game(
    id = id,
    ownerEmail = ownerEmail,
    date = date,
    time = time
)
