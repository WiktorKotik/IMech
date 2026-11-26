package com.example.imech.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imech.domain.usecase.CreateGameUseCase
import com.example.imech.domain.usecase.GetGamesUseCase
import com.example.imech.domain.model.Game
import kotlinx.coroutines.launch

/**
 * Краткое описание: ViewModel экранов Schedule Game и Landing (PRESENTATION).
 * Хранит выбранную дату и время, создаёт игру на сервере
 * и загружает список игр пользователя.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
class ScheduleViewModel(
    private val createGameUseCase: CreateGameUseCase,
    private val getGamesUseCase: GetGamesUseCase
) : ViewModel() {

    var selectedDate by mutableStateOf<String?>(null)
        private set
    var selectedTime by mutableStateOf<String?>(null)
        private set
    var serverError by mutableStateOf<String?>(null)
        private set
    var games by mutableStateOf<List<Game>>(emptyList())
        private set

    fun onDateSelected(date: String) {
        selectedDate = date
    }

    fun onTimeSelected(time: String) {
        selectedTime = time
    }

    fun dismissError() {
        serverError = null
    }

    /**
     * Сохранение созданной игры на сервере (модуль Б, п.6).
     * @param onSuccess вызывается после успешной публикации
     */
    fun publishGame(onSuccess: () -> Unit) {
        val date = selectedDate ?: run {
            serverError = "Выберите дату игры"
            return
        }
        val time = selectedTime ?: run {
            serverError = "Выберите время игры"
            return
        }
        viewModelScope.launch {
            createGameUseCase(date, time).fold(
                onSuccess = { onSuccess() },
                onFailure = { error ->
                    serverError = error.message ?: "Ошибка сервера"
                }
            )
        }
    }

    /** Загрузка списка игр для экрана Landing. */
    fun loadGames() {
        viewModelScope.launch {
            getGamesUseCase().fold(
                onSuccess = { games = it },
                onFailure = { error ->
                    serverError = error.message ?: "Ошибка сервера"
                }
            )
        }
    }
}
