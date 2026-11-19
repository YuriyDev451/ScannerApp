package com.gukunov.scannerapp.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.gukunov.scannerapp.history.HistoryScreen
import com.gukunov.scannerapp.history.ScanDetailsScreen
import com.gukunov.scannerapp.scanner.QrScannerScreen
import com.gukunov.scannerapp.settings.AboutScreen
import com.gukunov.scannerapp.settings.SettingsScreen

private val BackStackSaver = listSaver<SnapshotStateList<Screen>, String>(
    save = { stack -> stack.map { it.route } },
    restore = { routes -> mutableStateListOf(*routes.map(Screen::fromRoute).toTypedArray()) }
)

/**
 * Простая навигация без сторонних библиотек: стек экранов + системная «Назад».
 * Стек переживает поворот экрана, а состояние каждого экрана (rememberSaveable)
 * сохраняется, пока экран лежит в стеке.
 */
@Composable
fun AppNavHost(scanRequest: Int) {
    val backStack = rememberSaveable(saver = BackStackSaver) {
        mutableStateListOf<Screen>(Screen.Scanner)
    }
    val stateHolder = rememberSaveableStateHolder()

    fun navigate(screen: Screen) {
        backStack.add(screen)
    }

    fun pop() {
        if (backStack.size > 1) {
            val removed = backStack.removeAt(backStack.lastIndex)
            // Если такого экрана больше нет в стеке — забываем его состояние.
            if (backStack.none { it.route == removed.route }) stateHolder.removeState(removed.route)
        }
    }

    // Открыли с виджета — возвращаемся к сканеру, откуда бы ни были.
    LaunchedEffect(scanRequest) {
        if (scanRequest > 0) while (backStack.size > 1) pop()
    }

    BackHandler(enabled = backStack.size > 1) { pop() }

    val current = backStack.last()
    stateHolder.SaveableStateProvider(key = current.route) {
        when (current) {
            Screen.Scanner -> QrScannerScreen(
                scanRequest = scanRequest,
                onOpenHistory = { navigate(Screen.History) },
                onOpenSettings = { navigate(Screen.Settings) },
            )

            Screen.History -> HistoryScreen(
                onBack = ::pop,
                onScanClick = { id -> navigate(Screen.ScanDetails(id)) },
            )

            is Screen.ScanDetails -> ScanDetailsScreen(
                scanId = current.scanId,
                onBack = ::pop,
                onScanClick = { id -> navigate(Screen.ScanDetails(id)) },
            )

            Screen.Settings -> SettingsScreen(
                onBack = ::pop,
                onOpenHistory = { navigate(Screen.History) },
                onOpenAbout = { navigate(Screen.About) },
            )

            Screen.About -> AboutScreen(onBack = ::pop)
        }
    }
}
