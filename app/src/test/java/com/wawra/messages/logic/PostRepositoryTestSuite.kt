package com.wawra.messages.logic

import com.wawra.messages.BaseTestSuite
import com.wawra.messages.database.daos.PostDao
import com.wawra.messages.database.entities.Post
import com.wawra.messages.logic.models.PostStatus
import com.wawra.messages.network.ApiInterface
import com.wawra.messages.network.models.PostResponse
import com.wawra.messages.network.models.PostsResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.observers.TestObserver
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

    // region getPosts
    @Test
    fun shouldFetchPostsFromDbAndApi() {
        // given
        val post1 = Post(1L, 1L, "title1", "description1", "iconUrl1", PostStatus.UNCHANGED.value)
        val post2 = Post(2L, 2L, "title2", "description2", "iconUrl2", PostStatus.UNCHANGED.value)
        val postResponse1 = PostResponse(3L, "title3", "description3", "iconUrl3")
        val postResponse2 = PostResponse(4L, "title4", "description4", "iconUrl4")
        val post3 = Post(3L, 3L, "title3", "description3", "iconUrl3")
        val post4 = Post(4L, 4L, "title4", "description4", "iconUrl4")
        val postsSlot = slot<List<Post>>()
        // when
        every {
            postDaoMock.getAll()
        } returns Single.just(listOf(post1, post2)) andThen Single.just(listOf(post3, post4))
        every {
            apiMock.getPosts()
        } returns Single.just(PostsResponse(listOf(postResponse1, postResponse2)))
        every { postDaoMock.insertPosts(capture(postsSlot)) } returns Single.just(listOf(3L, 4L))
        val result: TestObserver<List<Post>> = objectUnderTest.getPosts().test()
        // then
        verify(exactly = 2) { postDaoMock.getAll() }
        verify { apiMock.getPosts() }
        every { postDaoMock.insertPosts(any()) }

        assertEquals(2, postsSlot.captured.size)
        assertEquals(0L, postsSlot.captured[0].postId)
        assertEquals(3L, postsSlot.captured[0].remoteId)
        assertEquals(0L, postsSlot.captured[1].postId)
        assertEquals(4L, postsSlot.captured[1].remoteId)

        assertEquals(2, result.valueCount())
        val resultFromDb = result.values()[0]
        val resultFromApi = result.values()[1]

        assertEquals(2, resultFromDb.size)
        assertEquals(1L, resultFromDb[0].postId)
        assertEquals(2L, resultFromDb[1].postId)

        assertEquals(2, resultFromApi.size)
        assertEquals(3L, resultFromApi[0].postId)
        assertEquals(4L, resultFromApi[1].postId)
    }

    @Test
    fun shouldFetchPostsEmptyDb() {
        // given
        val postResponse1 = PostResponse(3L, "title3", "description3", "iconUrl3")
        val postResponse2 = PostResponse(4L, "title4", "description4", "iconUrl4")
        val post3 = Post(3L, 3L, "title3", "description3", "iconUrl3")
        val post4 = Post(4L, 4L, "title4", "description4", "iconUrl4")
        val postsSlot = slot<List<Post>>()
        // when
        every {
            postDaoMock.getAll()
        } returns Single.just(listOf()) andThen Single.just(listOf(post3, post4))
        every {
            apiMock.getPosts()
        } returns Single.just(PostsResponse(listOf(postResponse1, postResponse2)))
        every { postDaoMock.insertPosts(capture(postsSlot)) } returns Single.just(listOf(3L, 4L))
        val result: TestObserver<List<Post>> = objectUnderTest.getPosts().test()
        // then
        verify(exactly = 2) { postDaoMock.getAll() }
        verify { apiMock.getPosts() }
        every { postDaoMock.insertPosts(any()) }

        assertEquals(2, postsSlot.captured.size)
        assertEquals(0L, postsSlot.captured[0].postId)
        assertEquals(3L, postsSlot.captured[0].remoteId)
        assertEquals(0L, postsSlot.captured[1].postId)
        assertEquals(4L, postsSlot.captured[1].remoteId)

        assertEquals(2, result.valueCount())
        val resultFromDb = result.values()[0]
        val resultFromApi = result.values()[1]

        assertEquals(0, resultFromDb.size)

        assertEquals(2, resultFromApi.size)
        assertEquals(3L, resultFromApi[0].postId)
        assertEquals(4L, resultFromApi[1].postId)
    }

    @Test
    fun shouldFetchPostsEmptyApiResponse() {
        // given
        val post1 = Post(1L, 1L, "title1", "description1", "iconUrl1", PostStatus.UNCHANGED.value)
        val post2 = Post(2L, 2L, "title2", "description2", "iconUrl2", PostStatus.UNCHANGED.value)
        // when
        every {
            postDaoMock.getAll()
        } returns Single.just(listOf(post1, post2)) andThen Single.just(listOf())
        every {
            apiMock.getPosts()
        } returns Single.just(PostsResponse(listOf()))
        every { postDaoMock.insertPosts(any()) } returns Single.just(listOf())
        val result: TestObserver<List<Post>> = objectUnderTest.getPosts().test()
        // then
        verify(exactly = 2) { postDaoMock.getAll() }
        verify { apiMock.getPosts() }
        every { postDaoMock.insertPosts(any()) }

        assertEquals(2, result.valueCount())
        val resultFromDb = result.values()[0]
        val resultFromApi = result.values()[1]

        assertEquals(2, resultFromDb.size)
        assertEquals(1L, resultFromDb[0].postId)
        assertEquals(2L, resultFromDb[1].postId)

        assertEquals(0, resultFromApi.size)
    }

    @Test
    fun shouldFetchPostsApiError() {
        // given
        val post1 = Post(1L, 1L, "title1", "description1", "iconUrl1", PostStatus.UNCHANGED.value)
        val post2 = Post(2L, 2L, "title2", "description2", "iconUrl2", PostStatus.UNCHANGED.value)
        // when
        every {
            postDaoMock.getAll()
        } returns Single.just(listOf(post1, post2))
        every { apiMock.getPosts() } returns Single.error(Exception())
        every { postDaoMock.insertPosts(any()) } returns Single.just(listOf())
        val result: TestObserver<List<Post>> = objectUnderTest.getPosts().test()
        // then
        verify(exactly = 2) { postDaoMock.getAll() }
        verify { apiMock.getPosts() }
        every { postDaoMock.insertPosts(any()) }

        assertEquals(2, result.valueCount())
        val resultFromDb = result.values()[0]
        val resultFromApi = result.values()[1]

        assertEquals(2, resultFromDb.size)
        assertEquals(1L, resultFromDb[0].postId)
        assertEquals(2L, resultFromDb[1].postId)

        assertEquals(2, resultFromApi.size)
        assertEquals(1L, resultFromApi[0].postId)
        assertEquals(2L, resultFromApi[1].postId)
    }
    // endregion getPosts

}