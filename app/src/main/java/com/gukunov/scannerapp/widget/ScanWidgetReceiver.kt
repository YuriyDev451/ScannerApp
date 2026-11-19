package com.gukunov.scannerapp.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * Точка входа системы в наш виджет.
 *
 * Под капотом это обычный AppWidgetProvider (а значит BroadcastReceiver),
 * зарегистрированный в AndroidManifest.xml с action APPWIDGET_UPDATE и
 * ссылкой на res/xml/scan_widget_info.xml. Именно по этой регистрации
 * лаунчер узнаёт, что у приложения есть виджет, и показывает его в списке.
 *
 * Классические колбэки AppWidgetProvider (onEnabled / onDisabled / onUpdate /
 * onDeleted) тут тоже есть — GlanceAppWidgetReceiver реализует их сам и
 * в onUpdate вызывает [ScanWidget.provideGlance]. Переопределять их можно,
 * но не обязательно. Нам не нужно: проверять «стоит ли виджет на экране»
 * надёжнее через GlanceAppWidgetManager.getGlanceIds (см. WidgetPinning.kt),
 * чем хранить свой флаг из onEnabled/onDisabled.
 */
class ScanWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ScanWidget()
}
