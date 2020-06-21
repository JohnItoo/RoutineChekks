package com.john.itoo.routinecheckks.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.john.itoo.routinecheckks.app.routinelist.RoutineListViewModel
import com.john.itoo.routinecheckks.app.newroutines.CreateRoutineViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ExampleAppViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RoutineListViewModel::class)
    abstract fun bindRoutineSourcesViewModel(viewModel: RoutineListViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(CreateRoutineViewModel::class)
    abstract fun bindCreateRoutineSourcesViewModel(viewModel: CreateRoutineViewModel): ViewModel


    // TODO Add other ViewModels.
}
