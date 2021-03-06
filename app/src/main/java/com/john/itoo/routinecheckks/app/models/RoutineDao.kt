package com.john.itoo.routinecheckks.app.models

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface RoutineDao {

    @Query("SELECT * FROM databaseroutine WHERE date <= :capDate AND date > :floorDate" )
    fun getNextUpRoutines(capDate: Date, floorDate: Date): LiveData<List<DatabaseRoutine>>

    @Query("SELECT * FROM databaseroutine ORDER BY :currDate - date  ASC")
    fun getAllRoutines(currDate: Date): LiveData<List<DatabaseRoutine>>

    @Query("SELECT * FROM databaseroutine")
    fun getAllRoutinesAsync(): List<DatabaseRoutine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(routine: DatabaseRoutine): Long

    @Query("SELECT * FROM databaseroutine WHERE date = :date ")
    fun getRoutine(date : Date) : DatabaseRoutine

    @Update
    fun update(routine: DatabaseRoutine)

    @Delete
    fun delete(routine: DatabaseRoutine)

    @Query("DELETE FROM databaseroutine")
    fun dropTable()
}