package com.wawra.posts.network.models

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("id")
    val id: Long = 0L,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("icon")
    val icon: String = ""
)