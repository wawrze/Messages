package com.wawra.messages.network

import com.wawra.messages.BaseNetworkTestSuite
import com.wawra.messages.network.models.PostsResponse
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
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
    fun shouldFetchPosts() {
        // given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                "{" +
                        "\"posts\": [" +
                        "{" +
                        "\"id\": 1," +
                        "\"title\": \"title 1\"," +
                        "\"description\": \"description 1\"," +
                        "\"icon\": \"icon 1\"" +
                        "}," +
                        "{" +
                        "\"id\": 2," +
                        "\"title\": \"title 2\"," +
                        "\"description\": \"description 2\"," +
                        "\"icon\": \"icon 2\"" +
                        "}" +
                        "]" +
                        "}"
            )
        // when
        mockServer.enqueue(response)
        val observedResponse = objectUnderTest.getPosts()
        val testObserver = TestObserver<PostsResponse>()
        observedResponse.subscribe(testObserver)
        val request = mockServer.takeRequest(1, TimeUnit.SECONDS)
        // then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val result = testObserver.values()[0]
        val posts = result.posts
        assertEquals("/posts", request?.path)
        assertEquals(2, posts.size)
        assertEquals(1L, posts[0].id)
        assertEquals("title 1", posts[0].title)
        assertEquals("description 1", posts[0].description)
        assertEquals("icon 1", posts[0].icon)
        assertEquals(2L, posts[1].id)
        assertEquals("title 2", posts[1].title)
        assertEquals("description 2", posts[1].description)
        assertEquals("icon 2", posts[1].icon)
    }

    @Test
    fun shouldFetchPostsEmptyList() {
        // given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{ \"posts\": [] }")
        // when
        mockServer.enqueue(response)
        val observedResponse = objectUnderTest.getPosts()
        val testObserver = TestObserver<PostsResponse>()
        observedResponse.subscribe(testObserver)
        val request = mockServer.takeRequest(1, TimeUnit.SECONDS)
        // then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val result = testObserver.values()[0]
        val posts = result.posts
        assertEquals("/posts", request?.path)
        assertEquals(0, posts.size)
    }

}