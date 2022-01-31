package com.udacity.asteroidradar.database

import androidx.room.TypeConverter
import java.util.*

class MyConverters {

    @TypeConverter
    fun fromTime(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimes(date: Date?): Long? {
        return date?.time
    }


}
