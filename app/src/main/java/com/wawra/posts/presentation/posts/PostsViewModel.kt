package com.wawra.posts.presentation.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wawra.posts.base.BaseViewModel
import com.wawra.posts.database.entities.Post
import com.wawra.posts.logic.PostRepository
import com.wawra.posts.logic.models.ErrorCodes
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class PostsViewModel @Inject constructor(private val postRepository: PostRepository) :
    BaseViewModel() {

    private val mError = MutableLiveData<Int>()

    val error: LiveData<Int>
        get() = mError

    fun getPosts(): LiveData<List<Post>> {
        val posts = MutableLiveData<List<Post>>()

        postRepository.getPosts()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                { posts.postValue(it) },
                {
                    it.printStackTrace()
                    mError.postValue(ErrorCodes.POSTS_VIEW_MODEL_GET_POSTS.code)
                }
            )
            .addToDisposables()

        return posts
    }

}