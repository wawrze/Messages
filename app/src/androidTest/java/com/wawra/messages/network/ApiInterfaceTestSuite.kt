package com.wawra.messages.network

import com.wawra.messages.BaseNetworkTestSuite
import com.wawra.messages.network.models.ModelResponse
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class ApiInterfaceTestSuite : BaseNetworkTestSuite() {

    private lateinit var mockServer: MockWebServer
    private lateinit var objectUnderTest: ApiInterface

    @Before
    fun init() {
        mockServer = MockWebServer()
        mockServer.start(9080)
        objectUnderTest = createRetrofit(mockServer.url("/"))
            .create(ApiInterface::class.java)
    }

    @After
    fun cleanUp() = mockServer.shutdown()

    @Test
    fun shouldFetchModels() {
        // given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                "[" +
                        "{" +
                        "\"id\": 1" +
                        "}," +
                        "{" +
                        "\"id\": 2" +
                        "}" +
                        "]"
            )
        // when
        mockServer.enqueue(response)
        val observedResponse = objectUnderTest.getModels()
        val testObserver = TestObserver<List<ModelResponse>>()
        observedResponse.subscribe(testObserver)
        val request = mockServer.takeRequest(1, TimeUnit.SECONDS)
        val result = testObserver.values()[0]
        // then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        Assert.assertEquals("/model", request?.path)
        Assert.assertEquals(2, result.size)
        Assert.assertEquals(1L, result[0].id)
        Assert.assertEquals(2L, result[1].id)
    }

}