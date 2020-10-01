package com.wawra.messages.network

import com.wawra.messages.network.models.ModelResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ApiInterface {

    @GET("/model")
    fun getModels(): Single<List<ModelResponse>>

}