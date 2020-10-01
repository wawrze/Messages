package com.wawra.posts.presentation.postDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wawra.posts.base.BaseViewModel
import com.wawra.posts.database.entities.Post
import com.wawra.posts.logic.PostRepository
import com.wawra.posts.logic.models.ErrorCodes
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class PostDetailsViewModel @Inject constructor(private val postRepository: PostRepository) :
    BaseViewModel() {

    // TODO: unit tests

    private val mPost = MutableLiveData<Post>()
    private val mError = MutableLiveData<Int>()

    val post: LiveData<Post>
        get() = mPost
    val error: LiveData<Int>
        get() = mError

    fun getPost(postId: Long) {
        postRepository.getPostById(postId)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                { mPost.postValue(it) },
                {
                    it.printStackTrace()
                    mError.postValue(ErrorCodes.POST_DETAILS_VIEW_MODEL_GET_POST.code)
                }
            )
            .addToDisposables()
    }

}