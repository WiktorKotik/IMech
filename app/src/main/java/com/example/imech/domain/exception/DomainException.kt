package com.example.imech.domain.exception

/**
 * Краткое описание: бизнес-ошибки домена (DOMAIN).
 * Сообщения отображаются на экранах через уведомление (модуль В).
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
sealed class DomainException(message: String) : Exception(message) {

    /** Регистрация: такой email уже занят. */
    class EmailAlreadyTaken : DomainException("Пользователь с таким email уже зарегистрирован")

    /** Авторизация: пользователь не найден или пароль неверный. */
    class InvalidCredentials : DomainException("Неверный email или пароль")

    /** Сервер вернул ошибку или нет соединения с интернетом. */
    class ServerError(message: String) : DomainException(message)
}
