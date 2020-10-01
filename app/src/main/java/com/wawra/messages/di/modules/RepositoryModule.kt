package com.wawra.messages.di.modules

import com.wawra.messages.database.daos.ModelDao
import com.wawra.messages.di.scopes.AppScoped
import com.wawra.messages.logic.ModelRepository
import com.wawra.messages.network.ApiInterface
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @AppScoped
    @Provides
    fun provideModelRepository(
        modelDao: ModelDao,
        api: ApiInterface
    ): ModelRepository = ModelRepository(modelDao, api)

}