package com.wawra.messages.di.modules

import androidx.lifecycle.ViewModelProvider
import com.wawra.messages.base.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProviderFactory): ViewModelProvider.Factory

}