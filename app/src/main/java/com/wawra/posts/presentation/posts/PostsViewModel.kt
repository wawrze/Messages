package com.wawra.posts.presentation.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wawra.posts.R
import com.wawra.posts.base.BaseViewModel
import com.wawra.posts.database.entities.Post
import com.wawra.posts.logic.PostRepository
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class PostsViewModel @Inject constructor(private val postRepository: PostRepository) :
    BaseViewModel() {

    private val mPosts = MutableLiveData<List<Post>>()
    private val mError = MutableLiveData<Int>()

    val posts: LiveData<List<Post>>
        get() = mPosts
    val error: LiveData<Int>
        get() = mError

    fun getPosts() {
        postRepository.getPosts()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                { mPosts.postValue(it) },
                {
                    it.printStackTrace()
                    mError.postValue(R.string.unknown_error)
                }
            )
            .addToDisposables()
    }

}