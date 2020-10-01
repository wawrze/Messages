package com.wawra.posts.di.modules

import androidx.lifecycle.ViewModel
import com.wawra.posts.di.scopes.ViewModelKey
import com.wawra.posts.presentation.postDetails.PostDetailsViewModel
import com.wawra.posts.presentation.posts.PostsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PostsViewModel::class)
    abstract fun bindPostsViewModel(postsViewModel: PostsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostDetailsViewModel::class)
    abstract fun bindPostDetailsViewModel(postDetailsViewModel: PostDetailsViewModel): ViewModel

}