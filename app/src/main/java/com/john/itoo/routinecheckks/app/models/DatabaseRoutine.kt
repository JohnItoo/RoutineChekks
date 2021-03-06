package com.john.itoo.routinecheckks.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.john.itoo.routinecheckks.utils.TimeUtils
import java.util.*

@Entity
class DatabaseRoutine(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    val description: String,

    val title: String,

    val frequency: Int,

    @TypeConverters(TimeUtils::class)
    val createdAt: Date,

    @TypeConverters(TimeUtils::class)
    val date: Date,

    @TypeConverters(TimeUtils::class)
    val nextTime: Date,

    val total: Int
)

fun List<DatabaseRoutine>.asDomainModel(): List<Routine> {
    return map {
        Routine(
            id = it.id,

            description = it.description,

            title = it.title,

            frequency = it.frequency,

            createdAt = it.createdAt,

            date = it.date,

            nextTime = it.nextTime,

            total = it.total
        )
    }
}

fun Routine.asDbRoutine(): DatabaseRoutine {
    return DatabaseRoutine(
        this.id,
        this.description,
        this.title,
        this.frequency,
        this.createdAt,
        this.date,
        this.nextTime,
        this.total
    )
}

fun DatabaseRoutine.asRoutine(): Routine {
    return Routine(
        this.id,
        this.description,
        this.title,
        this.frequency,
        this.createdAt,
        this.date,
        this.nextTime,
        this.total
    )
}