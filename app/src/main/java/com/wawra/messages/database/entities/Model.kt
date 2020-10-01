package com.wawra.messages.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "model")
data class Model(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "model_id")
    val modelId: Long
)