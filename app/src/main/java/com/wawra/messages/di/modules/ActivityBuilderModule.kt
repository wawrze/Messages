package com.wawra.messages.di.modules

import com.wawra.messages.di.scopes.ActivityScoped
import com.wawra.messages.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity?

}