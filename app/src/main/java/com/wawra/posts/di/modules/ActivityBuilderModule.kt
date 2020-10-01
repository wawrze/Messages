package com.wawra.posts.di.modules

import com.wawra.posts.di.scopes.ActivityScoped
import com.wawra.posts.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity?

}