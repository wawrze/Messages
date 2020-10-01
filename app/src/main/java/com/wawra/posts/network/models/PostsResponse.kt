package com.wawra.posts.network.models

import com.google.gson.annotations.SerializedName

data class PostsResponse(
    @SerializedName("posts")
    val posts: List<PostResponse>
)