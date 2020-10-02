package com.wawra.posts.presentation.deletedPosts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wawra.posts.base.BaseViewModel
import com.wawra.posts.database.entities.Post
import com.wawra.posts.logic.PostRepository
import com.wawra.posts.logic.models.ErrorCodes
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class DeletedPostsViewModel @Inject constructor(private val postRepository: PostRepository) :
    BaseViewModel() {

    private val mPosts = MutableLiveData<List<Post>>()
    private val mError = MutableLiveData<Int>()
    private val mRestoreResult = MutableLiveData<Boolean>()

    val posts: LiveData<List<Post>>
        get() = mPosts
    val error: LiveData<Int>
        get() = mError
    val restoreResult: LiveData<Boolean>
        get() = mRestoreResult

    // TODO: unit tests

    fun getDeletedPosts() {
        postRepository.getDeletedPosts()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                { mPosts.postValue(it) },
                {
                    it.printStackTrace()
                    mError.postValue(ErrorCodes.DELETED_POSTS_VIEW_MODEL_GET_POSTS.code)
                }
            )
            .addToDisposables()
    }

    fun restorePost(postId: Long) {
        postRepository.restoreDeletedPost(postId)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                { mRestoreResult.postValue(it) },
                {
                    it.printStackTrace()
                    mError.postValue(ErrorCodes.DELETED_POSTS_VIEW_MODEL_RESTORE_POST.code)
                }
            )
            .addToDisposables()
    }

}