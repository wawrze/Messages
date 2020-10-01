package com.wawra.posts.di.modules

import com.wawra.posts.di.scopes.FragmentScoped
import com.wawra.posts.presentation.posts.PostsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributePostsFragment(): PostsFragment?

}