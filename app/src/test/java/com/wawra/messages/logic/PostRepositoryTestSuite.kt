package com.wawra.messages.logic

import com.wawra.messages.BaseTestSuite
import com.wawra.messages.database.daos.PostDao
import com.wawra.messages.database.entities.Post
import com.wawra.messages.network.ApiInterface
import com.wawra.messages.network.models.PostResponse
import com.wawra.messages.network.models.PostsResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PostRepositoryTestSuite : BaseTestSuite() {

    private lateinit var postDaoMock: PostDao
    private lateinit var apiMock: ApiInterface

    private lateinit var objectUnderTest: PostRepository

    @Before
    fun init() {
        postDaoMock = mockk()
        apiMock = mockk()
        objectUnderTest = PostRepository(postDaoMock, apiMock)
    }

    @Test
    fun shouldGetModelsFromDb() {
        // given
        val posts = listOf(
            Post(1L, 1L),
            Post(2L, 2L)
        )
        // when
        every { postDaoMock.getAll() } returns Single.just(posts)
        val result = objectUnderTest.getPostsFromDb().blockingGet()
        // then
        verify { postDaoMock.getAll() }
        assertEquals(2, result.size)
        assertEquals(1L, result[0].postId)
        assertEquals(2L, result[1].postId)
    }

    @Test
    fun shouldGetModelsFromApi() {
        // given
        val postsResponse = PostsResponse(listOf(PostResponse(1L), PostResponse(2L)))
        // when
        every { apiMock.getPosts() } returns Single.just(postsResponse)
        val result = objectUnderTest.getPostsFromApi().blockingGet()
        // then
        verify { apiMock.getPosts() }
        assertEquals(2, result.size)
        assertEquals(1L, result[0].remoteId)
        assertEquals(2L, result[1].remoteId)
    }

}