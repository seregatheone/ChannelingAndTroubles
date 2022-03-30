package com.example.a20.ui

import com.example.a20.ui.first.FirstFragment
import com.example.a20.ui.second.SecondFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UIModule {

    @ContributesAndroidInjector
    abstract fun bindFirstFragment() : FirstFragment

    @ContributesAndroidInjector
    abstract fun bindWorkManagerFragment() : SecondFragment
}