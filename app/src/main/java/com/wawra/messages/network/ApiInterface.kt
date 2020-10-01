package com.wawra.messages.network

import com.wawra.messages.network.models.PostsResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ApiInterface {

    @GET("/DBForCandidates/posts")
    fun getPosts(): Single<PostsResponse>

}