package com.wawra.posts.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wawra.posts.database.entities.Post
import com.wawra.posts.logic.models.PostStatus
import io.reactivex.Single

@Dao
abstract class PostDao {

    @Query("DELETE FROM post WHERE status = :unchangedStatus")
    protected abstract fun deleteAllUnchanged(
        unchangedStatus: Int = PostStatus.UNCHANGED.value
    ): Single<Int>

    @Query("SELECT * FROM post WHERE remote_id = :remoteId")
    protected abstract fun getLocalPost(remoteId: Long): Post?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insert(posts: List<Post>): Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPost(post: Post): Single<Long>

    @Query(
        """
        UPDATE post SET
            title = :newTitle,
            description = :newDescription,
            icon_url = :newIconUrl,
            status = :changedStatus
        WHERE post_id = :postId
    """
    )
    abstract fun update(
        postId: Long,
        newTitle: String,
        newDescription: String,
        newIconUrl: String,
        changedStatus: Int = PostStatus.CHANGED.value
    ): Single<Int>

    @Query("SELECT * FROM post WHERE status != :deletedStatus")
    abstract fun getAll(deletedStatus: Int = PostStatus.DELETED.value): Single<List<Post>>

    @Query("SELECT * FROM post WHERE status = :deletedStatus")
    abstract fun getAllDeleted(deletedStatus: Int = PostStatus.DELETED.value): Single<List<Post>>

    @Query("UPDATE post SET status = :deletedStatus WHERE post_id = :postId")
    abstract fun deleteById(
        postId: Long,
        deletedStatus: Int = PostStatus.DELETED.value
    ): Single<Int>

    @Query("UPDATE post SET status = :changedStatus WHERE post_id = :postId")
    abstract fun restoreDeletedById(
        postId: Long,
        changedStatus: Int = PostStatus.CHANGED.value
    ): Single<Int>

    @Query("SELECT * FROM post WHERE post_id = :postId")
    abstract fun getById(postId: Long): Single<Post>

    fun insertPostsFromRemote(posts: List<Post>): Single<List<Long>> = Single.fromCallable {
        val postsToInsert = ArrayList<Post>()
        posts.forEach { remotePost ->
            val localPost = getLocalPost(remotePost.remoteId)
            if (localPost == null || localPost.status == PostStatus.UNCHANGED.value) {
                postsToInsert.add(remotePost.apply { postId = localPost?.postId ?: 0L })
            }
        }
        postsToInsert
    }.flatMap { insert(it) }

}