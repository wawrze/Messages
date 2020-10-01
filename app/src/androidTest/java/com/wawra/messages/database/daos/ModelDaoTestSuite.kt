package com.wawra.messages.database.daos

import com.wawra.messages.BaseDaoTestSuite
import com.wawra.messages.database.entities.Model
import org.junit.Assert.assertEquals
import org.junit.Test

class ModelDaoTestSuite : BaseDaoTestSuite() {

    private val objectUnderTest = db.modelDao()

    @Test
    fun shouldInsertModel() {
        // given
        val model = Model(0L)
        // when
        val result = objectUnderTest.insert(model).blockingGet()
        // then
        assertEquals(1L, result)
    }

    @Test
    fun shouldFetchModels() {
        // given
        val model1 = Model(0L)
        val model2 = Model(0L)
        // when
        objectUnderTest.insert(model1).blockingGet()
        objectUnderTest.insert(model2).blockingGet()
        val result = objectUnderTest.getAll().blockingGet()
        // then
        assertEquals(2, result.size)
        assertEquals(1L, result[0].modelId)
        assertEquals(2L, result[1].modelId)
    }

    @Test
    fun shouldDeleteAll() {
        // given
        val model1 = Model(0L)
        val model2 = Model(0L)
        // when
        objectUnderTest.insert(model1).blockingGet()
        objectUnderTest.insert(model2).blockingGet()
        val modelsBeforeDelete = objectUnderTest.getAll().blockingGet()
        val result = objectUnderTest.deleteAll().blockingGet()
        val modelsAfterDelete = objectUnderTest.getAll().blockingGet()
        // then
        assertEquals(2, modelsBeforeDelete.size)
        assertEquals(2, result)
        assertEquals(0, modelsAfterDelete.size)
    }

}