package com.gukunov.scannerapp.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.gukunov.scannerapp.R
import com.gukunov.scannerapp.mock.MockScan
import com.gukunov.scannerapp.mock.MockScans
import com.gukunov.scannerapp.ui.components.BackTopBar
import com.gukunov.scannerapp.ui.theme.ScannerAppTheme

/** Список прошлых сканирований (моковые данные). Клик открывает детали. */
@Composable
fun HistoryScreen(onBack: () -> Unit, onScanClick: (Int) -> Unit) {
    Scaffold(
        topBar = { BackTopBar(stringResource(R.string.history_title), onBack) }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(MockScans.all, key = { it.id }) { scan ->
                ScanListItem(scan = scan, onClick = { onScanClick(scan.id) })
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun ScanListItem(scan: MockScan, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ListItem(
        headlineContent = {
            Text(scan.content, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        supportingContent = { Text("${scan.type.label} · ${scan.date}") },
        leadingContent = {
            Icon(painterResource(R.drawable.ic_qr_scan), contentDescription = null)
        },
        trailingContent = {
            Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null)
        },
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    ScannerAppTheme { HistoryScreen(onBack = {}, onScanClick = {}) }
}
