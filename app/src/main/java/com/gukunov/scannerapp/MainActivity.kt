package com.gukunov.scannerapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.gukunov.scannerapp.navigation.AppNavHost
import com.gukunov.scannerapp.ui.theme.ScannerAppTheme

class MainActivity : ComponentActivity() {

    /** Растёт при каждом открытии с виджета, когда Activity уже запущена. */
    private var scanRequest by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScannerAppTheme {
                AppNavHost(scanRequest = scanRequest)
            }
        }
    }

    // launchMode="singleTop": повторное открытие с виджета приходит сюда, а не в новую Activity.
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.getBooleanExtra(EXTRA_START_SCAN, false)) scanRequest++
    }

    companion object {
        const val EXTRA_START_SCAN = "com.gukunov.scannerapp.extra.START_SCAN"
    }
}
