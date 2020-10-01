package com.wawra.messages.presentation.main

import com.wawra.messages.BaseTestSuite
import com.wawra.messages.R
import com.wawra.messages.database.entities.Post
import com.wawra.messages.logic.PostRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainViewPostTestSuite : BaseTestSuite() {

    private lateinit var postRepositoryMock: PostRepository
    private lateinit var objectUnderTest: MainViewModel

    @Before
    fun prepare() {
        postRepositoryMock = mockk()
        objectUnderTest = MainViewModel(postRepositoryMock)
    }

    @After
    fun cleanUp() {
        objectUnderTest.cancelActions()
    }

    @Test
    fun shouldGetModels() {
        // given
        val models = listOf(
            Post(1L, 1L),
            Post(2L, 2L)
        )
        // when
        every { postRepositoryMock.getPostsFromDb() } returns Single.just(models)
        objectUnderTest.getPosts()
        // then
        verify { postRepositoryMock.getPostsFromDb() }
        val result = objectUnderTest.models.value
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
        every { postRepositoryMock.getPostsFromDb() } returns Single.error(Exception())
        objectUnderTest.getPosts()
        // then
        verify { postRepositoryMock.getPostsFromDb() }
        val result = objectUnderTest.models.value
        val error = objectUnderTest.error.value
        assertNull(result)
        assertNotNull(error)
        assertEquals(R.string.unknown_error, error)
    }

}