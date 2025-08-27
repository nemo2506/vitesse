package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.di.NetworkModule
import com.openclassrooms.vitesse.ui.ConstantsApp
import org.junit.Assert.*
import org.junit.Test

class NetworkModuleTest {

    private val networkModule = NetworkModule

    @Test
    fun provideMoshi_returns_instance() {
        val moshi = networkModule.provideMoshi()
        assertNotNull(moshi)
    }

    @Test
    fun provideRetrofit_returns_instance() {
        val retrofit = networkModule.provideRetrofit()
        assertNotNull(retrofit)
        assertEquals(ConstantsApp.API_URL, retrofit.baseUrl().toString())
    }

    @Test
    fun provideManageClient_returns_instance() {
        val retrofit = networkModule.provideRetrofit()
        val client = networkModule.provideManageClient(retrofit)
        assertNotNull(client)
    }
}
