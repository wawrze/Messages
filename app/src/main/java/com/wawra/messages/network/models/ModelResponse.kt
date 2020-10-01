package com.wawra.messages.network.models

import com.google.gson.annotations.SerializedName

data class ModelResponse(
    @SerializedName("id")
    val id: Long
)