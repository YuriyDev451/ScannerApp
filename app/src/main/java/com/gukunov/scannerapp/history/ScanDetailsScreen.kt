package com.gukunov.scannerapp.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gukunov.scannerapp.R
import com.gukunov.scannerapp.mock.MockScans
import com.gukunov.scannerapp.ui.components.BackTopBar
import com.gukunov.scannerapp.ui.theme.ScannerAppTheme

/**
 * Детали одного скана + «Похожие сканы» того же типа.
 * Клик по похожему открывает его детали поверх текущих (стек растёт).
 */
@Composable
fun ScanDetailsScreen(scanId: Int, onBack: () -> Unit, onScanClick: (Int) -> Unit) {
    val scan = MockScans.byId(scanId)

    Scaffold(
        topBar = { BackTopBar(stringResource(R.string.scan_details_title, scanId), onBack) }
    ) { innerPadding ->
        if (scan == null) return@Scaffold
        val similar = MockScans.similarTo(scan)

        LazyColumn(contentPadding = innerPadding) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        SelectionContainer {
                            Text(scan.content, style = MaterialTheme.typography.bodyLarge)
                        }
                        Spacer(Modifier.height(16.dp))
                        InfoRow(stringResource(R.string.scan_details_type), scan.type.label)
                        InfoRow(stringResource(R.string.scan_details_date), scan.date)
                        InfoRow(stringResource(R.string.scan_details_format), "QR Code")
                    }
                }
            }
            if (similar.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.scan_details_similar),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                items(similar, key = { it.id }) { item ->
                    ScanListItem(scan = item, onClick = { onScanClick(item.id) })
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun ScanDetailsScreenPreview() {
    ScannerAppTheme { ScanDetailsScreen(scanId = 1, onBack = {}, onScanClick = {}) }
}
