package com.wawra.messages.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wawra.messages.database.daos.ModelDao
import com.wawra.messages.database.entities.Model

@Database(
    entities = [Model::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun modelDao(): ModelDao

}