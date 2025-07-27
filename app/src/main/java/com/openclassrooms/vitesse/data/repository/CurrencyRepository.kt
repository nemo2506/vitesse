package com.openclassrooms.vitesse.data.repository

import com.openclassrooms.vitesse.data.network.ManageClient
import com.openclassrooms.vitesse.domain.model.GbpReportModel
import com.openclassrooms.vitesse.domain.usecase.Result
import javax.inject.Inject

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