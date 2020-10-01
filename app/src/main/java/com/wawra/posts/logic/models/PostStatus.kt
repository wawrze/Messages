package com.wawra.posts.logic.models

enum class PostStatus(val value: Int) {
    UNCHANGED(0),
    CHANGED(1),
    DELETED(2)
}