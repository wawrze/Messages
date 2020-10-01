package com.wawra.messages.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wawra.messages.R
import com.wawra.messages.base.BaseViewModel
import com.wawra.messages.database.entities.Model
import com.wawra.messages.logic.ModelRepository
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class MainViewModel @Inject constructor(private val modelRepository: ModelRepository) :
    BaseViewModel() {

    private val mModels = MutableLiveData<List<Model>>()
    private val mError = MutableLiveData<Int>()

    val models: LiveData<List<Model>>
        get() = mModels
    val error: LiveData<Int>
        get() = mError

    fun getModels() {
        modelRepository.getModelsFromDb()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                { mModels.postValue(it) },
                {
                    it.printStackTrace()
                    mError.postValue(R.string.unknown_error)
                }
            )
            .addToDisposables()
    }

}