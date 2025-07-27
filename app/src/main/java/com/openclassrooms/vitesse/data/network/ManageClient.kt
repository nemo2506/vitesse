package com.openclassrooms.vitesse.data.network

import com.openclassrooms.vitesse.data.response.GbpResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Interface that defines the API endpoints used for managing user actions such as
 * authentication, balance inquiry, and money transfer.
 */
interface ManageClient {
    /**
     * Sends a login request to the API using the provided [User] credentials.
     *
     * @param get The currency data.
     * @return [Response] containing [GbpResponse] with data result.
     */
    @GET("/v1/currencies/eur.json")
    suspend fun fetchGbp(): Response<GbpResponse>
}
