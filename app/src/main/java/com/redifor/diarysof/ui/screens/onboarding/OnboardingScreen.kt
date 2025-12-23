package com.redifor.diarysof.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redifor.diarysof.ui.theme.*
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: String
)

private val onboardingPages = listOf(
    OnboardingPage(
        title = "Track Your Daily Luck",
        description = "Mark each day as lucky, neutral, or unlucky. Build your fortune diary and see patterns in your life.",
        icon = "🌟"
    ),
    OnboardingPage(
        title = "Save Notes and Moments",
        description = "Capture the details that matter. Write notes about what made your day lucky or unlucky.",
        icon = "📝"
    ),
    OnboardingPage(
        title = "See Your Luck Statistics",
        description = "Visualize your fortune over time. Track streaks, analyze patterns, and watch your luck evolve.",
        icon = "📊"
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: com.redifor.diarysof.viewmodel.DiaryViewModel,
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

     Scaffold { paddingValues ->
         Box(
             modifier = Modifier
                 .fillMaxSize()
                 .background(
                     Brush.verticalGradient(
                         colors = listOf(
                             MaterialTheme.colorScheme.background,
                             MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                             MaterialTheme.colorScheme.background
                         )
                     )
                 )
                 .padding(paddingValues)
         ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Skip button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                     TextButton(onClick = {
                         viewModel.setOnboardingCompleted(true)
                         onFinish()
                     }) {
                         Text(
                             text = "Skip",
                             color = MaterialTheme.colorScheme.secondary
                         )
                     }
                }

                // Pager
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) { page ->
                    OnboardingPageContent(onboardingPages[page])
                }

                // Indicators
                Row(
                    modifier = Modifier.padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(onboardingPages.size) { index ->
                        Box(
                            modifier = Modifier
                             .size(if (index == pagerState.currentPage) 24.dp else 8.dp, 8.dp)
                             .clip(CircleShape)
                             .background(
                                 if (index == pagerState.currentPage) 
                                     MaterialTheme.colorScheme.primary 
                                 else 
                                     MaterialTheme.colorScheme.surfaceVariant
                             )
                        )
                    }
                }

                // Navigation buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AnimatedVisibility(visible = pagerState.currentPage > 0) {
                         OutlinedButton(
                             onClick = {
                                 scope.launch {
                                     pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                 }
                             },
                             colors = ButtonDefaults.outlinedButtonColors(
                                 contentColor = MaterialTheme.colorScheme.secondary
                             )
                         ) {
                             Text("Back")
                         }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            if (pagerState.currentPage < onboardingPages.size - 1) {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                                viewModel.setOnboardingCompleted(true)
                                onFinish()
                            }
                        },
                         colors = ButtonDefaults.buttonColors(
                             containerColor = MaterialTheme.colorScheme.primary,
                             contentColor = MaterialTheme.colorScheme.onPrimary
                         ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (pagerState.currentPage < onboardingPages.size - 1) "Next" else "Start",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = page.icon,
            fontSize = 100.sp
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

