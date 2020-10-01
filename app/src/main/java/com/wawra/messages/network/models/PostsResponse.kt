package com.wawra.messages.network.models

import com.google.gson.annotations.SerializedName

data class PostsResponse(
    @SerializedName("posts")
    val posts: List<PostResponse>
)