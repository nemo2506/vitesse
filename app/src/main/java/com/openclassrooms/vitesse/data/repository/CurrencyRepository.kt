package com.openclassrooms.vitesse.data.repository

import com.openclassrooms.vitesse.data.network.ManageClient
import com.openclassrooms.vitesse.data.response.GbpResponse
import com.openclassrooms.vitesse.domain.usecase.Result
import retrofit2.Response
import javax.inject.Inject

/**
 * Repository responsible for handling banking-related operations such as login,
 * retrieving account balance, and performing money transfers. Communicates
 * with [ManageClient] to fetch data from the API and transforms the response
 * to domain models.
 *
 * @property dataService The network client used to perform API calls.
 */
class CurrencyRepository @Inject constructor(
    private val dataService: ManageClient
) {

    /**
     *
     */
    suspend fun getGbp(): Result<Double> {
        return try {
            val result: Response<GbpResponse> = dataService.fetchGbp()
            val model = result.body()?.eur?.gbp ?: throw Exception("Invalid data")
            Result.Success(model)
        } catch (error: Exception) {
            Result.Failure(error.message)
        }
    }
}