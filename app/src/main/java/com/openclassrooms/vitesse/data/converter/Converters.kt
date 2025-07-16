package com.openclassrooms.vitesse.data.converter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }
    }

    @TypeConverter
    fun dateToTimestamp(dateTime: LocalDateTime?): Long? {
        return dateTime
            ?.atZone(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
    }
}

