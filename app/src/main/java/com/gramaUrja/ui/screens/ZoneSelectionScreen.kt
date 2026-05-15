package com.gramaUrja.ui.screens

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gramaUrja.data.model.PowerStatus
import com.gramaUrja.data.model.Zone
import com.gramaUrja.data.model.ZoneType
import com.gramaUrja.ui.theme.Green50
import com.gramaUrja.ui.theme.Green800
import com.gramaUrja.ui.theme.Red50
import com.gramaUrja.ui.theme.Red800
import com.gramaUrja.viewmodel.MainViewModel

@Composable
fun ZoneSelectionScreen(
    viewModel: MainViewModel,
    onContinue: () -> Unit
) {
    val zones by viewModel.zones.collectAsState()
    val savedZoneId by viewModel.selectedZoneId.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedId by remember { mutableStateOf(savedZoneId) }

    val filteredZones = zones.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 52.dp, bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Select Your Zone",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Choose your village or transformer zone to monitor power",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search zones...", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(20.dp))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Zone List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredZones) { zone ->
                    ZoneSelectionCard(
                        zone = zone,
                        isSelected = selectedId == zone.id,
                        onClick = { selectedId = zone.id }
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            // Continue Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp, top = 12.dp)
            ) {
                Button(
                    onClick = {
                        selectedId?.let {
                            viewModel.setSelectedZoneId(it)
                            onContinue()
                        }
                    },
                    enabled = selectedId != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green800,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = "Continue",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun ZoneSelectionCard(zone: Zone, isSelected: Boolean, onClick: () -> Unit) {
    val cardBg by animateColorAsState(
        targetValue = if (isSelected) Green50 else Color.White,
        animationSpec = tween(200),
        label = "card_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Green800 else Color(0xFFC8E6C9),
        animationSpec = tween(200),
        label = "border"
    )

    val isOn = zone.powerStatus == PowerStatus.ON

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Zone icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (isSelected) Green800 else Color(0xFFE8F5E9),
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (zone.type == ZoneType.VILLAGE) Icons.Filled.Home else Icons.Filled.ElectricBolt,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else Color(0xFF5D7A5F),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = zone.name,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    fontSize = 15.sp,
                    color = if (isSelected) Green800 else Color(0xFF1B2E1C)
                )
                Text(
                    text = if (zone.type == ZoneType.VILLAGE) "Village Zone" else "Transformer Zone",
                    fontSize = 12.sp,
                    color = Color(0xFF5D7A5F)
                )
            }

            // Power Status Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        if (isOn) Green50 else Red50,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(if (isOn) Green800 else Red800, CircleShape)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = zone.powerStatus.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isOn) Green800 else Red800
                )
            }

            if (isSelected) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Selected",
                    tint = Green800,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
