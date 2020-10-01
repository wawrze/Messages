package com.wawra.messages.logic

import com.wawra.messages.database.daos.PostDao
import com.wawra.messages.database.entities.Post
import com.wawra.messages.logic.models.PostStatus
import com.wawra.messages.network.ApiInterface
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postDao: PostDao,
    private val api: ApiInterface
) {

    fun getPosts(): Observable<List<Post>> = Observable.concatArray(
        postDao.getAll().toObservable(),
        getPostsFromApi().toObservable()
    )

    fun getPostById(postId: Long) = Single.just(Post(0L, 0L))

    fun updatePost() = Single.just(true)

    fun deletePost() = Single.just(true)

    fun getDeletedPosts() = Single.just(listOf<Post>())

    fun restoreDeletedPost(postId: Long) = Single.just(true)

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