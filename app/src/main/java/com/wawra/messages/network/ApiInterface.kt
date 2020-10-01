package com.wawra.messages.network

import io.reactivex.Single
import retrofit2.http.GET

interface ApiInterface {

    @GET("")
    fun getObjects(): Single<List<Any>>

}