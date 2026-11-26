package com.example.imech.presentation.screens.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.imech.presentation.components.ErrorBanner
import com.example.imech.presentation.viewmodels.ScheduleViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Краткое описание: экран Schedule Game (модуль Б, п.6).
 * Дата и время выбираются стандартными системными элементами
 * (DatePicker и TimePicker Material 3). Кнопка Publish сохраняет
 * игру на сервере и открывает экран Success.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleGameScreen(
    viewModel: ScheduleViewModel,
    onPublishSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Schedule Game",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Дата: ${viewModel.selectedDate ?: "не выбрана"}")
            }
            OutlinedButton(
                onClick = { showTimePicker = true },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            ) {
                Text("Время: ${viewModel.selectedTime ?: "не выбрано"}")
            }

            Button(
                onClick = { viewModel.publishGame(onSuccess = onPublishSuccess) },
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
            ) { Text("Publish") }
        }

        ErrorBanner(
            message = viewModel.serverError,
            onDismiss = viewModel::dismissError,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

    // Стандартный системный выбор даты
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                        viewModel.onDateSelected("%02d-%02d-%04d".format(date.dayOfMonth, date.monthValue, date.year))
                    }
                    showDatePicker = false
                }) { Text("Выбрать") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            },
            text = { DatePicker(state = datePickerState) }
        )
    }

    // Стандартный системный выбор времени
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(is24Hour = true)
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onTimeSelected(
                        "%02d:%02d".format(timePickerState.hour, timePickerState.minute)
                    )
                    showTimePicker = false
                }) { Text("Выбрать") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Отмена") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }
}
