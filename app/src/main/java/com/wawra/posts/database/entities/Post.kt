package com.wawra.posts.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wawra.posts.logic.models.PostStatus

@Entity(tableName = "post", indices = [Index(value = ["post_id", "remote_id"], unique = true)])
data class Post(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "post_id")
    var postId: Long,
    @ColumnInfo(name = "remote_id")
    val remoteId: Long,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "icon_url")
    val iconUrl: String = "",
    @ColumnInfo(name = "status")
    val status: Int = PostStatus.UNCHANGED.value
)