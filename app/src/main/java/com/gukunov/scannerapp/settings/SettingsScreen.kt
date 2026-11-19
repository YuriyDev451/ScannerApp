package com.gukunov.scannerapp.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gukunov.scannerapp.R
import com.gukunov.scannerapp.ui.components.BackTopBar
import com.gukunov.scannerapp.ui.theme.ScannerAppTheme

/**
 * Настройки. Переключатели пока моковые — живут только в состоянии экрана
 * и никуда не сохраняются.
 */
@Composable
fun SettingsScreen(onBack: () -> Unit, onOpenHistory: () -> Unit, onOpenAbout: () -> Unit) {
    var vibration by rememberSaveable { mutableStateOf(true) }
    var sound by rememberSaveable { mutableStateOf(false) }
    var autoCopy by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = { BackTopBar(stringResource(R.string.settings_title), onBack) }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SectionHeader(stringResource(R.string.settings_section_general))
            SwitchItem(stringResource(R.string.settings_vibration), vibration) { vibration = it }
            SwitchItem(stringResource(R.string.settings_sound), sound) { sound = it }
            SwitchItem(stringResource(R.string.settings_auto_copy), autoCopy) { autoCopy = it }

            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            SectionHeader(stringResource(R.string.settings_section_other))
            NavItem(stringResource(R.string.history_title), R.drawable.ic_history, onOpenHistory)
            NavItem(stringResource(R.string.about_title), R.drawable.ic_qr_scan, onOpenAbout)
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SwitchItem(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    ListItem(
        headlineContent = { Text(title) },
        trailingContent = { Switch(checked = checked, onCheckedChange = null) },
        modifier = Modifier.clickable { onCheckedChange(!checked) }
    )
}

@Composable
private fun NavItem(title: String, iconRes: Int, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(title) },
        leadingContent = { Icon(painterResource(iconRes), contentDescription = null) },
        trailingContent = {
            Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null)
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    ScannerAppTheme { SettingsScreen(onBack = {}, onOpenHistory = {}, onOpenAbout = {}) }
}
