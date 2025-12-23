package com.redifor.diarysof.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.redifor.diarysof.data.database.AppDatabase
import com.redifor.diarysof.data.datastore.PreferencesManager
import com.redifor.diarysof.data.model.DayEntry
import com.redifor.diarysof.data.model.LuckStatus
import com.redifor.diarysof.data.repository.DiaryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: DiaryRepository
    private val preferencesManager: PreferencesManager
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = DiaryRepository(database.dayEntryDao())
        preferencesManager = PreferencesManager(application)
    }
    
    val allEntries: Flow<List<DayEntry>> = repository.allEntries
    
    val onboardingCompleted: Flow<Boolean> = preferencesManager.onboardingCompleted
    
    val reminderEnabled: Flow<Boolean> = preferencesManager.reminderEnabled
    
    val reminderTime: Flow<Pair<Int, Int>> = preferencesManager.reminderTime
    
    val themeMode: Flow<String> = preferencesManager.themeMode
    
    // Statistics
    private val _luckyDaysCount = MutableStateFlow(0)
    val luckyDaysCount: StateFlow<Int> = _luckyDaysCount.asStateFlow()
    
    private val _unluckyDaysCount = MutableStateFlow(0)
    val unluckyDaysCount: StateFlow<Int> = _unluckyDaysCount.asStateFlow()
    
    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()
    
    private val _longestStreak = MutableStateFlow(0)
    val longestStreak: StateFlow<Int> = _longestStreak.asStateFlow()
    
    init {
        loadStatistics()
    }
    
    private fun loadStatistics() {
        viewModelScope.launch {
            _luckyDaysCount.value = repository.getLuckyDaysCount()
            _unluckyDaysCount.value = repository.getUnluckyDaysCount()
            
            allEntries.collect { entries ->
                var currentStreak = 0
                var maxStreak = 0
                var tempStreak = 0
                
                val sortedEntries = entries.sortedByDescending { it.date }
                
                // Calculate current streak
                for (entry in sortedEntries) {
                    if (entry.luckStatus == LuckStatus.LUCKY) {
                        currentStreak++
                    } else {
                        break
                    }
                }
                
                // Calculate longest streak
                for (entry in entries.sortedBy { it.date }) {
                    if (entry.luckStatus == LuckStatus.LUCKY) {
                        tempStreak++
                        if (tempStreak > maxStreak) {
                            maxStreak = tempStreak
                        }
                    } else {
                        tempStreak = 0
                    }
                }
                
                _currentStreak.value = currentStreak
                _longestStreak.value = maxStreak
            }
        }
    }
    
    suspend fun getTodayEntry(): DayEntry? {
        val today = getTodayTimestamp()
        return repository.getEntryByDate(today)
    }
    
    suspend fun getEntryByDate(date: Long): DayEntry? {
        return repository.getEntryByDate(date)
    }
    
    fun saveEntry(date: Long, luckStatus: LuckStatus, note: String) {
        viewModelScope.launch {
            val entry = DayEntry(date, luckStatus, note)
            repository.insertOrUpdateEntry(entry)
            loadStatistics()
        }
    }
    
    fun deleteEntry(entry: DayEntry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
            loadStatistics()
        }
    }
    
    fun setOnboardingCompleted(completed: Boolean) {
        viewModelScope.launch {
            preferencesManager.setOnboardingCompleted(completed)
        }
    }
    
    fun setReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setReminderEnabled(enabled)
        }
    }
    
    fun setReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            preferencesManager.setReminderTime(hour, minute)
        }
    }
    
    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            preferencesManager.setThemeMode(mode)
        }
    }
    
    fun resetAllData() {
        viewModelScope.launch {
            repository.deleteAllEntries()
            loadStatistics()
        }
    }
    
    fun exportData(entries: List<DayEntry>): String {
        // Export all entries to CSV format
        val sb = StringBuilder()
        sb.append("Date,Status,Note\n")
        
        for (entry in entries) {
            val date = formatDate(entry.date)
            sb.append("$date,${entry.luckStatus.name},\"${entry.note}\"\n")
        }
        
        return sb.toString()
    }
    
    private fun getTodayTimestamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    private fun formatDate(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"
    }
}

