package com.wawra.messages.di.modules

import androidx.lifecycle.ViewModel
import com.wawra.messages.di.scopes.ViewModelKey
import com.wawra.messages.presentation.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

}