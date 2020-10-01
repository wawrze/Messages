package com.wawra.messages.logic

import com.wawra.messages.database.daos.ModelDao
import com.wawra.messages.database.entities.Model
import com.wawra.messages.network.ApiInterface
import com.wawra.messages.network.models.PostsResponse
import javax.inject.Inject

class ModelRepository @Inject constructor(
    private val modelDao: ModelDao,
    private val api: ApiInterface
) {

    fun getModelsFromDb() = modelDao.getAll()

    fun getModelsFromApi() = api.getPosts()
        .onErrorReturn { PostsResponse(listOf()) }
        .map { response -> response.posts.map { Model(it.id) } }

}