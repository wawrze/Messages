package com.wawra.posts.logic.models

enum class ErrorCodes(val code: Int) {
    POSTS_VIEW_MODEL_GET_POSTS(1),
    POST_DETAILS_VIEW_MODEL_GET_POST(2),
    DELETE_DIALOG_VIEW_MODEL_GET_POST(3),
    DELETE_DIALOG_VIEW_MODEL_DELETE_POST(4),
}