package com.gramaUrja.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gramaUrja.ui.theme.Amber
import com.gramaUrja.ui.theme.Green800
import com.gramaUrja.viewmodel.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigate: (hasZone: Boolean) -> Unit,
    viewModel: MainViewModel
) {
    val selectedZoneId by viewModel.selectedZoneId.collectAsState()

    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.7f) }
    val taglineAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Fade in + scale up the logo
        kotlinx.coroutines.coroutineScope {
            kotlinx.coroutines.launch {
                alphaAnim.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
                )
            }
            kotlinx.coroutines.launch {
                scaleAnim.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
                )
            }
        }
        // Then fade in tagline
        taglineAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400)
        )
        // Wait and navigate
        delay(1400)
        onNavigate(selectedZoneId != null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Green800),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value)
        ) {
            // App Icon
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Eco,
                    contentDescription = "Grama-Urja Logo",
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
                // Lightning bolt overlay
                Box(
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Text(
                        text = "⚡",
                        fontSize = 20.sp,
                        color = Amber
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Grama-Urja",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
        }

        // Tagline at bottom of logo area
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(taglineAlpha.value)
                .padding(top = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Community Powered Smart Irrigation",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 40.dp)
            )
        }

        // Loading dots at bottom
        LoadingDots(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
                .alpha(taglineAlpha.value)
        )
    }
}

@Composable
private fun LoadingDots(modifier: Modifier = Modifier) {
    val dot1 = remember { Animatable(0.3f) }
    val dot2 = remember { Animatable(0.3f) }
    val dot3 = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        while (true) {
            dot1.animateTo(1f, tween(300))
            dot2.animateTo(1f, tween(300))
            dot3.animateTo(1f, tween(300))
            dot1.animateTo(0.3f, tween(300))
            dot2.animateTo(0.3f, tween(300))
            dot3.animateTo(0.3f, tween(300))
        }
    }

    androidx.compose.foundation.layout.Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(dot1.value, dot2.value, dot3.value).forEach { alpha ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .alpha(alpha)
                    .background(Color.White.copy(alpha = 0.8f), CircleShape)
            )
        }
    }
}
