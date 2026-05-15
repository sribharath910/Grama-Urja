package com.gramaUrja.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gramaUrja.ui.theme.Green50
import com.gramaUrja.ui.theme.Green800
import com.gramaUrja.viewmodel.MainViewModel

@Composable
fun SettingsScreen(viewModel: MainViewModel, onChangeZone: () -> Unit) {
    val darkMode by viewModel.darkMode.collectAsState()
    val notifications by viewModel.notificationsEnabled.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Spacer(Modifier.height(52.dp))

            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Settings, null, tint = Green800, modifier = Modifier.size(26.dp))
                Spacer(Modifier.width(10.dp))
                Text("Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }

            // Preferences Group
            SettingsGroup(title = "PREFERENCES") {
                SettingsToggleItem(
                    icon = Icons.Filled.DarkMode,
                    label = "Dark Mode",
                    description = "Switch to dark theme for low-light use",
                    value = darkMode,
                    onToggle = { viewModel.setDarkMode(it) }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                SettingsToggleItem(
                    icon = Icons.Filled.Notifications,
                    label = "Notifications",
                    description = "Receive power status change alerts",
                    value = notifications,
                    onToggle = { viewModel.setNotificationsEnabled(it) },
                    isLast = true
                )
            }

            // Zone Group
            SettingsGroup(title = "ZONE") {
                SettingsActionItem(
                    icon = Icons.Filled.LocationOn,
                    label = "Change Zone",
                    description = "Select a different village or transformer zone",
                    onClick = onChangeZone,
                    isLast = true
                )
            }

            // App Info Group
            SettingsGroup(title = "APP INFORMATION") {
                SettingsInfoItem(icon = Icons.Filled.Info, label = "Version", value = "1.0.0")
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                SettingsInfoItem(icon = Icons.Filled.PhoneAndroid, label = "Platform", value = "Android")
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                SettingsInfoItem(icon = Icons.Filled.Star, label = "Build", value = "Prototype", isLast = true)
            }

            // Future Integrations Group
            SettingsGroup(title = "FUTURE INTEGRATIONS") {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Green50)
                        .border(1.dp, Green800.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Firebase icon placeholder
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(Color(0xFFFFF3E0), RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🔥", fontSize = 16.sp)
                            }
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text("Firebase Integration", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
                                Text("Real-time sync & push notifications", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE65100).copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("Soon", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFE65100))
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    listOf(
                        "Cloud-synced power status across devices",
                        "Real-time alerts when power changes",
                        "Historical data and analytics",
                        "Multi-village monitoring"
                    ).forEach { feature ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant, RoundedCornerShape(3.dp))
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(feature, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 17.sp)
                        }
                    }
                }
            }

            // Footer
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Grama-Urja", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Community Powered Smart Irrigation", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingsGroup(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsToggleItem(
    icon: ImageVector,
    label: String,
    description: String,
    value: Boolean,
    onToggle: (Boolean) -> Unit,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(Green50, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Green800, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
            Text(description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp)
        }
        Switch(
            checked = value,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Green800,
                checkedTrackColor = Green800.copy(alpha = 0.4f),
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

@Composable
private fun SettingsActionItem(
    icon: ImageVector,
    label: String,
    description: String,
    onClick: () -> Unit,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(Green50, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Green800, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
            Text(description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp)
        }
        Icon(Icons.Filled.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun SettingsInfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(Green50, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Green800, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
        Text(value, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
