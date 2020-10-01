package com.wawra.posts.presentation.posts

interface PostActions {
    fun details(postId: Long)
    fun edit(postId: Long)
    fun delete(postId: Long)
}