package com.gukunov.scannerapp.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent

/**
 * Описание виджета в терминах Glance — аналог Activity/экрана для рабочего стола.
 *
 * Виджет рисует не наше приложение, а лаунчер. Поэтому Glance превращает
 * Compose-подобный код из [ScanWidgetContent] в RemoteViews — «инструкцию»,
 * которую лаунчер умеет отрисовать у себя в процессе.
 *
 * [provideGlance] вызывается системой (через [ScanWidgetReceiver]) каждый раз,
 * когда виджет надо отрисовать: при добавлении, после перезагрузки, при ресайзе.
 * Один экземпляр класса обслуживает все копии виджета на экране — каждую
 * различает [GlanceId].
 */
class ScanWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Здесь можно было бы загрузить данные (БД, DataStore) до provideContent.
        // Нашему виджету данные не нужны — он всегда одинаковый.
        provideContent {
            // GlanceTheme — цвета для виджета; на Android 12+ динамические (от обоев).
            GlanceTheme { ScanWidgetContent() }
        }
    }
}
