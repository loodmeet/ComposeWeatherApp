package com.loodmeet.weatherapp.di

import android.app.Application
import com.loodmeet.weatherapp.di.modules.DataModule
import com.loodmeet.weatherapp.di.modules.DomainBindModule
import com.loodmeet.weatherapp.di.modules.DomainModule
import com.loodmeet.weatherapp.ui.activity.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@AppScope
@Component(modules = arrayOf(DomainModule::class, DataModule::class, DomainBindModule::class))
interface MainActivityComponent {

    fun inject(activity: MainActivity)

    @Component.Builder interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): MainActivityComponent
    }
}