package com.gramaUrja.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gramaUrja.ui.theme.Green50
import com.gramaUrja.ui.theme.Green800
import com.gramaUrja.viewmodel.MainViewModel

data class CropConfig(
    val name: String,
    val baseHours: Double,
    val waterIntensity: String,
    val description: String
)

val CROPS = listOf(
    CropConfig("Rice",       6.0,  "High",   "Paddy fields"),
    CropConfig("Wheat",      3.5,  "Medium", "Rabi crop"),
    CropConfig("Sugarcane",  8.0,  "High",   "Long-cycle crop"),
    CropConfig("Vegetables", 2.0,  "Low",    "Kitchen garden")
)

fun calculateDuration(crop: CropConfig, hp: Int): Pair<Int, Int> {
    val totalHours = (crop.baseHours * 3) / hp
    val hours = totalHours.toInt()
    val minutes = ((totalHours - hours) * 60).toInt()
    return Pair(hours, minutes)
}

@Composable
fun PumpTimerScreen(viewModel: MainViewModel) {
    var selectedCrop by remember { mutableStateOf(CROPS[0]) }
    var hp by remember { mutableIntStateOf(3) }
    var showResult by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(52.dp))

            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.WaterDrop, null, tint = Green800, modifier = Modifier.size(26.dp))
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("Pump Timer", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Text("Calculate optimal irrigation duration", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Crop Type Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Eco, null, tint = Green800, modifier = Modifier.size(17.dp))
                        Spacer(Modifier.width(7.dp))
                        Text("Crop Type", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                    Spacer(Modifier.height(12.dp))

                    // Crop Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CROPS.take(2).forEach { crop ->
                            CropPill(
                                crop = crop,
                                isSelected = selectedCrop.name == crop.name,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    selectedCrop = crop
                                    showResult = false
                                }
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CROPS.drop(2).forEach { crop ->
                            CropPill(
                                crop = crop,
                                isSelected = selectedCrop.name == crop.name,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    selectedCrop = crop
                                    showResult = false
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Green50)
                            .border(1.dp, Green800.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Water Intensity", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            selectedCrop.waterIntensity,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = when (selectedCrop.waterIntensity) {
                                "High" -> Color(0xFFC62828)
                                "Medium" -> Color(0xFFE65100)
                                else -> Green800
                            }
                        )
                    }
                }
            }

            // Pump HP Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Timer, null, tint = Green800, modifier = Modifier.size(17.dp))
                        Spacer(Modifier.width(7.dp))
                        Text("Pump Power", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                    Spacer(Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = { if (hp > 1) { hp--; showResult = false } },
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    if (hp > 1) Green50 else MaterialTheme.colorScheme.surfaceVariant,
                                    CircleShape
                                )
                                .border(
                                    2.dp,
                                    if (hp > 1) Green800 else MaterialTheme.colorScheme.outline,
                                    CircleShape
                                )
                        ) {
                            Icon(Icons.Filled.Remove, null, tint = if (hp > 1) Green800 else MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(100.dp)
                        ) {
                            Text(
                                text = "$hp",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text("HP", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        IconButton(
                            onClick = { if (hp < 10) { hp++; showResult = false } },
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    if (hp < 10) Green50 else MaterialTheme.colorScheme.surfaceVariant,
                                    CircleShape
                                )
                                .border(
                                    2.dp,
                                    if (hp < 10) Green800 else MaterialTheme.colorScheme.outline,
                                    CircleShape
                                )
                        ) {
                            Icon(Icons.Filled.Add, null, tint = if (hp < 10) Green800 else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    // HP Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        (1..10).forEach { i ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(if (i <= hp) Green800 else MaterialTheme.colorScheme.surfaceVariant)
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("Range: 1 – 10 HP", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Calculate Button
            Button(
                onClick = { showResult = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green800)
            ) {
                Icon(Icons.Filled.Calculate, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Calculate Duration", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

            // Result Card (animated)
            AnimatedVisibility(
                visible = showResult,
                enter = fadeIn() + expandVertically()
            ) {
                val (hours, minutes) = calculateDuration(selectedCrop, hp)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Green800),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Timer, null, tint = Color.White, modifier = Modifier.size(22.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Recommended Duration", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (hours > 0) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("$hours", fontSize = 44.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("hr${if (hours != 1) "s" else ""}", fontSize = 14.sp, color = Color.White.copy(alpha = 0.75f))
                                }
                                Spacer(Modifier.width(20.dp))
                            }
                            if (minutes > 0) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("$minutes", fontSize = 44.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("min${if (minutes != 1) "s" else ""}", fontSize = 14.sp, color = Color.White.copy(alpha = 0.75f))
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                        Spacer(Modifier.height(10.dp))
                        listOf("Crop" to selectedCrop.name, "Pump Power" to "$hp HP", "Water Need" to selectedCrop.waterIntensity).forEach { (label, value) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(label, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                                Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            }
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }

            // Tips Card
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
                        Icon(Icons.Filled.WaterDrop, null, tint = Color(0xFFE65100), modifier = Modifier.size(17.dp))
                        Spacer(Modifier.width(7.dp))
                        Text("Irrigation Tips", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                    listOf(
                        "Irrigate during early morning to reduce evaporation",
                        "Check soil moisture before starting the pump",
                        "Monitor water flow to detect pump issues early",
                        "Avoid irrigation during rain or high humidity"
                    ).forEachIndexed { i, tip ->
                        if (i > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 11.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(Green800, CircleShape)
                                    .padding(top = 6.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(tip, fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground, lineHeight = 18.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CropPill(
    crop: CropConfig,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Green50 else MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Green800 else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = crop.name,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) Green800 else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = crop.description,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
