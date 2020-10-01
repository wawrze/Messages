package com.wawra.posts.di.modules

import com.wawra.posts.database.daos.PostDao
import com.wawra.posts.di.scopes.AppScoped
import com.wawra.posts.logic.PostRepository
import com.wawra.posts.network.ApiInterface
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @AppScoped
    @Provides
    fun providePostRepository(
        postDao: PostDao,
        api: ApiInterface
    ): PostRepository = PostRepository(postDao, api)

}