package com.wawra.posts.presentation.postEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wawra.posts.base.BaseViewModel
import com.wawra.posts.database.entities.Post
import com.wawra.posts.logic.PostRepository
import com.wawra.posts.logic.models.ErrorCodes
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class PostEditViewModel @Inject constructor(private val postRepository: PostRepository) :
    BaseViewModel() {

    // TODO: unit tests

    private val mPost = MutableLiveData<Post>()
    private val mError = MutableLiveData<Int>()
    private val mUpdateResult = MutableLiveData<Boolean>()

    val post: LiveData<Post>
        get() = mPost
    val error: LiveData<Int>
        get() = mError
    val updateResult: LiveData<Boolean>
        get() = mUpdateResult

    fun getPost(postId: Long) {
        postRepository.getPostById(postId)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                { mPost.postValue(it) },
                {
                    it.printStackTrace()
                    mError.postValue(ErrorCodes.POST_EDIT_VIEW_MODEL_GET_POST.code)
                }
            )
            .addToDisposables()
    }

    fun updatePost(postId: Long, title: String, content: String, iconUrl: String) {
        postRepository.updatePost(postId, title, content, iconUrl)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                { mUpdateResult.postValue(it) },
                {
                    it.printStackTrace()
                    mError.postValue(ErrorCodes.POST_EDIT_VIEW_MODEL_UPDATE_POST.code)
                }
            )
            .addToDisposables()
    }

}