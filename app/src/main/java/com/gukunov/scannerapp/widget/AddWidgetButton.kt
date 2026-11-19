package com.gukunov.scannerapp.widget

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.gukunov.scannerapp.R
import kotlinx.coroutines.launch

/**
 * Кнопка «Добавить виджет на экран» внутри приложения (обычный Compose).
 * Если добавить виджет нельзя или он уже есть — не рисует ничего.
 */
@Composable
fun AddWidgetButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var canPin by remember { mutableStateOf(false) }

    // ON_RESUME срабатывает при открытии экрана И при возвращении на него.
    // Системный диалог добавления виджета — чужая Activity поверх нашей,
    // поэтому после его закрытия мы снова получаем ON_RESUME и перепроверяем:
    // виджет добавили → кнопка исчезает; удалили с рабочего стола → появляется.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        scope.launch { canPin = canPinScanWidget(context) }
    }

    if (!canPin) return

    IconButton(
        onClick = { scope.launch { requestPinScanWidget(context) } },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Black.copy(alpha = 0.4f),
            contentColor = Color.White
        ),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add_widget),
            contentDescription = stringResource(R.string.action_add_widget)
        )
    }
}
