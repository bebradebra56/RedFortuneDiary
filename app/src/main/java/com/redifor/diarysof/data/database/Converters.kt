package com.redifor.diarysof.data.database

import androidx.room.TypeConverter
import com.redifor.diarysof.data.model.LuckStatus

class Converters {
    @TypeConverter
    fun fromLuckStatus(status: LuckStatus): String {
        return status.name
    }
    
    @TypeConverter
    fun toLuckStatus(value: String): LuckStatus {
        return LuckStatus.valueOf(value)
    }
}

