package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.repository.CurrencyRepository
import com.openclassrooms.vitesse.domain.usecase.Result
import com.openclassrooms.vitesse.data.network.ManageClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class CurrencyRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var manageClient: ManageClient
    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        manageClient = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ManageClient::class.java)

        currencyRepository = CurrencyRepository(manageClient)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getGbp_returns_success() = runTest {
        // WHEN
        val responseBody = """
            {
              "eur": {
                "gbp": 0.85
              }
            }
        """.trimIndent()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json")
        )

        val result = currencyRepository.getGbp()
        // THEN
        assertTrue(result is Result.Success)
        assertEquals(0.85, result.value)
    }

    @Test
    fun getGbp_returns_failure_400() = runTest {
        // WHEN
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody("")
        )
        val result = currencyRepository.getGbp()
        // THEN
        assertTrue(result is Result.Failure)
        assertNotNull(result.message)
    }

    @Test
    fun getGbp_returns_failure_malformed() = runTest {
        // WHEN
        val badJson = "{ bad_json }" // JSON invalide volontaire
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(badJson)
                .addHeader("Content-Type", "application/json")
        )
        val result = currencyRepository.getGbp()
        // THEN
        assertTrue(result is Result.Failure)
        assertNotNull(result.message)
    }
}