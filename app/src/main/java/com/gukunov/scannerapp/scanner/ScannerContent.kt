package com.gukunov.scannerapp.scanner

import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.gukunov.scannerapp.R
import com.gukunov.scannerapp.widget.AddWidgetButton

/** Экран с камерой: превью, распознавание QR и карточка результата. */
@Composable
fun ScannerContent(scanRequest: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val haptic = LocalHapticFeedback.current

    // Распознанный текст; null — идёт сканирование.
    var result by rememberSaveable { mutableStateOf<String?>(null) }

    // Открыли с виджета, когда приложение уже было запущено, — начинаем заново.
    LaunchedEffect(scanRequest) {
        if (scanRequest > 0) result = null
    }

    val scanner = remember {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        )
    }
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
        }
    }

    // Камера живёт по жизненному циклу экрана: уходим в фон — камера освобождается.
    DisposableEffect(lifecycleOwner) {
        cameraController.bindToLifecycle(lifecycleOwner)
        onDispose { cameraController.unbind() }
    }
    DisposableEffect(Unit) {
        onDispose { scanner.close() }
    }

    // Анализ кадров работает только пока результата нет.
    DisposableEffect(result) {
        if (result == null) {
            val executor = ContextCompat.getMainExecutor(context)
            cameraController.setImageAnalysisAnalyzer(
                executor,
                MlKitAnalyzer(
                    listOf(scanner),
                    ImageAnalysis.COORDINATE_SYSTEM_ORIGINAL,
                    executor
                ) { analyzerResult ->
                    if (result != null) return@MlKitAnalyzer
                    val barcode = analyzerResult.getValue(scanner)?.firstOrNull()
                        ?: return@MlKitAnalyzer
                    val value = barcode.rawValue ?: barcode.displayValue ?: return@MlKitAnalyzer
                    result = value
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            )
        }
        onDispose { cameraController.clearImageAnalysisAnalyzer() }
    }

    Box(modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    controller = cameraController
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        ViewfinderOverlay(Modifier.fillMaxSize())

        // Сама решает, показываться ли (см. widget/AddWidgetButton.kt).
        AddWidgetButton(
            Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(12.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            val text = result
            if (text == null) {
                Text(
                    text = stringResource(R.string.scan_hint),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                )
            } else {
                ResultCard(text = text, onScanAgain = { result = null })
            }
        }
    }
}
