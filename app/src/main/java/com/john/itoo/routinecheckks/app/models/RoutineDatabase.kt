package com.john.itoo.routinecheckks.app.models

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DatabaseRoutine::class], version = 1, exportSchema = false)
@TypeConverters(com.john.itoo.routinecheckks.utils.TimeUtils::class)

abstract class RoutineDatabase: RoomDatabase() {
    abstract val routineDao: RoutineDao
}