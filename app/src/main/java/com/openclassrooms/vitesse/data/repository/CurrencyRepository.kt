package com.openclassrooms.vitesse.data.repository

import android.util.Log
import com.openclassrooms.vitesse.data.network.ManageClient
import com.openclassrooms.vitesse.domain.model.GbpReportModel
import com.openclassrooms.vitesse.domain.usecase.Result
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
    suspend fun getGbp(): Result<GbpReportModel> {
        return try {
            val result = dataService.fetchGbp()
            val model = result.body()?.toDomainModel() ?: throw Exception("Invalid data")
            Result.Success(model)
        } catch (error: Exception) {
            Result.Failure(error.message)
        }
    }
}