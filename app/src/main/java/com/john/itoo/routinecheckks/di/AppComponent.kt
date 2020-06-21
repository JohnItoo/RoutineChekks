package com.john.itoo.routinecheckks.di

import android.app.Application
import com.john.itoo.routinecheckks.app.routinelist.RoutineListFragment
import com.john.itoo.routinecheckks.app.newroutines.CreateRoutineFragment
import com.john.itoo.routinecheckks.app.routinedetails.RoutineDetailsFragment
import com.john.itoo.routinecheckks.scheduling.*
import com.john.itoo.routinecheckks.utils.TimeUtils
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, LocalDataModule::class])
interface AppComponent {

    fun inject(target: RoutineListFragment)
    fun inject(target: RefreshDataWork)
    fun inject(target: CreateRoutineFragment)
    fun inject(target: TimeUtils)
    fun inject(target: AlarmFanny)
    fun inject(target: NotificationHelper)
    fun inject(target: AlarmReceiver)
    fun inject(target: RoutineDetailsFragment)
    fun inject(target: RestartAlarmsService)
    fun inject(target: BootReceiver)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun application(app: Application): Builder
    }
}
