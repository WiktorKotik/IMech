package com.example.imech.data.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Краткое описание: локальное хранилище (DATA, Data Source) на SharedPreferences.
 * Отвечает за флаг показа онбординга, сохранение последнего экрана (модуль Г)
 * и локальную «базу» зарегистрированных пользователей для имитации сервера.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
class AppPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("imech_prefs", Context.MODE_PRIVATE)

    /** Пройден ли онбординг: показывается только при первом запуске после установки. */
    fun isOnboardingDone(): Boolean = prefs.getBoolean(KEY_ONBOARDING, false)

    fun setOnboardingDone() {
        prefs.edit().putBoolean(KEY_ONBOARDING, true).apply()
    }

    /** Сохранение последнего открытого экрана до перезапуска приложения (модуль Г). */
    fun saveLastScreen(route: String) {
        prefs.edit().putString(KEY_LAST_SCREEN, route).apply()
    }

    fun loadLastScreen(): String? = prefs.getString(KEY_LAST_SCREEN, null)

    /** Сохранение пользователя в локальной «базе» (имитация таблицы users на сервере). */
    fun registerUser(email: String, password: String) {
        prefs.edit().putString(userKey(email), "$email:$password").apply()
    }

    /**
     * Поиск пользователя по email.
     * @return пара «email и пароль» или null, если пользователя нет
     */
    fun findUser(email: String): Pair<String, String>? {
        val saved = prefs.getString(userKey(email), null) ?: return null
        val parts = saved.split(":", limit = 2)
        return parts[0] to parts[1]
    }

    private fun userKey(email: String) = "user_${email.trim().lowercase()}"

    private companion object {
        const val KEY_ONBOARDING = "onboarding_done"
        const val KEY_LAST_SCREEN = "last_screen"
    }
}
