package com.wawra.messages.di.components

import com.wawra.messages.App
import com.wawra.messages.di.modules.*
import com.wawra.messages.di.scopes.AppScoped
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@AppScoped
@Component(
    modules = [
        ActivityBuilderModule::class,
        AndroidSupportInjectionModule::class,
        DatabaseModule::class,
        FragmentBuilderModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelFactoryModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun build(): ApplicationComponent

    }

}