package com.wawra.messages.di.modules

import androidx.lifecycle.ViewModel
import com.wawra.messages.di.scopes.ViewModelKey
import com.wawra.messages.presentation.posts.PostsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PostsViewModel::class)
    abstract fun bindPostsViewModel(postsViewModel: PostsViewModel): ViewModel

}