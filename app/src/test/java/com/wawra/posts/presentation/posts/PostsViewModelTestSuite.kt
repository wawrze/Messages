package com.wawra.posts.presentation.posts

import com.wawra.posts.BaseTestSuite
import com.wawra.posts.database.entities.Post
import com.wawra.posts.logic.PostRepository
import com.wawra.posts.logic.models.ErrorCodes
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PostsViewModelTestSuite : BaseTestSuite() {

    private lateinit var postRepositoryMock: PostRepository
    private lateinit var objectUnderTest: PostsViewModel

    @Before
    fun prepare() {
        postRepositoryMock = mockk()
        objectUnderTest = PostsViewModel(postRepositoryMock)
    }

    @After
    fun cleanUp() {
        objectUnderTest.cancelActions()
    }

    @Test
    fun shouldGetModels() {
        // given
        val posts = listOf(
            Post(1L, 1L),
            Post(2L, 2L)
        )
        // when
        every { postRepositoryMock.getPosts() } returns Observable.just(posts)
        objectUnderTest.getPosts()
        // then
        verify { postRepositoryMock.getPosts() }
        val result = objectUnderTest.posts.value
        val error = objectUnderTest.error.value
        assertNull(error)
        assertNotNull(result)
        result!!
        assertEquals(2, result.size)
        assertEquals(1L, result[0].postId)
        assertEquals(2L, result[1].postId)
    }

    @Test
    fun shouldNotGetModels() {
        // when
        every { postRepositoryMock.getPosts() } returns Observable.error(Exception())
        objectUnderTest.getPosts()
        // then
        verify { postRepositoryMock.getPosts() }
        val result = objectUnderTest.posts.value
        val error = objectUnderTest.error.value
        assertNull(result)
        assertNotNull(error)
        assertEquals(ErrorCodes.POSTS_VIEW_MODEL_GET_POSTS.code, error)
    }

}