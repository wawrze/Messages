package com.wawra.posts.di.modules

import androidx.room.Room
import com.wawra.posts.App
import com.wawra.posts.database.Database
import com.wawra.posts.database.daos.PostDao
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    companion object {
        private const val DB_NAME = "posts.db"
    }

    private lateinit var database: Database

    @Provides
    fun provideDataBase(app: App): Database = if (!::database.isInitialized) {
        database = Room.databaseBuilder(app, Database::class.java, DB_NAME).build()
        database
    } else {
        database
    }

    @Provides
    fun providePostDao(database: Database): PostDao = database.postDao()

}