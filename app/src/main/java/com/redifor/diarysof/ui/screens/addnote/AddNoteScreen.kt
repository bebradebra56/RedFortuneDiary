package com.redifor.diarysof.ui.screens.addnote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.redifor.diarysof.data.model.LuckStatus
import com.redifor.diarysof.ui.theme.*
import com.redifor.diarysof.viewmodel.DiaryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    date: Long,
    viewModel: DiaryViewModel,
    onNavigateBack: () -> Unit
) {
    var note by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<LuckStatus?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(date) {
        val entry = viewModel.getEntryByDate(date)
        if (entry != null) {
            note = entry.note
            selectedStatus = entry.luckStatus
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(Date(date)),
                        fontWeight = FontWeight.Bold
                    )
                },
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
                    .padding(24.dp)
            ) {
                // Status selection
                Text(
                    text = "Day Status",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatusChip(
                        status = LuckStatus.LUCKY,
                        isSelected = selectedStatus == LuckStatus.LUCKY,
                        onClick = { selectedStatus = LuckStatus.LUCKY },
                        modifier = Modifier.weight(1f)
                    )
                    
                    StatusChip(
                        status = LuckStatus.NEUTRAL,
                        isSelected = selectedStatus == LuckStatus.NEUTRAL,
                        onClick = { selectedStatus = LuckStatus.NEUTRAL },
                        modifier = Modifier.weight(1f)
                    )
                    
                    StatusChip(
                        status = LuckStatus.UNLUCKY,
                        isSelected = selectedStatus == LuckStatus.UNLUCKY,
                        onClick = { selectedStatus = LuckStatus.UNLUCKY },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Note text field
                Text(
                    text = "What happened?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    placeholder = {
                        Text(
                            "Describe your day, what made it lucky or unlucky...",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Decorative cards
                Text(
                    text = "✨ 🌟 ⭐",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Save button
                Button(
                    onClick = {
                        selectedStatus?.let { status ->
                            scope.launch {
                                viewModel.saveEntry(date, status, note)
                                snackbarHostState.showSnackbar(
                                    message = "✨ Note saved successfully!",
                                    duration = SnackbarDuration.Short
                                )
                                kotlinx.coroutines.delay(800)
                                onNavigateBack()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedStatus != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Save",
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        }
    )
}

@Composable
fun StatusChip(
    status: LuckStatus,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (icon, color) = when (status) {
        LuckStatus.LUCKY -> "🔥" to MaterialTheme.colorScheme.primary
        LuckStatus.NEUTRAL -> "⚖️" to MaterialTheme.colorScheme.onSurface
        LuckStatus.UNLUCKY -> "💨" to MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }
    
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = icon,
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            selectedContainerColor = color.copy(alpha = 0.3f),
            labelColor = color,
            selectedLabelColor = color
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = if (isSelected) color else MaterialTheme.colorScheme.surfaceVariant,
            selectedBorderColor = color,
            borderWidth = 2.dp,
            selectedBorderWidth = 2.dp
        )
    )
}

