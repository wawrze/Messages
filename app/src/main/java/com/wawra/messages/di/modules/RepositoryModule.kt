package com.wawra.messages.di.modules

import com.wawra.messages.database.daos.PostDao
import com.wawra.messages.di.scopes.AppScoped
import com.wawra.messages.logic.PostRepository
import com.wawra.messages.network.ApiInterface
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