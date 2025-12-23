package com.redifor.diarysof.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_entries")
data class DayEntry(
    @PrimaryKey
    val date: Long, // timestamp
    val luckStatus: LuckStatus,
    val note: String = ""
)

enum class LuckStatus {
    LUCKY,
    NEUTRAL,
    UNLUCKY
}

