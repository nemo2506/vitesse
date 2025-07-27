package com.openclassrooms.vitesse.di

import com.openclassrooms.vitesse.data.network.ManageClient
import com.openclassrooms.vitesse.ui.ConstantsApp
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Hilt module that provides network-related dependencies such as Retrofit,
 * OkHttpClient, and API service interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    /**
     * Provides a singleton instance of [Retrofit] configured with a base URL,
     *
     * @return A configured [Retrofit] instance for API calls.
     */
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(ConstantsApp.API_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    /**
     * Provides a singleton instance of [ManageClient], the API interface used for
     * accessing endpoints related to user login, balance, and transfers.
     *
     * @param retrofit The [Retrofit] instance used to create the API service.
     * @return An implementation of [ManageClient].
     */
    @Singleton
    @Provides
    fun provideManageClient(retrofit: Retrofit): ManageClient {
        return retrofit.create(ManageClient::class.java)
    }
}
