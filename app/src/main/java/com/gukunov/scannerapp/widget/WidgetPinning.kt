package com.gukunov.scannerapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidgetManager

/*
 * «Pinning» — добавление виджета на рабочий стол по запросу из приложения
 * (а не через долгое нажатие на рабочем столе → «Виджеты»).
 *
 * Схема:
 *   приложение ──requestPinGlanceAppWidget──► лаунчер показывает диалог
 *   пользователь жмёт «Добавить» ──► лаунчер создаёт виджет
 *   лаунчер ──successCallback (PendingIntent)──► WidgetPinnedReceiver
 */

/**
 * Можно ли сейчас предложить добавить виджет.
 * suspend — потому что getGlanceIds читает данные Glance не на главном потоке.
 */
suspend fun canPinScanWidget(context: Context): Boolean {
    // 1. API для pinning появилось в Android 8.0 (API 26). У нас minSdk = 24.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return false

    // 2. Решает лаунчер: он может вообще не поддерживать pinning
    //    (некоторые оболочки, сторонние лаунчеры).
    if (!AppWidgetManager.getInstance(context).isRequestPinAppWidgetSupported) return false

    // 3. Виджет уже стоит на экране — кнопку не показываем.
    //    getGlanceIds возвращает id всех размещённых копий ScanWidget.
    //    Обратите внимание: сюда передаётся класс виджета, а не receiver.
    return GlanceAppWidgetManager(context).getGlanceIds(ScanWidget::class.java).isEmpty()
}

/**
 * Показывает системный диалог «Добавить виджет на главный экран».
 *
 * Требование системы: вызывать из Activity на переднем плане,
 * поэтому сюда передаётся контекст Activity (LocalContext в Compose).
 */
suspend fun requestPinScanWidget(context: Context) {
    // PendingIntent, который лаунчер «выстрелит», если виджет действительно добавлен.
    // При отказе пользователя ничего не придёт — это ограничение API.
    val successCallback = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context, WidgetPinnedReceiver::class.java),
        // Лаунчер дописывает в Intent EXTRA_APPWIDGET_ID (id нового виджета),
        // значит PendingIntent должен разрешать изменения — FLAG_MUTABLE.
        // Флаг появился в Android 12 (API 31); до этого все были mutable по умолчанию.
        PendingIntent.FLAG_UPDATE_CURRENT or mutableFlag()
    )

    // Возвращает true, если запрос ушёл лаунчеру (это ещё не значит, что виджет добавлен).
    GlanceAppWidgetManager(context).requestPinGlanceAppWidget(
        receiver = ScanWidgetReceiver::class.java,
        // Glance отрисует настоящий виджет как превью в диалоге.
        preview = ScanWidget(),
        previewSize = DpSize(110.dp, 110.dp),
        successCallback = successCallback
    )
}

private fun mutableFlag(): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
