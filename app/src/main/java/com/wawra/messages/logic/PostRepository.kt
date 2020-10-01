package com.wawra.messages.logic

import com.wawra.messages.database.daos.PostDao
import com.wawra.messages.database.entities.Post
import com.wawra.messages.logic.models.PostStatus
import com.wawra.messages.network.ApiInterface
import io.reactivex.Observable
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postDao: PostDao,
    private val api: ApiInterface
) {

    fun getPosts(): Observable<List<Post>> = Observable.concatArray(
        postDao.getAll().toObservable(),
        getPostsFromApi().toObservable()
    )

    fun getPostById(postId: Long) = postDao.getById(postId)
        .onErrorReturn { Post(0L, 0L) }

    fun updatePost(
        postId: Long,
        newTitle: String,
        newDescription: String,
        newIconUrl: String
    ) = postDao.update(postId, newTitle, newDescription, newIconUrl)
        .onErrorReturn { 0 }
        .map { it > 0 }

    fun deletePost(postId: Long) = postDao.deleteById(postId)
        .onErrorReturn { 0 }
        .map { it > 0 }

    fun getDeletedPosts() = postDao.getAllDeleted()
        .onErrorReturn { listOf() }

    fun restoreDeletedPost(postId: Long) = postDao.restoreDeletedById(postId)
        .onErrorReturn { 0 }
        .map { it > 0 }

    private fun getPostsFromApi() = api.getPosts()
        .map { response ->
            response.posts.map {
                Post(
                    0L,
                    it.id,
                    it.title,
                    it.description,
                    it.icon,
                    PostStatus.UNCHANGED.value
                )
            }
        }
        .flatMap { postDao.insertPosts(it) }
        .onErrorReturn { listOf() }
        .flatMap { postDao.getAll() }

}