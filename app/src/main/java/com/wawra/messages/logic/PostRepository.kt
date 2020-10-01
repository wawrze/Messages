package com.wawra.messages.logic

import com.wawra.messages.database.daos.PostDao
import com.wawra.messages.database.entities.Post
import com.wawra.messages.network.ApiInterface
import com.wawra.messages.network.models.PostsResponse
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postDao: PostDao,
    private val api: ApiInterface
) {

    fun getPostsFromDb() = postDao.getAll()

    fun getPostsFromApi() = api.getPosts()
        .onErrorReturn { PostsResponse(listOf()) }
        .map { response -> response.posts.map { Post(0L, it.id) } }

}