package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.response.GbpResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class GbpResponseTest {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Test
    fun gbpResponse_parses_valid_json()  = runTest {
        val json = """ { "eur": { "gbp": 0.85 } } """
        val adapter = moshi.adapter(GbpResponse::class.java)

        val response = adapter.fromJson(json)

        assertNotNull(response)
        response!!.eur?.gbp?.let { assertEquals(0.85, it, 0.0) }
    }

    @Test
    fun gbpResponse_handles_missing_gbp()  = runTest {
        val json = """ { "eur": {} } """  // gbp missing
        val adapter = moshi.adapter(GbpResponse::class.java)

        val response = adapter.fromJson(json)

        assertNotNull(response)
        // Moshi sets default Double = 0.0 if field missing
        response!!.eur?.gbp?.let { assertEquals(0.0, it, 0.0) }
    }

    @Test
    fun gbpResponse_handles_malformed_json()  = runTest {
        val json = """ { "eurr": { "gbp": 0.85 } } """  // typo key
        val adapter = moshi.adapter(GbpResponse::class.java)

        val response = adapter.fromJson(json)

        // parsing succeeds but eur is null → gbp cannot be read
        assertNull(response?.eur)
    }
}
