package com.wawra.messages

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
abstract class BaseInstrumentedTestSuite {

    @Suppress("UNCHECKED_CAST")
    fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }

}