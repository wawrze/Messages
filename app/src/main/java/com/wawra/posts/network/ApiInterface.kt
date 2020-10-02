package com.wawra.posts.network

import com.wawra.posts.network.models.PostsResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ApiInterface {

    //    @GET("/DBForCandidates/posts")
    @GET("/v3/3eda8539-d850-49bb-874f-f95d19713ee2")
    fun getPosts(): Single<PostsResponse>

}