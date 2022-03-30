package com.example.a20.di

import android.app.Application
import android.content.Context
import com.example.a20.ProjectApplication
import com.example.a20.data.DataModule
import com.example.a20.ui.UIModule
import com.example.a20.ui.second.SharedFragmentViewModelFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Scope

@[AppScope Component(modules = [AppModule::class, AndroidInjectionModule::class])]
interface AppComponent: AndroidInjector<ProjectApplication> {

    fun sharedFragmentViewModelFactory() : SharedFragmentViewModelFactory

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }


}
@Module(includes = [DataModule::class, UIModule::class,SharedPreferencesModule::class])
class AppModule{
    @[Provides AppScope]
    fun provideAppContext(application: Application): Context = application.applicationContext
}

@Scope
annotation class AppScope