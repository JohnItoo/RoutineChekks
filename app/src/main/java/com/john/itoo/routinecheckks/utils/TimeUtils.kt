package com.john.itoo.routinecheckks.utils

import androidx.room.TypeConverter
import java.util.*


class TimeUtils  {

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toTimeStamp(date: Date?): Long? {
        return date?.time
    }
}