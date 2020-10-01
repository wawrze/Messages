package com.wawra.messages.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wawra.messages.R
import com.wawra.messages.base.BaseViewModel
import com.wawra.messages.database.entities.Post
import com.wawra.messages.logic.PostRepository
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class MainViewModel @Inject constructor(private val postRepository: PostRepository) :
    BaseViewModel() {

    private val mPosts = MutableLiveData<List<Post>>()
    private val mError = MutableLiveData<Int>()

    val models: LiveData<List<Post>>
        get() = mPosts
    val error: LiveData<Int>
        get() = mError

    fun getPosts() {
        postRepository.getPostsFromDb()
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