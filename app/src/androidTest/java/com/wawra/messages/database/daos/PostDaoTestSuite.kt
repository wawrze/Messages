package com.wawra.messages.database.daos

import androidx.room.EmptyResultSetException
import com.wawra.messages.BaseDaoTestSuite
import com.wawra.messages.database.entities.Post
import com.wawra.messages.logic.models.PostStatus
import org.junit.Assert.*
import org.junit.Test

class PostDaoTestSuite : BaseDaoTestSuite() {

    private val objectUnderTest = db.postDao()

    // region insert one
    @Test
    fun shouldInsertPost() {
        // given
        val post = Post(0L, 1L)
        // when
        val result = objectUnderTest.insertPost(post).blockingGet()
        // then
        assertEquals(1L, result)
    }

    @Test
    fun shouldInsertAndReplacePost() {
        // given
        val post = Post(
            0L,
            1L,
            "title",
            "description",
            "iconUrl"
        )
        val newPost = Post(
            1L,
            1L,
            "newTitle",
            "newDescription",
            "newIconUrl"
        )
        // when
        objectUnderTest.insertPost(post).blockingGet()
        val postBeforeReplace = objectUnderTest.getById(1L).blockingGet()
        val result = objectUnderTest.insertPost(newPost).blockingGet()
        val postAfterReplace = objectUnderTest.getById(1L).blockingGet()
        // then
        assertEquals(1L, result)

        assertEquals(1L, postBeforeReplace.postId)
        assertEquals("title", postBeforeReplace.title)
        assertEquals("description", postBeforeReplace.description)
        assertEquals("iconUrl", postBeforeReplace.iconUrl)
        assertEquals(PostStatus.UNCHANGED.value, postBeforeReplace.status)

        assertEquals(1L, postAfterReplace.postId)
        assertEquals("newTitle", postAfterReplace.title)
        assertEquals("newDescription", postAfterReplace.description)
        assertEquals("newIconUrl", postAfterReplace.iconUrl)
        assertEquals(PostStatus.UNCHANGED.value, postAfterReplace.status)
    }
    // endregion insert one

