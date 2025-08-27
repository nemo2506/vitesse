package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.di.NetworkModule
import com.openclassrooms.vitesse.ui.ConstantsApp
import org.junit.Assert.*
import org.junit.Test

class NetworkModuleTest {

    private val networkModule = NetworkModule

    @Test
    fun provideMoshi_returns_instance() {
        // WHEN
        val moshi = networkModule.provideMoshi()
        // THEN
        assertNotNull(moshi)
    }

    @Test
    fun provideRetrofit_returns_instance() {
        // WHEN
        val retrofit = networkModule.provideRetrofit()
        // THEN
        assertNotNull(retrofit)
        assertEquals(ConstantsApp.API_URL, retrofit.baseUrl().toString())
    }

    @Test
    fun provideManageClient_returns_instance() {
        // WHEN
        val retrofit = networkModule.provideRetrofit()
        val client = networkModule.provideManageClient(retrofit)
        // THEN
        assertNotNull(client)
    }
}
