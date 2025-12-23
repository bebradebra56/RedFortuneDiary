package com.redifor.diarysof.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.redifor.diarysof.data.model.DayEntry
import com.redifor.diarysof.data.model.LuckStatus
import com.redifor.diarysof.ui.theme.*
import com.redifor.diarysof.viewmodel.DiaryViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: DiaryViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDayDetails: (Long) -> Unit
) {
    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }
    val entries by viewModel.allEntries.collectAsState(initial = emptyList())
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.secondary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Month selector
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            currentMonth = (currentMonth.clone() as Calendar).apply {
                                add(Calendar.MONTH, -1)
                            }
                        }) {
                            Icon(
                                Icons.Default.KeyboardArrowLeft,
                                contentDescription = "Previous month",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                        
                        Text(
                            text = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
                                .format(currentMonth.time),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        
                        IconButton(onClick = {
                            currentMonth = (currentMonth.clone() as Calendar).apply {
                                add(Calendar.MONTH, 1)
                            }
                        }) {
                            Icon(
                                Icons.Default.KeyboardArrowRight,
                                contentDescription = "Next month",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Weekday headers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                        Text(
                            text = day,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Calendar grid
                CalendarGrid(
                    currentMonth = currentMonth,
                    entries = entries,
                    onDayClick = onNavigateToDayDetails
                )
            }
        }
        }
    )
}

@Composable
fun CalendarGrid(
    currentMonth: Calendar,
    entries: List<DayEntry>,
    onDayClick: (Long) -> Unit
) {
    val firstDayOfMonth = (currentMonth.clone() as Calendar).apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val startDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1
    
    val days = mutableListOf<CalendarDay>()
    
    // Add empty cells for days before the first day of month
    repeat(startDayOfWeek) {
        days.add(CalendarDay.Empty)
    }
    
    // Add all days of the month
    for (day in 1..daysInMonth) {
        val calendar = (currentMonth.clone() as Calendar).apply {
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val timestamp = calendar.timeInMillis
        val entry = entries.find { it.date == timestamp }
        days.add(CalendarDay.Day(day, timestamp, entry))
    }
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(days) { calendarDay ->
            when (calendarDay) {
                is CalendarDay.Empty -> Box(modifier = Modifier.aspectRatio(1f))
                is CalendarDay.Day -> DayCell(
                    day = calendarDay.dayNumber,
                    entry = calendarDay.entry,
                    onClick = { if (calendarDay.entry != null) onDayClick(calendarDay.timestamp) }
                )
            }
        }
    }
}

@Composable
fun DayCell(
    day: Int,
    entry: DayEntry?,
    onClick: () -> Unit
) {
    val backgroundColor = when (entry?.luckStatus) {
        LuckStatus.LUCKY -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        LuckStatus.NEUTRAL -> MaterialTheme.colorScheme.surfaceVariant
        LuckStatus.UNLUCKY -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        null -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    val textColor = when (entry?.luckStatus) {
        LuckStatus.LUCKY -> MaterialTheme.colorScheme.tertiary
        LuckStatus.NEUTRAL -> MaterialTheme.colorScheme.onSurface
        LuckStatus.UNLUCKY -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        null -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    }
    
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(enabled = entry != null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (entry != null) FontWeight.Bold else FontWeight.Normal
        )
    }
}

sealed class CalendarDay {
    object Empty : CalendarDay()
    data class Day(val dayNumber: Int, val timestamp: Long, val entry: DayEntry?) : CalendarDay()
}

