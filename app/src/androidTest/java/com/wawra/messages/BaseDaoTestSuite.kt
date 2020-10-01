package com.wawra.messages

import android.app.Application
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.wawra.messages.database.Database

abstract class BaseDaoTestSuite : BaseInstrumentedTestSuite() {

    protected var db: Database = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application,
        Database::class.java
    ).allowMainThreadQueries().build()

}