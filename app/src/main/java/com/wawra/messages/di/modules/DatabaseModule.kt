package com.wawra.messages.di.modules

import androidx.room.Room
import com.wawra.messages.App
import com.wawra.messages.database.Database
import com.wawra.messages.database.daos.PostDao
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    companion object {
        private const val DB_NAME = "db.db"
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