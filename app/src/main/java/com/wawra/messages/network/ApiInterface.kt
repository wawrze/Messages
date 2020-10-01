package com.wawra.messages.network

import com.wawra.messages.network.models.PostsResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ApiInterface {

    @GET("/v3/3eda8539-d850-49bb-874f-f95d19713ee2")
    fun getPosts(): Single<PostsResponse>

}