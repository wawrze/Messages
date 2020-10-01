package com.wawra.messages.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wawra.messages.database.daos.PostDao
import com.wawra.messages.database.entities.Post

@Database(
    entities = [Post::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun postDao(): PostDao

}