    // region insert many
    @Test
    fun shouldInsertPosts() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1"),
            Post(0L, 2L, "title2", "description2", "iconUrl2")
        )
        // when
        val result = objectUnderTest.insertPosts(posts).blockingGet()
        val postsAfterInsert = objectUnderTest.getAll().blockingGet()
        // then
        assertEquals(2, result.size)
        assertEquals(1L, result[0])
        assertEquals(2L, result[1])

        assertEquals(2, postsAfterInsert.size)

        assertEquals(1L, postsAfterInsert[0].postId)
        assertEquals("title1", postsAfterInsert[0].title)
        assertEquals("description1", postsAfterInsert[0].description)
        assertEquals("iconUrl1", postsAfterInsert[0].iconUrl)
        assertEquals(PostStatus.UNCHANGED.value, postsAfterInsert[0].status)

        assertEquals(2L, postsAfterInsert[1].postId)
        assertEquals("title2", postsAfterInsert[1].title)
        assertEquals("description2", postsAfterInsert[1].description)
        assertEquals("iconUrl2", postsAfterInsert[1].iconUrl)
        assertEquals(PostStatus.UNCHANGED.value, postsAfterInsert[1].status)
    }

    @Test
    fun shouldNotInsertPostsAlreadyExistingAndChanged() {
        // given
        val post1 = Post(0L, 1L, "title1", "description1", "iconUrl1", PostStatus.CHANGED.value)
        val post2 = Post(0L, 2L, "title2", "description2", "iconUrl2", PostStatus.CHANGED.value)
        val newPosts = listOf(
            Post(0L, 1L, "newTitle1", "newDescription1", "newIconUrl1"),
            Post(0L, 2L, "newTitle2", "newDescription2", "newIconUrl2")
        )
        // when
        objectUnderTest.insertPost(post1).blockingGet()
        objectUnderTest.insertPost(post2).blockingGet()
        val result = objectUnderTest.insertPosts(newPosts).blockingGet()
        val postsAfterInsert = objectUnderTest.getAll().blockingGet()
        // then
        assertEquals(2, result.size)
        assertEquals(-1L, result[0])
        assertEquals(-1L, result[1])

        assertEquals(2, postsAfterInsert.size)

        assertEquals(1L, postsAfterInsert[0].postId)
        assertEquals("title1", postsAfterInsert[0].title)
        assertEquals("description1", postsAfterInsert[0].description)
        assertEquals("iconUrl1", postsAfterInsert[0].iconUrl)
        assertEquals(PostStatus.CHANGED.value, postsAfterInsert[0].status)

        assertEquals(2L, postsAfterInsert[1].postId)
        assertEquals("title2", postsAfterInsert[1].title)
        assertEquals("description2", postsAfterInsert[1].description)
        assertEquals("iconUrl2", postsAfterInsert[1].iconUrl)
        assertEquals(PostStatus.CHANGED.value, postsAfterInsert[1].status)
    }

    @Test
    fun shouldInsertPostsAlreadyExistingAndNotChanged() {
        // given
        val post1 = Post(0L, 1L, "title1", "description1", "iconUrl1", PostStatus.UNCHANGED.value)
        val post2 = Post(0L, 2L, "title2", "description2", "iconUrl2", PostStatus.UNCHANGED.value)
        val newPosts = listOf(
            Post(0L, 1L, "newTitle1", "newDescription1", "newIconUrl1"),
            Post(0L, 2L, "newTitle2", "newDescription2", "newIconUrl2")
        )
        // when
        objectUnderTest.insertPost(post1).blockingGet()
        objectUnderTest.insertPost(post2).blockingGet()
        val result = objectUnderTest.insertPosts(newPosts).blockingGet()
        val postsAfterInsert = objectUnderTest.getAll().blockingGet()
        // then
        assertEquals(2, result.size)
        assertEquals(3L, result[0])
        assertEquals(4L, result[1])

        assertEquals(2, postsAfterInsert.size)

        assertEquals(3L, postsAfterInsert[0].postId)
        assertEquals("newTitle1", postsAfterInsert[0].title)
        assertEquals("newDescription1", postsAfterInsert[0].description)
        assertEquals("newIconUrl1", postsAfterInsert[0].iconUrl)
        assertEquals(PostStatus.UNCHANGED.value, postsAfterInsert[0].status)

        assertEquals(4L, postsAfterInsert[1].postId)
        assertEquals("newTitle2", postsAfterInsert[1].title)
        assertEquals("newDescription2", postsAfterInsert[1].description)
        assertEquals("newIconUrl2", postsAfterInsert[1].iconUrl)
        assertEquals(PostStatus.UNCHANGED.value, postsAfterInsert[1].status)
    }

    @Test
    fun shouldInsertOnePostsSecondDeleted() {
        // given
        val oldPost = Post(
            0L,
            1L,
            "title1",
            "description1",
            "iconUrl1",
            PostStatus.DELETED.value
        )
        val newPosts = listOf(
            Post(0L, 1L, "newTitle1", "newDescription1", "newIconUrl1"),
            Post(0L, 2L, "newTitle2", "newDescription2", "newIconUrl2")
        )
        // when
        objectUnderTest.insertPost(oldPost).blockingGet()
        val result = objectUnderTest.insertPosts(newPosts).blockingGet()
        val postsAfterInsert = objectUnderTest.getAllDeleted().blockingGet() +
                objectUnderTest.getAll().blockingGet()
        // then
        assertEquals(2, result.size)
        assertEquals(-1L, result[0])
        assertEquals(3L, result[1])

        assertEquals(2, postsAfterInsert.size)

        assertEquals(1L, postsAfterInsert[0].postId)
        assertEquals("title1", postsAfterInsert[0].title)
        assertEquals("description1", postsAfterInsert[0].description)
        assertEquals("iconUrl1", postsAfterInsert[0].iconUrl)
        assertEquals(PostStatus.DELETED.value, postsAfterInsert[0].status)

        assertEquals(3L, postsAfterInsert[1].postId)
        assertEquals("newTitle2", postsAfterInsert[1].title)
        assertEquals("newDescription2", postsAfterInsert[1].description)
        assertEquals("newIconUrl2", postsAfterInsert[1].iconUrl)
        assertEquals(PostStatus.UNCHANGED.value, postsAfterInsert[1].status)
    }
    // endregion insert many

    // region update
    @Test
    fun shouldUpdatePost() {
        // given
        val post = Post(0L, 1L, "title", "description", "iconUrl")
        // when
        objectUnderTest.insertPost(post).blockingGet()
        val postBeforeUpdate = objectUnderTest.getById(1L).blockingGet()
        val result = objectUnderTest.update(
            1L,
            "newTitle",
            "newDescription",
            "newIconUrl"
        ).blockingGet()
        val postAfterUpdate = objectUnderTest.getById(1L).blockingGet()
        // then
        assertEquals(1, result)

        assertEquals(1L, postBeforeUpdate.postId)
        assertEquals("title", postBeforeUpdate.title)
        assertEquals("description", postBeforeUpdate.description)
        assertEquals("iconUrl", postBeforeUpdate.iconUrl)
        assertEquals(PostStatus.UNCHANGED.value, postBeforeUpdate.status)

        assertEquals(1L, postAfterUpdate.postId)
        assertEquals("newTitle", postAfterUpdate.title)
        assertEquals("newDescription", postAfterUpdate.description)
        assertEquals("newIconUrl", postAfterUpdate.iconUrl)
        assertEquals(PostStatus.CHANGED.value, postAfterUpdate.status)
    }

    @Test
    fun shouldNotUpdatePostNoPost() {
        // when
        val result = objectUnderTest.update(
            1L,
            "newTitle",
            "newDescription",
            "newIconUrl"
        ).blockingGet()
        // then
        assertEquals(0, result)
    }
    // endregion update

    // region getAll
    @Test
    fun shouldFetchPosts() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1"),
            Post(0L, 2L, "title2", "description2", "iconUrl2")
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val result = objectUnderTest.getAll().blockingGet()
        // then
        assertEquals(2, result.size)
        assertEquals(1L, result[0].postId)
        assertEquals(2L, result[1].postId)
    }

    @Test
    fun shouldFetchPostsEmptyList() {
        // when
        val result = objectUnderTest.getAll().blockingGet()
        // then
        assertEquals(0, result.size)
    }

    @Test
    fun shouldFetchPostsEmptyListAllDeleted() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1", PostStatus.DELETED.value),
            Post(0L, 2L, "title2", "description2", "iconUrl2", PostStatus.DELETED.value)
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val result = objectUnderTest.getAll().blockingGet()
        // then
        assertEquals(0, result.size)
    }

    @Test
    fun shouldFetchPostsWithoutDeleted() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1", PostStatus.DELETED.value),
            Post(0L, 2L, "title2", "description2", "iconUrl2")
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val result = objectUnderTest.getAll().blockingGet()
        // then
        assertEquals(1, result.size)
        assertEquals(2L, result[0].postId)
    }
    // endregion getAll

    // region getAllDeleted
    @Test
    fun shouldFetchDeletedPosts() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1", PostStatus.DELETED.value),
            Post(0L, 2L, "title2", "description2", "iconUrl2", PostStatus.DELETED.value)
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val result = objectUnderTest.getAllDeleted().blockingGet()
        // then
        assertEquals(2, result.size)
        assertEquals(1L, result[0].postId)
        assertEquals(2L, result[1].postId)
    }

    @Test
    fun shouldFetchDeletedPostsEmptyList() {
        // when
        val result = objectUnderTest.getAllDeleted().blockingGet()
        // then
        assertEquals(0, result.size)
    }

    @Test
    fun shouldFetchDeletedPostsEmptyListNoPostsDeleted() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1"),
            Post(0L, 2L, "title2", "description2", "iconUrl2")
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val result = objectUnderTest.getAllDeleted().blockingGet()
        // then
        assertEquals(0, result.size)
    }

    @Test
    fun shouldFetchDeletedPostsWithoutOthers() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1", PostStatus.CHANGED.value),
            Post(0L, 2L, "title2", "description2", "iconUrl2", PostStatus.DELETED.value),
            Post(0L, 3L, "title3", "description3", "iconUrl3", PostStatus.UNCHANGED.value),
            Post(0L, 4L, "title4", "description4", "iconUrl4", PostStatus.DELETED.value)
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val result = objectUnderTest.getAllDeleted().blockingGet()
        // then
        assertEquals(2, result.size)
        assertEquals(2L, result[0].postId)
        assertEquals(4L, result[1].postId)
    }
    // endregion getAllDeleted

    // region deleteById
    @Test
    fun shouldDeleteById() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1"),
            Post(0L, 2L, "title2", "description2", "iconUrl2"),
            Post(0L, 3L, "title3", "description3", "iconUrl3"),
            Post(0L, 4L, "title4", "description4", "iconUrl4")
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val postsBeforeDelete = objectUnderTest.getAll().blockingGet()
        val result = objectUnderTest.deleteById(2L).blockingGet()
        val postsAfterDelete = objectUnderTest.getAll().blockingGet()
        val deletedPostsAfterDelete = objectUnderTest.getAllDeleted().blockingGet()
        // then
        assertEquals(1, result)
        assertEquals(4, postsBeforeDelete.size)
        assertEquals(3, postsAfterDelete.size)
        assertEquals(1, deletedPostsAfterDelete.size)
        assertEquals(2L, deletedPostsAfterDelete[0].postId)
    }

    @Test
    fun shouldDeleteByIdAlreadyDeleted() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1"),
            Post(0L, 2L, "title2", "description2", "iconUrl2", PostStatus.DELETED.value),
            Post(0L, 3L, "title3", "description3", "iconUrl3"),
            Post(0L, 4L, "title4", "description4", "iconUrl4")
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val postsBeforeDelete = objectUnderTest.getAll().blockingGet()
        val result = objectUnderTest.deleteById(2L).blockingGet()
        val postsAfterDelete = objectUnderTest.getAll().blockingGet()
        val deletedPostsAfterDelete = objectUnderTest.getAllDeleted().blockingGet()
        // then
        assertEquals(1, result)
        assertEquals(3, postsBeforeDelete.size)
        assertEquals(3, postsAfterDelete.size)
        assertEquals(1, deletedPostsAfterDelete.size)
        assertEquals(2L, deletedPostsAfterDelete[0].postId)
    }

    @Test
    fun shouldNotDeleteByIdNoPost() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1"),
            Post(0L, 2L, "title2", "description2", "iconUrl2"),
            Post(0L, 3L, "title3", "description3", "iconUrl3"),
            Post(0L, 4L, "title4", "description4", "iconUrl4")
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val postsBeforeDelete = objectUnderTest.getAll().blockingGet()
        val result = objectUnderTest.deleteById(5L).blockingGet()
        val postsAfterDelete = objectUnderTest.getAll().blockingGet()
        val deletedPostsAfterDelete = objectUnderTest.getAllDeleted().blockingGet()
        // then
        assertEquals(0, result)
        assertEquals(4, postsBeforeDelete.size)
        assertEquals(4, postsAfterDelete.size)
        assertEquals(0, deletedPostsAfterDelete.size)
    }
    // endregion deleteById

    // region restoreDeletedById
    @Test
    fun shouldRestoreDeletedById() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1"),
            Post(0L, 2L, "title2", "description2", "iconUrl2"),
            Post(0L, 3L, "title3", "description3", "iconUrl3", PostStatus.DELETED.value),
            Post(0L, 4L, "title4", "description4", "iconUrl4")
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val postsBeforeRestore = objectUnderTest.getAll().blockingGet()
        val result = objectUnderTest.restoreDeletedById(3L).blockingGet()
        val postsAfterRestore = objectUnderTest.getAll().blockingGet()
        val deletedPostsAfterRestore = objectUnderTest.getAllDeleted().blockingGet()
        // then
        assertEquals(1, result)
        assertEquals(3, postsBeforeRestore.size)
        assertEquals(4, postsAfterRestore.size)
        assertEquals(0, deletedPostsAfterRestore.size)
    }

    @Test
    fun shouldRestoreDeletedByIdNoDeleted() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1"),
            Post(0L, 2L, "title2", "description2", "iconUrl2"),
            Post(0L, 3L, "title3", "description3", "iconUrl3"),
            Post(0L, 4L, "title4", "description4", "iconUrl4")
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val postsBeforeRestore = objectUnderTest.getAll().blockingGet()
        val result = objectUnderTest.restoreDeletedById(3L).blockingGet()
        val postsAfterRestore = objectUnderTest.getAll().blockingGet()
        val deletedPostsAfterRestore = objectUnderTest.getAllDeleted().blockingGet()
        // then
        assertEquals(1, result)
        assertEquals(4, postsBeforeRestore.size)
        assertEquals(4, postsAfterRestore.size)
        assertEquals(0, deletedPostsAfterRestore.size)
    }

    @Test
    fun shouldNotRestoreDeletedByIdNoPost() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1"),
            Post(0L, 2L, "title2", "description2", "iconUrl2"),
            Post(0L, 3L, "title3", "description3", "iconUrl3"),
            Post(0L, 4L, "title4", "description4", "iconUrl4")
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val postsBeforeRestore = objectUnderTest.getAll().blockingGet()
        val result = objectUnderTest.restoreDeletedById(5L).blockingGet()
        val postsAfterRestore = objectUnderTest.getAll().blockingGet()
        val deletedPostsAfterRestore = objectUnderTest.getAllDeleted().blockingGet()
        // then
        assertEquals(0, result)
        assertEquals(4, postsBeforeRestore.size)
        assertEquals(4, postsAfterRestore.size)
        assertEquals(0, deletedPostsAfterRestore.size)
    }
    // endregion restoreDeletedById

    // region getById
    @Test
    fun shouldFetchPostById() {
        // given
        val posts = listOf(
            Post(0L, 1L, "title1", "description1", "iconUrl1"),
            Post(0L, 2L, "title2", "description2", "iconUrl2"),
            Post(0L, 3L, "title3", "description3", "iconUrl3"),
            Post(0L, 4L, "title4", "description4", "iconUrl4")
        )
        // when
        objectUnderTest.insertPosts(posts).blockingGet()
        val result = objectUnderTest.getById(3L).blockingGet()
        // then
        assertEquals(3L, result.postId)
        assertEquals("title3", result.title)
        assertEquals("description3", result.description)
        assertEquals("iconUrl3", result.iconUrl)
        assertEquals(PostStatus.UNCHANGED.value, result.status)
    }

    @Test
    fun shouldNotFetchPostByIdNoPost() {
        // given
        var result: Post? = null
        var error: Exception? = null
        // when
        try {
            result = objectUnderTest.getById(1L).blockingGet()
        } catch (e: Exception) {
            error = e
        }
        // then
        assertNull(result)
        assertTrue(error is EmptyResultSetException)
    }
    // endregion getById

}