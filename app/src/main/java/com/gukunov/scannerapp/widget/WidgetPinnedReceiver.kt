package com.gukunov.scannerapp.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.gukunov.scannerapp.R

/**
 * Получает successCallback от лаунчера — виджет действительно добавлен.
 *
 * Зарегистрирован в манифесте с exported="false": снаружи его вызвать нельзя,
 * но PendingIntent срабатывает от имени нашего приложения, так что лаунчеру можно.
 *
 * Кнопку «Добавить виджет» прятать здесь не нужно — AddWidgetButton сама
 * перепроверит состояние, когда приложение вернётся на передний план.
 */
class WidgetPinnedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // В intent лаунчер кладёт id нового виджета:
        // intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ...).
        // Пригодится, если виджет когда-нибудь станет настраиваемым.
        Toast.makeText(context, R.string.widget_added, Toast.LENGTH_SHORT).show()
    }
}
