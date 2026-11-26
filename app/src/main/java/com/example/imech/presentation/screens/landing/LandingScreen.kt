package com.example.imech.presentation.screens.landing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.imech.presentation.components.ErrorBanner
import com.example.imech.presentation.viewmodels.ScheduleViewModel
import kotlinx.coroutines.launch

/**
 * Краткое описание: главный экран Landing (модуль Б, п.5).
 * Кнопка-«гамбургер» открывает меню слева с анимацией; меню закрывается
 * свайпом влево или крестиком. Центральная кнопка нижнего меню
 * открывает экран Schedule Game.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    viewModel: ScheduleViewModel,
    onOpenSchedule: () -> Unit,
    onOpenDocument: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadGames()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Меню", style = MaterialTheme.typography.titleMedium)
                        // Закрытие меню крестиком (задание 5)
                        IconButton(onClick = { scope.launch { drawerState.close() } }) {
                            Icon(Icons.Filled.Close, contentDescription = "Закрыть меню")
                        }
                    }
                    Button(
                        onClick = {
                            scope.launch { drawerState.close() }
                            onOpenDocument("privacy_policy")
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) { Text("Политика конфиденциальности") }
                    Button(
                        onClick = {
                            scope.launch { drawerState.close() }
                            onOpenDocument("terms_of_use")
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) { Text("Пользовательское соглашение") }
                    Button(
                        onClick = {
                            scope.launch { drawerState.close() }
                            onLogout()
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) { Text("Выйти") }
                }
            }
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("ИМЭЧ") },
                    navigationIcon = {
                        // «Гамбургер» открывает меню с анимацией
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Открыть меню")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = true,
                        onClick = {},
                        icon = { androidx.compose.material3.Icon(Icons.Filled.List, contentDescription = null) },
                        label = { Text("Игры") }
                    )
                    // Центральная кнопка нижнего меню (задание 5)
                    NavigationBarItem(
                        selected = false,
                        onClick = onOpenSchedule,
                        icon = {
                            androidx.compose.material3.Icon(Icons.Filled.Add, contentDescription = "Новая игра")
                        },
                        label = { Text("Создать") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = { androidx.compose.material3.Icon(Icons.Filled.Person, contentDescription = null) },
                        label = { Text("Профиль") }
                    )
                }
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                if (viewModel.games.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Пока нет ни одной игры", style = MaterialTheme.typography.titleMedium)
                        Button(
                            onClick = onOpenSchedule,
                            modifier = Modifier.padding(top = 16.dp)
                        ) { Text("Создать игру") }
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        items(viewModel.games) { game ->
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                Text("Игра №${game.id}", style = MaterialTheme.typography.titleMedium)
                                Text("${game.date} в ${game.time}", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }

                ErrorBanner(
                    message = viewModel.serverError,
                    onDismiss = viewModel::dismissError,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}
