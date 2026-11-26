package com.example.imech.presentation.screens.document

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import java.io.File

/**
 * Краткое описание: экран ознакомления с документами (модуль Г).
 * Если в assets есть PDF-версия документа, она копируется в кеш
 * и открывается внешним просмотрщиком; иначе текст показывается
 * прямо на экране.
 *
 * Дата создания: 26-11-2026
 * Автор создания: участник 1
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentScreen(
    docId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val title = if (docId == "privacy_policy") {
        "Политика конфиденциальности"
    } else {
        "Пользовательское соглашение"
    }

    // Попытка открыть PDF-версию документа внешним просмотрщиком
    runCatching {
        val file = File(context.cacheDir, "documents/$docId.pdf")
        if (file.exists()) {
            val uri = FileProvider.getUriForFile(context, "com.example.imech.fileprovider", file)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
        }
    }

    // Текстовая версия документа из assets
    val text = remember(docId) {
        runCatching {
            context.assets.open("$docId.txt").bufferedReader().readText()
        }.getOrDefault("Документ недоступен")
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = text)
        }
    }
}
