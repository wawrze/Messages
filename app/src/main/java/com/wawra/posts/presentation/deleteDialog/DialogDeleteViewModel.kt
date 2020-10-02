package com.wawra.posts.presentation.deleteDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wawra.posts.base.BaseViewModel
import com.wawra.posts.logic.PostRepository
import com.wawra.posts.logic.models.ErrorCodes
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class DialogDeleteViewModel @Inject constructor(private val postRepository: PostRepository) :
    BaseViewModel() {

    // TODO: unit tests

    private val mPostTitle = MutableLiveData<String>()
    private val mError = MutableLiveData<Int>()
    private val mDeleteResult = MutableLiveData<Boolean>()

    val postTitle: LiveData<String>
        get() = mPostTitle
    val error: LiveData<Int>
        get() = mError
    val deleteResult: LiveData<Boolean>
        get() = mDeleteResult

    fun getPostTitle(postId: Long) {
        postRepository.getPostById(postId)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                { mPostTitle.postValue(it.title) },
                {
                    it.printStackTrace()
                    mError.postValue(ErrorCodes.DELETE_DIALOG_VIEW_MODEL_GET_POST.code)
                }
            )
            .addToDisposables()
    }

    fun deletePost(postId: Long) {
        postRepository.deletePost(postId)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                { mDeleteResult.postValue(it) },
                {
                    it.printStackTrace()
                    mError.postValue(ErrorCodes.DELETE_DIALOG_VIEW_MODEL_DELETE_POST.code)
                }
            )
            .addToDisposables()
    }

}