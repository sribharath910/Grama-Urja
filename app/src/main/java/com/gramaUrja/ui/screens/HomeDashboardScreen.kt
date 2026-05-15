package com.gramaUrja.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gramaUrja.data.model.ActivityItem
import com.gramaUrja.data.model.PowerStatus
import com.gramaUrja.ui.theme.Green50
import com.gramaUrja.ui.theme.Green800
import com.gramaUrja.ui.theme.Red50
import com.gramaUrja.ui.theme.Red800
import com.gramaUrja.utils.TimeUtils
import com.gramaUrja.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeDashboardScreen(viewModel: MainViewModel, onChangeZone: () -> Unit) {
    val zones by viewModel.zones.collectAsState()
    val selectedZoneId by viewModel.selectedZoneId.collectAsState()
    val activityLog by viewModel.activityLog.collectAsState()
    var bannerVisible by remember { mutableStateOf(true) }

    val selectedZone = zones.find { it.id == selectedZoneId }
    val isOn = selectedZone?.powerStatus == PowerStatus.ON

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (selectedZone == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.LocationOn, null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(12.dp))
                    Text("No zone selected", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            return@Surface
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(Modifier.height(52.dp)) }

            // Notification Banner
            if (bannerVisible) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Green50)
                            .border(1.dp, Green800.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Notifications, null, tint = Green800, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Power monitoring active. Tap ON/OFF to update zone status.",
                            fontSize = 12.sp,
                            color = Green800,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { bannerVisible = false },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Filled.Close, null, modifier = Modifier.size(16.dp), tint = Color(0xFF5D7A5F))
                        }
                    }
                }
            }

            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Power Status", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            selectedZone.name,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                            .clickable { onChangeZone() }
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.LocationOn, null, tint = Green800, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(5.dp))
                        Text("Change", fontSize = 12.sp, color = Green800, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // Power Status Card
            item {
                val cardBg by animateColorAsState(
                    targetValue = if (isOn) Green50 else Red50,
                    animationSpec = tween(400),
                    label = "card_bg"
                )
                val borderCol by animateColorAsState(
                    targetValue = if (isOn) Green800 else Red800,
                    animationSpec = tween(400),
                    label = "border"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, borderCol, RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(if (isOn) Green800 else Red800, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isOn) Icons.Filled.ElectricBolt else Icons.Filled.PowerOff,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Current Status",
                                    fontSize = 12.sp,
                                    color = if (isOn) Green800 else Red800
                                )
                                Text(
                                    "Power ${selectedZone.powerStatus.name}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isOn) Green800 else Red800
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(if (isOn) Green800 else Red800, CircleShape)
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Timeline, null, modifier = Modifier.size(13.dp), tint = if (isOn) Green800 else Red800)
                            Spacer(Modifier.width(5.dp))
                            Text(
                                TimeUtils.getRelativeTime(selectedZone.lastUpdated),
                                fontSize = 12.sp,
                                color = if (isOn) Green800 else Red800
                            )
                        }
                    }
                }
            }

            // Power Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PowerButton(
                        label = "Power ON",
                        active = isOn,
                        activeColor = Green800,
                        activeBg = Green50,
                        borderColor = if (isOn) Green800 else MaterialTheme.colorScheme.outline,
                        onClick = { viewModel.togglePower(selectedZone.id, PowerStatus.ON) },
                        modifier = Modifier.weight(1f)
                    )
                    PowerButton(
                        label = "Power OFF",
                        active = !isOn,
                        activeColor = Red800,
                        activeBg = Red50,
                        borderColor = if (!isOn) Red800 else MaterialTheme.colorScheme.outline,
                        onClick = { viewModel.togglePower(selectedZone.id, PowerStatus.OFF) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Zone Details Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Info, null, tint = Green800, modifier = Modifier.size(17.dp))
                            Spacer(Modifier.width(7.dp))
                            Text("Zone Details", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                        Spacer(Modifier.height(12.dp))
                        ZoneDetailRow("Zone Type", if (selectedZone.type.name == "VILLAGE") "Village" else "Transformer")
                        ZoneDetailRow("Zone ID", selectedZone.id)
                        ZoneDetailRow("Last Update", TimeUtils.formatTime(selectedZone.lastUpdated))
                        ZoneDetailRow("Date", SimpleDateFormat("d MMM", Locale.getDefault()).format(Date()))
                    }
                }
            }

            // Recent Activity
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Timeline, null, tint = Green800, modifier = Modifier.size(17.dp))
                            Spacer(Modifier.width(7.dp))
                            Text("Recent Activity", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                        activityLog.take(4).forEachIndexed { index, item ->
                            if (index > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                            ActivityRowItem(item = item)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun PowerButton(
    label: String,
    active: Boolean,
    activeColor: Color,
    activeBg: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (active) 1.02f else 1f,
        animationSpec = tween(200),
        label = "btn_scale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(14.dp))
            .background(if (active) activeBg else MaterialTheme.colorScheme.surface)
            .border(
                width = if (active) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = if (label.contains("ON")) Icons.Filled.ElectricBolt else Icons.Filled.PowerOff,
                contentDescription = label,
                tint = if (active) activeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(26.dp)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                color = if (active) activeColor else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        // Active indicator bar at bottom
        if (active) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(activeColor)
            )
        }
    }
}

@Composable
private fun ZoneDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun ActivityRowItem(item: ActivityItem) {
    val isOn = item.action == PowerStatus.ON
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(if (isOn) Green800 else Red800, CircleShape)
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.zoneName, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
            Text(TimeUtils.getRelativeTime(item.timestamp), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Box(
            modifier = Modifier
                .background(if (isOn) Green50 else Red50, RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                item.action.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isOn) Green800 else Red800
            )
        }
    }
}
