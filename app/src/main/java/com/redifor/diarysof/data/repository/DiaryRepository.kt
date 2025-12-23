package com.redifor.diarysof.data.repository

import com.redifor.diarysof.data.dao.DayEntryDao
import com.redifor.diarysof.data.model.DayEntry
import com.redifor.diarysof.data.model.LuckStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DiaryRepository(private val dao: DayEntryDao) {
    
    val allEntries: Flow<List<DayEntry>> = dao.getAllEntries()
    
    suspend fun getEntryByDate(date: Long): DayEntry? {
        return dao.getEntryByDate(date)
    }
    
    fun getEntriesInRange(startDate: Long, endDate: Long): Flow<List<DayEntry>> {
        return dao.getEntriesInRange(startDate, endDate)
    }
    
    suspend fun insertOrUpdateEntry(entry: DayEntry) {
        dao.insertEntry(entry)
    }
    
    suspend fun deleteEntry(entry: DayEntry) {
        dao.deleteEntry(entry)
    }
    
    suspend fun deleteAllEntries() {
        dao.deleteAllEntries()
    }
    
    suspend fun getLuckyDaysCount(): Int {
        return dao.getCountByStatus(LuckStatus.LUCKY.name)
    }
    
    suspend fun getUnluckyDaysCount(): Int {
        return dao.getCountByStatus(LuckStatus.UNLUCKY.name)
    }
    
    suspend fun getCurrentStreak(): Int {
        // Calculate current streak of lucky days
        val entries = dao.getAllEntries()
        var streak = 0
        entries.map { list ->
            for (entry in list) {
                if (entry.luckStatus == LuckStatus.LUCKY) {
                    streak++
                } else {
                    break
                }
            }
        }
        return streak
    }
}

