package com.example.a20.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesModule {

    @[Provides AppScope]
    fun provideSharedPreferencesSettings(application: Application): SharedPreferences {
        return when (val state =
            application.getSharedPreferences("counter", Context.MODE_PRIVATE)) {
            null -> {
                val editor = state?.edit()
                editor!!.putInt("current", 0)
                editor!!.putInt("total", 0)
                editor.apply()
                return application.getSharedPreferences("counter", Context.MODE_PRIVATE)
            }
            else -> state
        }

    }
}