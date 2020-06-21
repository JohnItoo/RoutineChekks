package com.john.itoo.routinecheckks.di

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.john.itoo.routinecheckks.utils.PrefsUtils
import com.google.gson.Gson
import com.john.itoo.routinecheckks.app.RoutineRepository
import com.john.itoo.routinecheckks.app.models.RoutineDatabase
import com.john.itoo.routinecheckks.scheduling.AlarmFanny
import com.john.itoo.routinecheckks.scheduling.AlarmReceiver
import com.john.itoo.routinecheckks.utils.TimeUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalDataModule {

    @Provides
    @Singleton
    fun providePrefsUtils(prefs: SharedPreferences, gson: Gson): PrefsUtils =
        PrefsUtils(prefs, gson)

    @Provides
    @Singleton
    fun provideGlobalSharedPreference(app: Application): SharedPreferences =
        app.getSharedPreferences("global_shared_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideRoutineDatabase(app: Application): RoutineDatabase = Room.databaseBuilder(
        app,
        RoutineDatabase::class.java,
        "routine-database.db"
    ).build()

    @Provides
    @Singleton
    fun provideTimeUtiles(): TimeUtils =
        TimeUtils()

    @Provides
    @Singleton
    fun provideAlarmManager(app: Application): AlarmManager =
        app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Provides
    fun provideAlarmReceiverIntent(app: Application): Intent =
        Intent(app, AlarmReceiver::class.java)


    @Provides
    fun provideAlarmFanny(alarmManager: AlarmManager, timeUtils: TimeUtils): AlarmFanny =
        AlarmFanny(alarmManager, timeUtils)

    @Provides
    fun provideExampleRepositiory(db: RoutineDatabase, timeUtils: TimeUtils): RoutineRepository =
        RoutineRepository(db, timeUtils)

    //    @Provides
//    fun provideAppContext(app: Context) : Context =
// app.applicationContext
    @Provides
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideNotificationManager(app: Application): NotificationManager =
        app.getSystemService(NotificationManager::class.java)

    @Provides
    @Singleton
    fun provideNotificationBuilder(app: Application): NotificationCompat.Builder =
        NotificationCompat.Builder(app, "todos")

}
