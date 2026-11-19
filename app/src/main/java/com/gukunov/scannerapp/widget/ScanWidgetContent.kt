package com.gukunov.scannerapp.widget

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.gukunov.scannerapp.MainActivity
import com.gukunov.scannerapp.R

/**
 * UI виджета: иконка QR + «Сканировать».
 *
 * ВАЖНО: это Glance, а не обычный Compose. Аннотация @Composable та же,
 * но компоненты другие — Column, Text, Image и модификаторы импортируются
 * из androidx.glance.*, и вместо Modifier используется GlanceModifier.
 * Обычные androidx.compose.ui/material3 компоненты здесь не работают.
 */
@Composable
fun ScanWidgetContent() {
    // LocalContext тоже из Glance (androidx.glance.LocalContext).
    val context = LocalContext.current

    // Экстра-флаг, по которому MainActivity поймёт, что её открыли с виджета.
    val openScanner = Intent(context, MainActivity::class.java)
        .putExtra(MainActivity.EXTRA_START_SCAN, true)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            // Помечает этот элемент как фон виджета — лаунчер использует это
            // для анимаций и закругления углов.
            .appWidgetBackground()
            .background(GlanceTheme.colors.widgetBackground)
            // Работает на Android 12+, на старых версиях просто игнорируется.
            .cornerRadius(16.dp)
            // Лямбды-onClick тут нет: виджет живёт в чужом процессе, поэтому
            // клик описывается как «действие» — Glance превращает его в PendingIntent.
            .clickable(actionStartActivity(openScanner))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            provider = ImageProvider(R.drawable.ic_qr_scan),
            contentDescription = null,
            colorFilter = ColorFilter.tint(GlanceTheme.colors.primary),
            modifier = GlanceModifier.size(32.dp)
        )
        Spacer(GlanceModifier.height(4.dp))
        Text(
            text = context.getString(R.string.widget_scan),
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1
        )
    }
}
