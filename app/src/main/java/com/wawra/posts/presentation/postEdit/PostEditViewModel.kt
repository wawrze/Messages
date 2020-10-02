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
    private val mSaveResult = MutableLiveData<Long>()

    val post: LiveData<Post>
        get() = mPost
    val error: LiveData<Int>
        get() = mError
    val saveResult: LiveData<Long>
        get() = mSaveResult

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

    fun savePost(postId: Long, title: String, content: String, iconUrl: String) {
        if (postId == 0L) {
            postRepository.createPost(title, content, iconUrl)
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(
                    { mSaveResult.postValue(it) },
                    {
                        it.printStackTrace()
                        mError.postValue(ErrorCodes.POST_EDIT_VIEW_MODEL_CREATE_POST.code)
                    }
                )
                .addToDisposables()
        } else {
            postRepository.updatePost(postId, title, content, iconUrl)
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(
                    { mSaveResult.postValue(if (it) postId else 0L) },
                    {
                        it.printStackTrace()
                        mError.postValue(ErrorCodes.POST_EDIT_VIEW_MODEL_SAVE_POST.code)
                    }
                )
                .addToDisposables()
        }
    }

}