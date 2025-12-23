package com.redifor.diarysof.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToToday: () -> Unit,
    isOnboardingCompleted: Boolean
) {
    var hasNavigated by remember { mutableStateOf(false) }
    var isDataLoaded by remember { mutableStateOf(false) }
    
    // Wait a bit for DataStore to load the real value
    LaunchedEffect(Unit) {
        delay(300) // Give DataStore time to load
        isDataLoaded = true
    }
    
    LaunchedEffect(isDataLoaded, isOnboardingCompleted) {
        if (isDataLoaded && !hasNavigated) {
            delay(1700) // Show animation for remaining time (300 + 1700 = 2000ms total)
            hasNavigated = true
            if (isOnboardingCompleted) {
                onNavigateToToday()
            } else {
                onNavigateToOnboarding()
            }
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "splash_animation")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_animation"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha_animation"
    )
    
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Star and sparkles emoji
                Text(
                    text = "🌟",
                    fontSize = 80.sp,
                    modifier = Modifier
                        .scale(scale)
                        .alpha(alpha)
                )
                
                Text(
                    text = "✨",
                    fontSize = 64.sp,
                    modifier = Modifier
                        .scale(scale)
                        .alpha(alpha)
                )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // App name
            Text(
                text = "Red Fortune",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.alpha(alpha)
            )
            
            Text(
                text = "Diary",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.alpha(alpha)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Track your luck",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f),
                modifier = Modifier.alpha(alpha)
            )
        }
        }
    }
}

