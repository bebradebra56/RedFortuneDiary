package com.redifor.diarysof.ui.screens.today

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redifor.diarysof.R
import com.redifor.diarysof.data.model.LuckStatus
import com.redifor.diarysof.ui.theme.*
import com.redifor.diarysof.viewmodel.DiaryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    viewModel: DiaryViewModel,
    onNavigateToAddNote: (Long) -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToStreaks: () -> Unit,
    onNavigateToReminders: () -> Unit,
    onNavigateToExport: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    var selectedLuckStatus by remember { mutableStateOf<LuckStatus?>(null) }
    var drawerOpen by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    val todayTimestamp = remember {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.timeInMillis
    }
    
    LaunchedEffect(Unit) {
        val entry = viewModel.getTodayEntry()
        selectedLuckStatus = entry?.luckStatus
    }
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    
    LaunchedEffect(drawerOpen) {
        if (drawerOpen) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "🎩 Red Fortune",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
                
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                
                NavigationDrawerItem(
                    icon = { 
                        Icon(
                            Icons.Default.DateRange, 
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        ) 
                    },
                    label = { Text("Calendar") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToCalendar()
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary
                    )
                )
                
                NavigationDrawerItem(
                    icon = { 
                        Icon(
                            painter = painterResource(R.drawable.fire_fortine_ic_analytics), 
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        ) 
                    },
                    label = { Text("Statistics") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToStatistics()
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary
                    )
                )
                
                NavigationDrawerItem(
                    icon = { 
                        Icon(
                            painter = painterResource(R.drawable.fire_department), 
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        ) 
                    },
                    label = { Text("Streaks") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToStreaks()
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary
                    )
                )
                
                NavigationDrawerItem(
                    icon = { 
                        Icon(
                            painter = painterResource(R.drawable.ic_download), 
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        ) 
                    },
                    label = { Text("Export") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToExport()
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary
                    )
                )
                
                NavigationDrawerItem(
                    icon = { 
                        Icon(
                            Icons.Default.Settings, 
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        ) 
                    },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToSettings()
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Today", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { 
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.secondary
                    )
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        actionColor = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
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
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Date
                    Text(
                        text = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.ENGLISH)
                            .format(Date(todayTimestamp)),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Question
                    Text(
                        text = "How lucky was your day?",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    // Luck status options
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LuckStatusButton(
                            status = LuckStatus.LUCKY,
                            isSelected = selectedLuckStatus == LuckStatus.LUCKY,
                            onClick = { selectedLuckStatus = LuckStatus.LUCKY }
                        )
                        
                        LuckStatusButton(
                            status = LuckStatus.NEUTRAL,
                            isSelected = selectedLuckStatus == LuckStatus.NEUTRAL,
                            onClick = { selectedLuckStatus = LuckStatus.NEUTRAL }
                        )
                        
                        LuckStatusButton(
                            status = LuckStatus.UNLUCKY,
                            isSelected = selectedLuckStatus == LuckStatus.UNLUCKY,
                            onClick = { selectedLuckStatus = LuckStatus.UNLUCKY }
                        )
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Add note button
                    OutlinedButton(
                        onClick = { onNavigateToAddNote(todayTimestamp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Note")
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Save button
                    Button(
                        onClick = {
                            selectedLuckStatus?.let { status ->
                                scope.launch {
                                    viewModel.saveEntry(todayTimestamp, status, "")
                                    snackbarHostState.showSnackbar(
                                        message = "✨ Day saved successfully!",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedLuckStatus != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Save Day",
                            modifier = Modifier.padding(vertical = 8.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LuckStatusButton(
    status: LuckStatus,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        label = "scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = when {
            !isSelected -> MaterialTheme.colorScheme.surfaceVariant
            status == LuckStatus.LUCKY -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            status == LuckStatus.NEUTRAL -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        },
        label = "background"
    )
    
    val (icon, text, iconColor) = when (status) {
        LuckStatus.LUCKY -> Triple("🔥", "Lucky Day", MaterialTheme.colorScheme.tertiary)
        LuckStatus.NEUTRAL -> Triple("⚖️", "Neutral Day", MaterialTheme.colorScheme.onSurface)
        LuckStatus.UNLUCKY -> Triple("💨", "Unlucky Day", MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = icon,
                fontSize = 36.sp
            )
            
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                color = iconColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(iconColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

