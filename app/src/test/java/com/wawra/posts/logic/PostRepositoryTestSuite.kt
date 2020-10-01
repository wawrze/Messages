package com.wawra.posts.logic

import androidx.room.EmptyResultSetException
import com.wawra.posts.BaseTestSuite
import com.wawra.posts.database.daos.PostDao
import com.wawra.posts.database.entities.Post
import com.wawra.posts.logic.models.PostStatus
import com.wawra.posts.network.ApiInterface
import com.wawra.posts.network.models.PostResponse
import com.wawra.posts.network.models.PostsResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
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
        verify { postDaoMock.insertPosts(any()) }

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
        verify { postDaoMock.insertPosts(any()) }

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
        verify { postDaoMock.insertPosts(any()) }

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
        verify(exactly = 0) { postDaoMock.insertPosts(any()) }

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

    // region getPostById
    @Test
    fun shouldFetchPostById() {
        // given
        val post = Post(1L, 2L, "title1", "description1", "iconUrl1", PostStatus.UNCHANGED.value)
        // when
        every { postDaoMock.getById(1L) } returns Single.just(post)
        val result = objectUnderTest.getPostById(1L).blockingGet()
        // then
        verify { postDaoMock.getById(1L) }
        assertEquals(1L, result.postId)
        assertEquals(2L, result.remoteId)
    }

    @Test
    fun shouldNotFetchPostByIdNoPost() {
        // when
        every { postDaoMock.getById(1L) } returns Single.error(EmptyResultSetException(""))
        val result = objectUnderTest.getPostById(1L).blockingGet()
        // then
        verify { postDaoMock.getById(1L) }
        assertEquals(0L, result.postId)
        assertEquals(0L, result.remoteId)
    }
    // endregion getPostById

    // region updatePost
    @Test
    fun shouldUpdatePost() {
        // when
        every {
            postDaoMock.update(1L, "newTitle", "newDescription", "newIconUrl")
        } returns Single.just(1)
        val result = objectUnderTest.updatePost(1L, "newTitle", "newDescription", "newIconUrl")
            .blockingGet()
        // then
        verify { postDaoMock.update(1L, "newTitle", "newDescription", "newIconUrl") }
        assertTrue(result)
    }

    @Test
    fun shouldNotUpdatePost() {
        // when
        every {
            postDaoMock.update(1L, "newTitle", "newDescription", "newIconUrl")
        } returns Single.just(0)
        val result = objectUnderTest.updatePost(1L, "newTitle", "newDescription", "newIconUrl")
            .blockingGet()
        // then
        verify { postDaoMock.update(1L, "newTitle", "newDescription", "newIconUrl") }
        assertFalse(result)
    }

    @Test
    fun shouldNotUpdatePostDbError() {
        // when
        every {
            postDaoMock.update(1L, "newTitle", "newDescription", "newIconUrl")
        } returns Single.error(EmptyResultSetException(""))
        val result = objectUnderTest.updatePost(1L, "newTitle", "newDescription", "newIconUrl")
            .blockingGet()
        // then
        verify { postDaoMock.update(1L, "newTitle", "newDescription", "newIconUrl") }
        assertFalse(result)
    }
    // endregion updatePost

    // region deletePost
    @Test
    fun shouldDeletePost() {
        // when
        every { postDaoMock.deleteById(1L) } returns Single.just(1)
        val result = objectUnderTest.deletePost(1L).blockingGet()
        // then
        verify { postDaoMock.deleteById(1L) }
        assertTrue(result)
    }

    @Test
    fun shouldNotDeletePost() {
        // when
        every { postDaoMock.deleteById(1L) } returns Single.just(0)
        val result = objectUnderTest.deletePost(1L).blockingGet()
        // then
        verify { postDaoMock.deleteById(1L) }
        assertFalse(result)
    }

    @Test
    fun shouldNotDeletePostDbError() {
        // when
        every { postDaoMock.deleteById(1L) } returns Single.error(EmptyResultSetException(""))
        val result = objectUnderTest.deletePost(1L).blockingGet()
        // then
        verify { postDaoMock.deleteById(1L) }
        assertFalse(result)
    }
    // endregion deletePost

    // region getDeletedPosts
    @Test
    fun shouldFetchDeletedPosts() {
        // given
        val post1 = Post(1L, 1L, "title1", "description1", "iconUrl1", PostStatus.DELETED.value)
        val post2 = Post(2L, 2L, "title2", "description2", "iconUrl2", PostStatus.DELETED.value)
        // when
        every { postDaoMock.getAllDeleted() } returns Single.just(listOf(post1, post2))
        val result = objectUnderTest.getDeletedPosts().blockingGet()
        // then
        verify { postDaoMock.getAllDeleted() }
        assertEquals(2, result.size)
        assertEquals(1L, result[0].postId)
        assertEquals(2L, result[1].postId)
    }

    @Test
    fun shouldFetchDeletedPostsEmptyList() {
        // when
        every { postDaoMock.getAllDeleted() } returns Single.just(listOf())
        val result = objectUnderTest.getDeletedPosts().blockingGet()
        // then
        verify { postDaoMock.getAllDeleted() }
        assertEquals(0, result.size)
    }

    @Test
    fun shouldFetchDeletedPostsDbError() {
        // when
        every { postDaoMock.getAllDeleted() } returns Single.error(EmptyResultSetException(""))
        val result = objectUnderTest.getDeletedPosts().blockingGet()
        // then
        verify { postDaoMock.getAllDeleted() }
        assertEquals(0, result.size)
    }
    // endregion getDeletedPosts

    // region restoreDeletedPost
    @Test
    fun shouldRestoreDeletedPost() {
        // when
        every { postDaoMock.restoreDeletedById(1L) } returns Single.just(1)
        val result = objectUnderTest.restoreDeletedPost(1L).blockingGet()
        // then
        verify { postDaoMock.restoreDeletedById(1L) }
        assertTrue(result)
    }

    @Test
    fun shouldNotRestoreDeletedPost() {
        // when
        every { postDaoMock.restoreDeletedById(1L) } returns Single.just(0)
        val result = objectUnderTest.restoreDeletedPost(1L).blockingGet()
        // then
        verify { postDaoMock.restoreDeletedById(1L) }
        assertFalse(result)
    }

    @Test
    fun shouldNotRestoreDeletedPostDbError() {
        // when
        every { postDaoMock.restoreDeletedById(1L) } returns Single.error(EmptyResultSetException(""))
        val result = objectUnderTest.restoreDeletedPost(1L).blockingGet()
        // then
        verify { postDaoMock.restoreDeletedById(1L) }
        assertFalse(result)
    }
    // endregion restoreDeletedPost

}