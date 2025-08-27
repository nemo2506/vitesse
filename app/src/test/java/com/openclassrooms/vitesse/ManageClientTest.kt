package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.network.ManageClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.After
import org.junit.Before
import kotlin.test.Test

class ManageClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var manageClient: ManageClient

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        manageClient = retrofit.create(ManageClient::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * Verifies that fetchGbp returns a valid GbpResponse when API succeeds.
     */
    @Test
    fun fetchGbp_returns_gbpResponse_on_success() = runTest {
        // WHEN
        val mockJson = """ { "eur": { "gbp": 0.85 } } """
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(mockJson))
        val response = manageClient.fetchGbp()
        val body = response.body()
        // THEN
        assertTrue(response.isSuccessful)
        assertNotNull(body)
        body!!.eur?.gbp?.let { assertEquals(0.85, it, 0.0) }
    }

    /**
     * Verifies that fetchGbp returns an error response when API fails (500).
     */
    @Test
    fun fetchGbp_returns_failure_on_500_internal_server_error() = runTest {
        // WHEN
        val errorJson = """ { "error": "Internal Server Error" } """
        mockWebServer.enqueue(MockResponse().setResponseCode(500).setBody(errorJson))
        val response = manageClient.fetchGbp()
        val errorBody = response.errorBody()?.string()
        // THEN
        assertFalse(response.isSuccessful)
        assertTrue(errorBody?.contains("Internal Server Error") == true)
    }

}
