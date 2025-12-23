package com.redifor.diarysof.data.dao

import androidx.room.*
import com.redifor.diarysof.data.model.DayEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface DayEntryDao {
    @Query("SELECT * FROM day_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<DayEntry>>
    
    @Query("SELECT * FROM day_entries WHERE date = :date")
    suspend fun getEntryByDate(date: Long): DayEntry?
    
    @Query("SELECT * FROM day_entries WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getEntriesInRange(startDate: Long, endDate: Long): Flow<List<DayEntry>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: DayEntry)
    
    @Update
    suspend fun updateEntry(entry: DayEntry)
    
    @Delete
    suspend fun deleteEntry(entry: DayEntry)
    
    @Query("DELETE FROM day_entries")
    suspend fun deleteAllEntries()
    
    @Query("SELECT COUNT(*) FROM day_entries WHERE luckStatus = :status")
    suspend fun getCountByStatus(status: String): Int
}

