package com.wawra.messages.di.modules

import com.wawra.messages.di.scopes.FragmentScoped
import com.wawra.messages.presentation.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeMainFragment(): MainFragment?

}